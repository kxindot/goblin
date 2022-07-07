package com.kxindot.goblin;

import static com.kxindot.goblin.Objects.isBlank;
import static com.kxindot.goblin.Objects.isNotBlank;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;

import com.kxindot.goblin.exception.RuntimeException;

/**
 * @author zhaoqingjiang
 */
public class IO {
    
    public static String readString(URI uri) {
        return new String(readBytes(uri));
    }
    
    public static String readString(URL url) {
        return new String(readBytes(url));
    }
    
    public static String readString(InputStream inputStream) {
        return new String(readBytes(inputStream));
    }
    
    public static byte[] readBytes(URI uri) {
        try {
            return readBytes(uri.toURL().openStream());
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static byte[] readBytes(URL url) {
        try {
            return readBytes(url.openStream());
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
    
    public static byte[] readBytes(InputStream inputStream) {
        try (InputStream in = inputStream;
                ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            int i;
            byte[] buf = new byte[1024 * 1024];
            while ((i = in.read(buf)) != -1) {out.write(buf, 0, i);}
            return out.toByteArray();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
    
    
    public static void write(OutputStream outputStream, CharSequence charSequence) {
        write(outputStream, charSequence.toString().getBytes());
    }
    
    public static void write(OutputStream outputStream, byte[] bytes) {
        write(outputStream, new ByteArrayInputStream(bytes));
    }
    
    public static void write(OutputStream outputStream, InputStream inputStream) {
        try (InputStream in = inputStream) {
            int i;
            byte[] buf = new byte[1024 * 1024];
            while ((i = in.read(buf)) != -1) {outputStream.write(buf, 0, i);}
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
    
    /**
     * Create a new {@link IORuntimeException}
     * @param cause Throwable
     * @return IORuntimeException
     */
    public static IORuntimeException newIORuntimeException(IOException cause) {
        return newIORuntimeException(cause, null);
    }
    
    /**
     * Create a new {@link IORuntimeException}
     * @param cause Throwable
     * @param message String
     * @param args {@code Object...}
     * @return IORuntimeException
     */
    public static IORuntimeException newIORuntimeException(IOException cause, String message, Object... args) {
        String msg = cause.getMessage();
        if (isNotBlank(msg)) {
            msg = "Cause : " + msg;
            message = isBlank(message) ? msg : String.join("; ", message, msg);
        }
        return newIORuntimeException(cause, message, args);
    }

    
    /**
     * This customized exception implements {@link RuntimeException}.
     * It is designed to wrap {@link IOException}, and the developer won't 
     * check {@link IOException} all the time.
     * @author zhaoqingjiang
     */
    public static class IORuntimeException extends RuntimeException {

        private static final long serialVersionUID = 7496540237542866176L;

        public IORuntimeException(IOException cause, String message, Object... args) {
            super(cause, message, args);
        }

        public IORuntimeException(IOException cause, String message) {
            super(cause, message);
        }

        public IORuntimeException(IOException cause) {
            super(cause);
        }
    }
    
}
