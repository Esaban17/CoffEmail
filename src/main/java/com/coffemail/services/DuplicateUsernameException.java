package com.coffemail.services;

/**
 * Se lanza al intentar registrar un nombre de usuario que ya existe.
 *
 * <p>La versión original no comprobaba duplicados: registrar dos veces el mismo
 * usuario agregaba una segunda línea al archivo y, peor aún, sobrescribía la
 * fotografía del primero, porque la copia usaba el nombre de usuario como
 * nombre de archivo con {@code REPLACE_EXISTING}.
 *
 * @author Estuardo Sabán
 */
public class DuplicateUsernameException extends Exception {

    private static final long serialVersionUID = 1L;

    public DuplicateUsernameException(String username) {
        super("El usuario '" + username + "' ya está registrado.");
    }
}
