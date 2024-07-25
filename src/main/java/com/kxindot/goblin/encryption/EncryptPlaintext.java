package com.kxindot.goblin.encryption;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.jasypt.iv.RandomIvGenerator;
import org.jasypt.salt.RandomSaltGenerator;

/**
 * Jasypt encryption tool
 *
 * @author 2024-07-15
 */
public class EncryptPlaintext {

    private static final CaesarCipher caesarCipher = new CaesarCipher();

    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            System.err.println("args must not be empty");
            return;
        }

        String visiblePassword = ResourceUtils.loadTxtFileFromResource("visible_jasypt_password");
        String password = caesarCipher.encrypt(visiblePassword, 1);
        StringEncryptor encryptor = stringEncryptor(password);

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg == null || arg.isEmpty()) {
                System.err.println("args[" + i + "] must not be empty");
                continue;
            }
            String result = encryptor.encrypt(arg);
            System.out.println(arg + "  ->  ENC(" + result + ")");

        }
    }

    public static StringEncryptor stringEncryptor(String password) {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(password);
        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGenerator(new RandomSaltGenerator());
        config.setIvGenerator(new RandomIvGenerator());
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }

}
