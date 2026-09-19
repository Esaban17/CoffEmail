package com.coffemail.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Derivación y verificación de contraseñas con PBKDF2-HMAC-SHA512.
 *
 * <p>Sustituye al MD5 con salt de la versión original, que tenía dos problemas
 * graves: MD5 es demasiado rápido para hashear contraseñas (una GPU calcula
 * miles de millones por segundo) y el salt se generaba pero nunca se guardaba,
 * de modo que la contraseña jamás podía verificarse después.
 *
 * <p>El valor devuelto por {@link #hash(char[])} es autocontenido y tiene el
 * formato {@code pbkdf2$<iteraciones>$<salt-base64>$<hash-base64>}, así que
 * basta con almacenar ese único campo para poder verificar más adelante.
 *
 * @author Estuardo Sabán
 */
public final class PasswordHasher {

    private static final String ALGORITHM = "PBKDF2WithHmacSHA512";
    private static final String PREFIX = "pbkdf2";
    private static final String SEPARATOR = "\\$";

    /** Mínimo recomendado por OWASP (2023) para PBKDF2-HMAC-SHA512. */
    private static final int DEFAULT_ITERATIONS = 210_000;
    private static final int KEY_LENGTH_BITS = 256;
    private static final int SALT_LENGTH_BYTES = 16;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private PasswordHasher() {
        // clase de utilidades
    }

    /**
     * Deriva un hash nuevo con un salt aleatorio.
     *
     * @param password contraseña en claro; el llamador debería limpiar el arreglo después
     * @return cadena autocontenida lista para guardarse
     */
    public static String hash(char[] password) {
        byte[] salt = new byte[SALT_LENGTH_BYTES];
        SECURE_RANDOM.nextBytes(salt);
        return hash(password, salt, DEFAULT_ITERATIONS);
    }

    /**
     * Deriva un hash con salt e iteraciones explícitos. Visible para las pruebas,
     * que necesitan resultados reproducibles.
     */
    static String hash(char[] password, byte[] salt, int iterations) {
        byte[] derived = derive(password, salt, iterations);
        Base64.Encoder encoder = Base64.getEncoder();
        return PREFIX + "$" + iterations + "$"
                + encoder.encodeToString(salt) + "$"
                + encoder.encodeToString(derived);
    }

    /**
     * Verifica una contraseña contra un hash almacenado.
     *
     * <p>La comparación es de tiempo constante, para no filtrar información por
     * el tiempo de respuesta.
     *
     * @param password  contraseña en claro a comprobar
     * @param storedHash valor previamente devuelto por {@link #hash(char[])}
     * @return {@code true} solo si la contraseña corresponde al hash
     */
    public static boolean verify(char[] password, String storedHash) {
        if (password == null || storedHash == null) {
            return false;
        }
        String[] parts = storedHash.split(SEPARATOR);
        if (parts.length != 4 || !PREFIX.equals(parts[0])) {
            return false;
        }
        final int iterations;
        final byte[] salt;
        final byte[] expected;
        try {
            iterations = Integer.parseInt(parts[1]);
            salt = Base64.getDecoder().decode(parts[2]);
            expected = Base64.getDecoder().decode(parts[3]);
        } catch (IllegalArgumentException ex) {
            // Registro corrupto o escrito por una versión incompatible.
            return false;
        }
        if (iterations <= 0 || salt.length == 0 || expected.length == 0) {
            return false;
        }
        byte[] actual = derive(password, salt, iterations, expected.length * Byte.SIZE);
        return MessageDigest.isEqual(expected, actual);
    }

    private static byte[] derive(char[] password, byte[] salt, int iterations) {
        return derive(password, salt, iterations, KEY_LENGTH_BITS);
    }

    private static byte[] derive(char[] password, byte[] salt, int iterations, int keyLengthBits) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLengthBits);
        try {
            return SecretKeyFactory.getInstance(ALGORITHM).generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            // PBKDF2WithHmacSHA512 es parte de la plataforma desde Java 8: si falta,
            // la JVM está rota y no hay forma sensata de que la interfaz lo resuelva.
            throw new IllegalStateException("La JVM no soporta " + ALGORITHM, ex);
        } finally {
            spec.clearPassword();
        }
    }

}
