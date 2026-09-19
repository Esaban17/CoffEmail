package com.coffemail.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class RegistrationValidatorTest {

    private static List<String> validateWith(String username, String phone, String birthDate,
                                             String password, String confirm) {
        return RegistrationValidator.validate(username, "Estuardo", "Sabán",
                "esaban@coffemail.test", phone, birthDate, "/tmp/foto.png",
                password.toCharArray(), confirm.toCharArray());
    }

    @Test
    @DisplayName("un formulario correcto no produce errores")
    void acceptsValidForm() {
        assertEquals(List.of(),
                validateWith("esaban", "+502 5555-1234", "14-03-1998", "Cafe1234", "Cafe1234"));
    }

    @Test
    @DisplayName("un campo con solo espacios ya no cuenta como lleno")
    void rejectsWhitespaceOnlyFields() {
        List<String> errors = RegistrationValidator.validate("   ", "  ", "\t", "  ", " ", " ", " ",
                "Cafe1234".toCharArray(), "Cafe1234".toCharArray());
        assertEquals(7, errors.size());
    }

    @Test
    @DisplayName("se informa cada problema por separado, no un mensaje genérico")
    void reportsEachProblemSeparately() {
        List<String> errors = validateWith("x", "abc", "32-13-2020", "corta", "otra");
        assertTrue(errors.size() >= 4, "se esperaban errores de usuario, teléfono, fecha y contraseña");
    }

    @ParameterizedTest
    @ValueSource(strings = {"esaban@coffemail.test", "a.b+c@sub.dominio.gt", "x_1@d.co"})
    @DisplayName("acepta correos con forma válida")
    void acceptsValidEmails(String email) {
        assertTrue(RegistrationValidator.isValidEmail(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {"esaban", "esaban@", "@dominio.com", "a@b", "a b@c.com", "a@b..com"})
    @DisplayName("rechaza correos mal formados")
    void rejectsInvalidEmails(String email) {
        assertFalse(RegistrationValidator.isValidEmail(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {"55551234", "+502 5555-1234", "502-5555-1234"})
    @DisplayName("acepta teléfonos con código de país, espacios y guiones")
    void acceptsValidPhones(String phone) {
        assertTrue(RegistrationValidator.isValidPhone(phone));
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "123", "+", "5555 1234 ext 9a"})
    @DisplayName("rechaza teléfonos mal formados")
    void rejectsInvalidPhones(String phone) {
        assertFalse(RegistrationValidator.isValidPhone(phone));
    }

    @Test
    @DisplayName("un teléfono largo con código de país ya no desborda como pasaba con int")
    void acceptsPhoneLongerThanIntRange() {
        assertTrue(RegistrationValidator.isValidPhone("50212345678"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"14-03-1998", "14/03/1998", "1998-03-14"})
    @DisplayName("interpreta los formatos de fecha que puede producir el DateChooser")
    void parsesSupportedDateFormats(String value) {
        assertEquals(LocalDate.of(1998, 3, 14),
                RegistrationValidator.parseBirthDate(value).orElseThrow());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "no es fecha", "32-01-2020", "2020-13-01"})
    @DisplayName("rechaza fechas inválidas en vez de guardar null")
    void rejectsInvalidDates(String value) {
        assertTrue(RegistrationValidator.parseBirthDate(value).isEmpty());
    }

    @Test
    @DisplayName("rechaza una fecha de nacimiento en el futuro")
    void rejectsFutureBirthDate() {
        String tomorrow = LocalDate.now().plusDays(1).toString();
        assertTrue(RegistrationValidator.parseBirthDate(tomorrow).isEmpty());
    }

    @Test
    @DisplayName("rechaza la fecha de hoy, que es la que pre-llena el DateChooser")
    void rejectsTodayBecauseTheDateChooserPrefillsIt() {
        assertTrue(RegistrationValidator.parseBirthDate(LocalDate.now().toString()).isEmpty());
    }

    @Test
    @DisplayName("rechaza fechas absurdamente antiguas")
    void rejectsImplausiblyOldBirthDate() {
        assertTrue(RegistrationValidator.parseBirthDate("01-01-1800").isEmpty());
        assertTrue(RegistrationValidator.parseBirthDate(
                LocalDate.now().minusYears(30).toString()).isPresent());
    }

    @Test
    @DisplayName("exige longitud mínima, letras y números, y que ambas coincidan")
    void enforcesPasswordRules() {
        assertTrue(RegistrationValidator.validatePassword("Cafe1234".toCharArray(),
                "Cafe1234".toCharArray()).isEmpty());
        assertFalse(RegistrationValidator.validatePassword("Cafe12".toCharArray(),
                "Cafe12".toCharArray()).isEmpty());
        assertFalse(RegistrationValidator.validatePassword("solamenteletras".toCharArray(),
                "solamenteletras".toCharArray()).isEmpty());
        assertFalse(RegistrationValidator.validatePassword("12345678".toCharArray(),
                "12345678".toCharArray()).isEmpty());
        assertFalse(RegistrationValidator.validatePassword("Cafe1234".toCharArray(),
                "Cafe4321".toCharArray()).isEmpty());
        assertFalse(RegistrationValidator.validatePassword(new char[0], new char[0]).isEmpty());
    }
}
