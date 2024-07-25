package com.kxindot.goblin.encryption;

/**
 * Caesar cipher encryption/decryption
 *
 * @author 2024-07-15
 */
public class CaesarCipher {

    public String encrypt(String plainText, int shift) {
        StringBuilder encryptedText = new StringBuilder();
        for (char c : plainText.toCharArray()) {
            int shifted = (c + shift - 1) % 127 + 1;
            encryptedText.append((char) shifted);
        }
        return encryptedText.toString();
    }

    public String decrypt(String encryptedText, int shift) {
        StringBuilder decryptedText = new StringBuilder();
        for (char c : encryptedText.toCharArray()) {
            int shifted = (c - shift - 1 + 127) % 127 + 1;
            decryptedText.append((char) shifted);
        }
        return decryptedText.toString();
    }

}
