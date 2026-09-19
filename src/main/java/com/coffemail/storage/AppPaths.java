package com.coffemail.storage;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Ubicación de los datos de la aplicación.
 *
 * <p>La versión original tenía {@code "C:/MEIA/usuario.txt"} escrito a mano en
 * dos lugares distintos, lo que hacía imposible ejecutar la aplicación fuera de
 * Windows: en Linux o macOS se creaba un directorio llamado literalmente
 * {@code C:} dentro del directorio de trabajo.
 *
 * <p>La ruta puede sobrescribirse con la propiedad de sistema
 * {@code coffemail.data.dir}, lo que además permite que las pruebas escriban en
 * un directorio temporal en vez de en el perfil real del usuario.
 *
 * @author Estuardo Sabán
 */
public final class AppPaths {

    /** Propiedad de sistema que sobrescribe el directorio de datos. */
    public static final String DATA_DIR_PROPERTY = "coffemail.data.dir";

    private static final String DEFAULT_DIR_NAME = ".coffemail";

    private AppPaths() {
        // clase de utilidades
    }

    /** @return directorio base donde vive todo el estado de la aplicación */
    public static Path dataDir() {
        String override = System.getProperty(DATA_DIR_PROPERTY);
        if (override != null && !override.isBlank()) {
            return Paths.get(override);
        }
        return Paths.get(System.getProperty("user.home"), DEFAULT_DIR_NAME);
    }

    /** @return archivo de usuarios */
    public static Path usersFile() {
        return dataDir().resolve("usuarios.txt");
    }

    /** @return directorio de fotografías de perfil */
    public static Path imagesDir() {
        return dataDir().resolve("images");
    }
}
