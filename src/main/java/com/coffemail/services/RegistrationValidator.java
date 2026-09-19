package com.coffemail.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Validación de los datos del formulario de registro.
 *
 * <p>Vive fuera de la vista para poder probarse sin levantar Swing. La versión
 * original solo comprobaba que los campos no fueran la cadena vacía —un espacio
 * en blanco pasaba— y mostraba siempre el mismo mensaje genérico.
 *
 * @author Estuardo Sabán
 */
public final class RegistrationValidator {

    /** Longitud mínima de contraseña exigida al registrarse. */
    public static final int MIN_PASSWORD_LENGTH = 8;

    /** Edad máxima admitida, como cota de cordura para la fecha de nacimiento. */
    private static final int MAX_AGE_YEARS = 120;

    private static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");
    private static final Pattern PHONE = Pattern.compile("^\\+?[0-9][0-9 -]{6,18}[0-9]$");
    private static final Pattern USERNAME = Pattern.compile("^[a-zA-Z0-9._-]{3,32}$");

    /** Formatos de fecha aceptados, en orden de preferencia. */
    private static final List<DateTimeFormatter> DATE_FORMATS = List.of(
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ISO_LOCAL_DATE);

    private RegistrationValidator() {
        // clase de utilidades
    }

    /**
     * Revisa todos los campos y devuelve los problemas encontrados.
     *
     * @return lista de mensajes; vacía si el formulario es válido
     */
    public static List<String> validate(String username, String name, String lastName, String email,
                                        String phone, String birthDate, String photoPath,
                                        char[] password, char[] passwordConfirm) {
        List<String> errors = new ArrayList<>();

        if (isBlank(username)) {
            errors.add("El usuario es obligatorio.");
        } else if (!USERNAME.matcher(username.trim()).matches()) {
            errors.add("El usuario debe tener entre 3 y 32 caracteres (letras, números, punto, guion o guion bajo).");
        }
        if (isBlank(name)) {
            errors.add("El nombre es obligatorio.");
        }
        if (isBlank(lastName)) {
            errors.add("El apellido es obligatorio.");
        }
        if (isBlank(email)) {
            errors.add("El correo electrónico es obligatorio.");
        } else if (!isValidEmail(email)) {
            errors.add("El correo electrónico no tiene un formato válido.");
        }
        if (isBlank(phone)) {
            errors.add("El teléfono es obligatorio.");
        } else if (!isValidPhone(phone)) {
            errors.add("El teléfono solo admite dígitos, espacios, guiones y un '+' inicial.");
        }
        if (isBlank(birthDate)) {
            errors.add("La fecha de nacimiento es obligatoria.");
        } else if (parseBirthDate(birthDate).isEmpty()) {
            errors.add("La fecha de nacimiento no es válida. Use el formato dd-MM-yyyy "
                    + "y una fecha anterior a hoy.");
        }
        if (isBlank(photoPath)) {
            errors.add("La fotografía es obligatoria.");
        }
        errors.addAll(validatePassword(password, passwordConfirm));
        return errors;
    }

    /** Reglas de contraseña, separadas para poder reutilizarlas en un cambio de contraseña. */
    public static List<String> validatePassword(char[] password, char[] passwordConfirm) {
        List<String> errors = new ArrayList<>();
        if (password == null || password.length == 0) {
            errors.add("La contraseña es obligatoria.");
            return errors;
        }
        if (password.length < MIN_PASSWORD_LENGTH) {
            errors.add("La contraseña debe tener al menos " + MIN_PASSWORD_LENGTH + " caracteres.");
        }
        if (!hasLetter(password) || !hasDigit(password)) {
            errors.add("La contraseña debe combinar al menos una letra y un número.");
        }
        if (passwordConfirm == null || !Arrays.equals(password, passwordConfirm)) {
            errors.add("Las contraseñas no coinciden.");
        }
        return errors;
    }

    /** @return {@code true} si el correo tiene una forma plausible */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL.matcher(email.trim()).matches();
    }

    /** @return {@code true} si el teléfono tiene una forma plausible */
    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE.matcher(phone.trim()).matches();
    }

    /**
     * Interpreta la fecha de nacimiento aceptando varios formatos.
     *
     * <p>La versión original asumía {@code dd-MM-yyyy}, pero el componente
     * DateChooser no garantiza ese formato; ahora se tolera también
     * {@code dd/MM/yyyy} e ISO-8601.
     *
     * <p>Se exige además que la fecha sea estrictamente anterior a hoy: el
     * DateChooser pre-llena el campo con la fecha actual, así que sin esta
     * comprobación un usuario que ignorara el campo quedaría registrado con la
     * fecha de nacimiento de hoy.
     *
     * @return la fecha, o vacío si no se pudo interpretar o está fuera de rango
     */
    public static Optional<LocalDate> parseBirthDate(String value) {
        if (isBlank(value)) {
            return Optional.empty();
        }
        String trimmed = value.trim();
        for (DateTimeFormatter format : DATE_FORMATS) {
            try {
                LocalDate parsed = LocalDate.parse(trimmed, format);
                return isPlausibleBirthDate(parsed) ? Optional.of(parsed) : Optional.empty();
            } catch (DateTimeParseException ignored) {
                // se intenta con el siguiente formato
            }
        }
        return Optional.empty();
    }

    private static boolean isPlausibleBirthDate(LocalDate date) {
        LocalDate today = LocalDate.now();
        return date.isBefore(today) && date.isAfter(today.minusYears(MAX_AGE_YEARS));
    }

    private static boolean hasLetter(char[] value) {
        for (char c : value) {
            if (Character.isLetter(c)) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasDigit(char[] value) {
        for (char c : value) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
