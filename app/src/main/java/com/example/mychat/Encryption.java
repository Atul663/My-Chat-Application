package com.example.mychat;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {

    private byte encryptionKey[] = {9, 115, 51, 86, 105, 4, -31, -23, -68, 88, 17, 20, 3, -105, 119, -53};
    private Cipher cipher, deCipher;
    private SecretKeySpec secretKeySpec;

    Encryption()
    {
        try {
            cipher = Cipher.getInstance("AES");
            deCipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }

        secretKeySpec = new SecretKeySpec(encryptionKey, "AES");
    }


    public String encryptMessage(String message) {
        byte messageByte[] = message.getBytes();
        byte encrypteByte[] = new byte[messageByte.length];

        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            encrypteByte = cipher.doFinal(messageByte);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }

        String finalMessage = null;

        try {
            finalMessage = new String(encrypteByte, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return finalMessage;
    }


    public String decryptMessage(String message) throws UnsupportedEncodingException {
        byte [] encryptedByte = message.getBytes("ISO-8859-1");
        String decrypteMessage = message;

        byte [] decryption;

        try {
            deCipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            decryption = deCipher.doFinal(encryptedByte);
            decrypteMessage = new String(decryption);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }

        return decrypteMessage;
    }
}
