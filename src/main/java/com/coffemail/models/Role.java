package com.coffemail.models;

/**
 * Rol de un usuario dentro de la aplicación.
 *
 * <p>Reemplaza al {@code boolean role} original, que solo podía expresar dos
 * estados y no dejaba claro cuál era cuál al leer el archivo de datos.
 *
 * @author Estuardo Sabán
 */
public enum Role {
    /** Primer usuario registrado: administra la aplicación. */
    ADMIN,
    /** Usuario estándar. */
    USER;

    /**
     * Convierte el valor almacenado en disco a un rol, tolerando registros
     * escritos por versiones anteriores que guardaban {@code true}/{@code false}.
     *
     * @param stored texto tal como aparece en el archivo de usuarios
     * @return el rol correspondiente, o {@link #USER} si el valor es desconocido
     */
    public static Role fromStorage(String stored) {
        if (stored == null) {
            return USER;
        }
        String value = stored.trim();
        if ("true".equalsIgnoreCase(value)) {
            return ADMIN;
        }
        if ("false".equalsIgnoreCase(value)) {
            return USER;
        }
        try {
            return Role.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return USER;
        }
    }
}
