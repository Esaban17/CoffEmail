package com.coffemail.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PasswordHasherTest {

    @Test
    @DisplayName("el hash no contiene la contraseña en claro")
    void hashDoesNotLeakPassword() {
        String hash = PasswordHasher.hash("Cafe1234".toCharArray());
        assertFalse(hash.contains("Cafe1234"));
    }

    @Test
    @DisplayName("el hash es autocontenido: algoritmo, iteraciones, salt y derivado")
    void hashIsSelfContained() {
        String[] parts = PasswordHasher.hash("Cafe1234".toCharArray()).split("\\$");
        assertEquals(4, parts.length);
        assertEquals("pbkdf2", parts[0]);
        assertTrue(Integer.parseInt(parts[1]) >= 210_000);
    }

    @Test
    @DisplayName("verify acepta la contraseña correcta")
    void verifyAcceptsCorrectPassword() {
        String hash = PasswordHasher.hash("Cafe1234".toCharArray());
        assertTrue(PasswordHasher.verify("Cafe1234".toCharArray(), hash));
    }

    @Test
    @DisplayName("verify rechaza una contraseña incorrecta")
    void verifyRejectsWrongPassword() {
        String hash = PasswordHasher.hash("Cafe1234".toCharArray());
        assertFalse(PasswordHasher.verify("cafe1234".toCharArray(), hash));
        assertFalse(PasswordHasher.verify("Cafe12345".toCharArray(), hash));
        assertFalse(PasswordHasher.verify(new char[0], hash));
    }

    @Test
    @DisplayName("dos registros de la misma contraseña usan salts distintos")
    void samePasswordProducesDifferentHashes() {
        String first = PasswordHasher.hash("Cafe1234".toCharArray());
        String second = PasswordHasher.hash("Cafe1234".toCharArray());
        assertNotEquals(first, second, "cada hash debe llevar su propio salt aleatorio");
        assertTrue(PasswordHasher.verify("Cafe1234".toCharArray(), first));
        assertTrue(PasswordHasher.verify("Cafe1234".toCharArray(), second));
    }

    @Test
    @DisplayName("con el mismo salt e iteraciones el resultado es reproducible")
    void hashIsDeterministicForAGivenSalt() {
        byte[] salt = new byte[16];
        assertEquals(
                PasswordHasher.hash("Cafe1234".toCharArray(), salt, 1000),
                PasswordHasher.hash("Cafe1234".toCharArray(), salt, 1000));
    }

    @Test
    @DisplayName("las contraseñas con acentos se tratan como UTF-8, no según el SO")
    void handlesNonAsciiPasswords() {
        String hash = PasswordHasher.hash("contraseña-Ñ-1".toCharArray());
        assertTrue(PasswordHasher.verify("contraseña-Ñ-1".toCharArray(), hash));
    }

    @Test
    @DisplayName("verify rechaza hashes nulos, vacíos o corruptos sin lanzar excepción")
    void verifyRejectsMalformedHashes() {
        char[] password = "Cafe1234".toCharArray();
        assertFalse(PasswordHasher.verify(password, null));
        assertFalse(PasswordHasher.verify(password, ""));
        assertFalse(PasswordHasher.verify(password, "no-es-un-hash"));
        assertFalse(PasswordHasher.verify(password, "md5$1$aaaa$bbbb"));
        assertFalse(PasswordHasher.verify(password, "pbkdf2$abc$aaaa$bbbb"));
        assertFalse(PasswordHasher.verify(password, "pbkdf2$1000$!!!!$bbbb"));
        assertFalse(PasswordHasher.verify(password, "pbkdf2$0$aaaa$bbbb"));
        assertFalse(PasswordHasher.verify(null, "pbkdf2$1000$aaaa$bbbb"));
    }
}
