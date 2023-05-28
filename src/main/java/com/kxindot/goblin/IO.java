package com.kxindot.goblin;

import static com.kxindot.goblin.Throws.silentThrex;

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
public interface IO {
    
    public interface IOInput {
        void read();
        
        void read(int bufSize);
    }
    
    public interface IOOutput {
        
    }
    
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
        InputStream iStream = null;
        try {
            iStream = uri.toURL().openStream();
        } catch (IOException e) {
            silentThrex(e);
        }
        return readBytes(iStream);
    }

    public static byte[] readBytes(URL url) {
        InputStream iStream = null;
        try {
            iStream = url.openStream();
        } catch (IOException e) {
            silentThrex(e);
        }
        return readBytes(iStream);
    }
    
    public static byte[] readBytes(InputStream inputStream) {
        byte[] buf = null;
        try (InputStream in = inputStream;
                ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            int i;
            buf = new byte[1024 * 1024];
            while ((i = in.read(buf)) != -1) {out.write(buf, 0, i);}
            return out.toByteArray();
        } catch (IOException e) {
            silentThrex(e);
        }
        return buf;
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
            throw new RuntimeException();
        }
    }
}
