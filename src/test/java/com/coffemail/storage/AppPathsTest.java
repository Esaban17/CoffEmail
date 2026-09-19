package com.coffemail.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AppPathsTest {

    @AfterEach
    void clearOverride() {
        System.clearProperty(AppPaths.DATA_DIR_PROPERTY);
    }

    @Test
    @DisplayName("por defecto los datos viven en el perfil del usuario, no en C:/MEIA")
    void defaultsToUserHome() {
        System.clearProperty(AppPaths.DATA_DIR_PROPERTY);
        Path dataDir = AppPaths.dataDir();

        assertTrue(dataDir.startsWith(Paths.get(System.getProperty("user.home"))));
        assertFalse(dataDir.toString().contains("MEIA"));
        assertFalse(dataDir.toString().startsWith("C:"),
                "la ruta no debe estar fijada a una unidad de Windows");
    }

    @Test
    @DisplayName("la propiedad de sistema permite redirigir los datos")
    void honorsSystemPropertyOverride() {
        System.setProperty(AppPaths.DATA_DIR_PROPERTY, Paths.get("build", "datos").toString());

        assertEquals(Paths.get("build", "datos"), AppPaths.dataDir());
        assertEquals(Paths.get("build", "datos", "usuarios.txt"), AppPaths.usersFile());
        assertEquals(Paths.get("build", "datos", "images"), AppPaths.imagesDir());
    }

    @Test
    @DisplayName("una propiedad vacía no anula el valor por defecto")
    void ignoresBlankOverride() {
        System.setProperty(AppPaths.DATA_DIR_PROPERTY, "   ");
        assertTrue(AppPaths.dataDir().startsWith(Paths.get(System.getProperty("user.home"))));
    }
}
