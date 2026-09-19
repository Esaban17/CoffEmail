package com.coffemail.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class PhotoStoreTest {

    @TempDir
    Path tempDir;

    private PhotoStore store;

    @BeforeEach
    void setUp() {
        store = new PhotoStore(tempDir.resolve("images"));
    }

    private Path writePng(String fileName) throws IOException {
        Path png = tempDir.resolve(fileName);
        ImageIO.write(new BufferedImage(8, 8, BufferedImage.TYPE_INT_RGB), "png", png.toFile());
        return png;
    }

    @Test
    @DisplayName("guarda la imagen con el nombre del usuario y crea el directorio")
    void storesImageUnderUsername() throws IOException {
        Path stored = store.store("esaban", writePng("foto.png"));

        assertTrue(Files.exists(stored));
        assertEquals("esaban.png", stored.getFileName().toString());
        assertEquals(tempDir.resolve("images"), stored.getParent());
    }

    @Test
    @DisplayName("la ruta devuelta usa el separador de la plataforma, no '\\' fijo")
    void returnedPathIsPlatformCorrect() throws IOException {
        Path stored = store.store("esaban", writePng("foto.png"));
        assertEquals(stored, Path.of(stored.toString()));
    }

    @Test
    @DisplayName("rechaza un archivo que no existe")
    void rejectsMissingFile() {
        assertThrows(IllegalArgumentException.class,
                () -> store.store("esaban", tempDir.resolve("no-existe.png")));
    }

    @Test
    @DisplayName("rechaza una extensión no soportada")
    void rejectsUnsupportedExtension() throws IOException {
        Path document = Files.writeString(tempDir.resolve("cv.pdf"), "no soy imagen",
                StandardCharsets.UTF_8);
        assertThrows(IllegalArgumentException.class, () -> store.store("esaban", document));
    }

    @Test
    @DisplayName("rechaza un archivo que solo finge ser imagen por la extensión")
    void rejectsFileThatIsNotReallyAnImage() throws IOException {
        Path fake = Files.writeString(tempDir.resolve("virus.png"), "MZ no soy un png",
                StandardCharsets.UTF_8);
        assertThrows(IllegalArgumentException.class, () -> store.store("esaban", fake));
    }

    @Test
    @DisplayName("extensionOf normaliza a minúsculas y tolera archivos sin extensión")
    void extensionOfHandlesEdgeCases() {
        assertEquals(".png", PhotoStore.extensionOf(Path.of("Foto.PNG")));
        assertEquals(".jpeg", PhotoStore.extensionOf(Path.of("a.b.jpeg")));
        assertEquals("", PhotoStore.extensionOf(Path.of("sin_extension")));
        assertEquals("", PhotoStore.extensionOf(Path.of("termina_en_punto.")));
    }
}
