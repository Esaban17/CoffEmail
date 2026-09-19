package com.coffemail.storage;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import javax.imageio.ImageIO;

/**
 * Copia y valida las fotografías de perfil.
 *
 * <p>La versión original aceptaba cualquier archivo, devolvía {@code ""} en
 * silencio cuando la copia fallaba y armaba la ruta de retorno concatenando
 * {@code "\\"} a mano, lo que producía rutas inválidas fuera de Windows.
 *
 * @author Estuardo Sabán
 */
public class PhotoStore {

    /** Extensiones aceptadas, en minúsculas y con punto. */
    static final Set<String> ALLOWED_EXTENSIONS = Set.of(".png", ".jpg", ".jpeg", ".gif", ".bmp");

    /** Tamaño máximo aceptado para una fotografía de perfil. */
    static final long MAX_SIZE_BYTES = 5L * 1024L * 1024L;

    private final Path imagesDir;

    /** Crea un almacén sobre la ubicación estándar de la aplicación. */
    public PhotoStore() {
        this(AppPaths.imagesDir());
    }

    /**
     * @param imagesDir directorio donde se guardarán las fotografías
     */
    public PhotoStore(Path imagesDir) {
        this.imagesDir = imagesDir;
    }

    /**
     * Valida la imagen de origen y la copia al almacén con el nombre del usuario.
     *
     * @param username nombre de usuario, usado como nombre de archivo
     * @param source   archivo elegido por el usuario
     * @return la ruta definitiva de la copia
     * @throws IOException              si la copia falla
     * @throws IllegalArgumentException si el archivo no existe, excede el tamaño
     *                                  máximo o no es una imagen válida
     */
    public Path store(String username, Path source) throws IOException {
        if (source == null || !Files.isRegularFile(source)) {
            throw new IllegalArgumentException("El archivo de fotografía no existe.");
        }
        if (Files.size(source) > MAX_SIZE_BYTES) {
            throw new IllegalArgumentException(
                    "La fotografía supera el tamaño máximo de " + (MAX_SIZE_BYTES / 1024 / 1024) + " MB.");
        }
        String extension = extensionOf(source);
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException(
                    "Formato no soportado. Use PNG, JPG, GIF o BMP.");
        }
        if (!isReadableImage(source)) {
            // No basta con la extensión: se comprueba que el contenido sea una imagen.
            throw new IllegalArgumentException("El archivo seleccionado no es una imagen válida.");
        }

        Files.createDirectories(imagesDir);
        Path target = imagesDir.resolve(username + extension);
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        return target;
    }

    /**
     * @return la extensión en minúsculas, incluyendo el punto, o cadena vacía si no hay
     */
    static String extensionOf(Path path) {
        String fileName = path.getFileName().toString();
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot < 0 || lastDot == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(lastDot).toLowerCase(Locale.ROOT);
    }

    private static boolean isReadableImage(Path source) {
        try {
            BufferedImage image = ImageIO.read(source.toFile());
            return image != null;
        } catch (IOException ex) {
            return false;
        }
    }
}
