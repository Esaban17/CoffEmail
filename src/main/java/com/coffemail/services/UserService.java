package com.coffemail.services;

import com.coffemail.models.Role;
import com.coffemail.models.User;
import com.coffemail.security.PasswordHasher;
import com.coffemail.storage.PhotoStore;
import com.coffemail.storage.UserRepository;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Reglas de negocio de usuarios: registro y autenticación.
 *
 * <p>Ocupa el lugar del antiguo {@code UserController}, que en realidad mezclaba
 * tres responsabilidades —hashear contraseñas, copiar archivos y escribir en
 * disco— en una sola clase imposible de probar por partes.
 *
 * @author Estuardo Sabán
 */
public class UserService {

    private final UserRepository users;
    private final PhotoStore photos;

    /** Crea el servicio sobre las ubicaciones estándar de la aplicación. */
    public UserService() {
        this(new UserRepository(), new PhotoStore());
    }

    /**
     * @param users  repositorio de usuarios
     * @param photos almacén de fotografías
     */
    public UserService(UserRepository users, PhotoStore photos) {
        this.users = users;
        this.photos = photos;
    }

    /**
     * Registra un usuario nuevo.
     *
     * <p>El primer usuario del sistema recibe el rol {@link Role#ADMIN}; los
     * demás, {@link Role#USER}. Antes esa decisión se tomaba en la vista
     * comprobando si el archivo de datos existía, lo que metía detalles de
     * persistencia dentro de la interfaz gráfica.
     *
     * <p>La contraseña se limpia del arreglo antes de devolver, de modo que no
     * quede en memoria más tiempo del necesario.
     *
     * @param user            datos del usuario, sin contraseña ni ruta final de foto
     * @param password        contraseña en claro
     * @param sourcePhotoPath archivo de fotografía elegido por el usuario
     * @return el usuario tal como quedó guardado
     * @throws DuplicateUsernameException si el nombre de usuario ya existe
     * @throws IllegalArgumentException   si la fotografía no es válida
     * @throws IOException                si falla la escritura en disco
     */
    public User register(User user, char[] password, Path sourcePhotoPath)
            throws DuplicateUsernameException, IOException {
        try {
            if (users.findByUsername(user.getUsername()).isPresent()) {
                throw new DuplicateUsernameException(user.getUsername());
            }

            user.setRole(users.isEmpty() ? Role.ADMIN : Role.USER);
            user.setPasswordHash(PasswordHasher.hash(password));
            user.setPhotoPath(photos.store(user.getUsername(), sourcePhotoPath).toString());
            user.setActive(true);

            users.save(user);
            return user;
        } finally {
            clear(password);
        }
    }

    /**
     * Verifica unas credenciales.
     *
     * <p>Esto solo es posible porque ahora el salt se guarda junto al hash: con
     * el esquema anterior, el salt se descartaba tras registrar y ninguna
     * contraseña podía volver a comprobarse.
     *
     * @return el usuario si las credenciales son correctas y la cuenta está activa
     */
    public Optional<User> authenticate(String username, char[] password) throws IOException {
        try {
            Optional<User> found = users.findByUsername(username);
            if (found.isEmpty()) {
                return Optional.empty();
            }
            User user = found.get();
            if (!user.isActive() || !PasswordHasher.verify(password, user.getPasswordHash())) {
                return Optional.empty();
            }
            return Optional.of(user);
        } finally {
            clear(password);
        }
    }

    private static void clear(char[] password) {
        if (password != null) {
            java.util.Arrays.fill(password, '\0');
        }
    }
}
