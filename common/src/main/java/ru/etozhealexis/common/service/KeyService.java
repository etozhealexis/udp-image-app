package ru.etozhealexis.common.service;

import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * A class for key management
 */
@Getter
@Service
public class KeyService {

    private SecretKey secretKey;

    private static int KEY_SIZE = 256;

    /**
     * Generates encrypted key
     *
     * @param receiverPublicKeyBytes receiver public key
     * @return encrypted key
     */
    @SneakyThrows
    public byte[] generateEncryptedKey(byte[] receiverPublicKeyBytes) {
        secretKey = generateSecretKey();
        PublicKey receiverPublicKey = generatePublicKey(receiverPublicKeyBytes);
        return generateEncryptedKey(receiverPublicKey, secretKey);
    }

    /**
     * Encrypts payload
     *
     * @param payload payload
     * @return ecrypted payload
     */
    @SneakyThrows
    public byte[] encryptPayload(byte[] payload) {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(payload);
    }

    /**
     * Generates secret key
     *
     * @return secret key
     */
    private SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(KEY_SIZE);
        return keyGenerator.generateKey();
    }

    /**
     * Generates outer public key
     *
     * @param receiverPublicKeyBytes inner public key
     * @return outer public key
     */
    private PublicKey generatePublicKey(byte[] receiverPublicKeyBytes)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(receiverPublicKeyBytes);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * Generates encrypted key
     *
     * @param receiverPublicKey receiver public key
     * @param secretKey         secret key
     * @return encrypted key
     */
    private byte[] generateEncryptedKey(PublicKey receiverPublicKey, SecretKey secretKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, receiverPublicKey);
        return cipher.doFinal(secretKey.getEncoded());
    }
}
