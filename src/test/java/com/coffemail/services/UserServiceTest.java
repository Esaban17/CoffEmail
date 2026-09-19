package com.coffemail.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.coffemail.models.Role;
import com.coffemail.models.User;
import com.coffemail.storage.PhotoStore;
import com.coffemail.storage.UserRepository;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class UserServiceTest {

    @TempDir
    Path tempDir;

    private UserRepository repository;
    private UserService service;
    private Path photo;

    @BeforeEach
    void setUp() throws IOException {
        repository = new UserRepository(tempDir.resolve("usuarios.txt"));
        service = new UserService(repository, new PhotoStore(tempDir.resolve("images")));
        photo = tempDir.resolve("foto.png");
        ImageIO.write(new BufferedImage(8, 8, BufferedImage.TYPE_INT_RGB), "png", photo.toFile());
    }

    private static User newUser(String username) {
        User user = new User();
        user.setUsername(username);
        user.setName("Estuardo");
        user.setLastName("Sabán");
        user.setEmail(username + "@coffemail.test");
        user.setPhone("+502 5555-1234");
        user.setBirthDate(LocalDate.of(1998, 3, 14));
        return user;
    }

    @Test
    @DisplayName("el primer usuario registrado es ADMIN y los siguientes son USER")
    void firstUserBecomesAdmin() throws Exception {
        assertEquals(Role.ADMIN,
                service.register(newUser("esaban"), "Cafe1234".toCharArray(), photo).getRole());
        assertEquals(Role.USER,
                service.register(newUser("segundo"), "Cafe1234".toCharArray(), photo).getRole());
    }

    @Test
    @DisplayName("el registro guarda un hash, nunca la contraseña en claro")
    void neverStoresPlainTextPassword() throws Exception {
        service.register(newUser("esaban"), "Cafe1234".toCharArray(), photo);

        String contents = Files.readString(tempDir.resolve("usuarios.txt"), StandardCharsets.UTF_8);
        assertFalse(contents.contains("Cafe1234"));
        assertTrue(contents.contains("pbkdf2$"));
    }

    @Test
    @DisplayName("la contraseña se limpia del arreglo después de registrar")
    void clearsPasswordArrayAfterUse() throws Exception {
        char[] password = "Cafe1234".toCharArray();
        service.register(newUser("esaban"), password, photo);

        assertEquals(0, password[0]);
        assertEquals("\0".repeat(8), new String(password));
    }

    @Test
    @DisplayName("no permite registrar dos veces el mismo usuario")
    void rejectsDuplicateUsername() throws Exception {
        service.register(newUser("esaban"), "Cafe1234".toCharArray(), photo);

        assertThrows(DuplicateUsernameException.class,
                () -> service.register(newUser("esaban"), "Otra1234".toCharArray(), photo));
        assertThrows(DuplicateUsernameException.class,
                () -> service.register(newUser("ESABAN"), "Otra1234".toCharArray(), photo));
        assertEquals(1, repository.findAll().size());
    }

    @Test
    @DisplayName("rechaza una fotografía que no es una imagen real")
    void rejectsInvalidPhoto() throws IOException {
        Path fake = Files.writeString(tempDir.resolve("falsa.png"), "no soy png",
                StandardCharsets.UTF_8);

        assertThrows(IllegalArgumentException.class,
                () -> service.register(newUser("esaban"), "Cafe1234".toCharArray(), fake));
        assertTrue(repository.findAll().isEmpty(), "no debe quedar un usuario a medias");
    }

    @Test
    @DisplayName("la contraseña registrada puede verificarse después: el salt se conserva")
    void authenticatesWithTheRegisteredPassword() throws Exception {
        service.register(newUser("esaban"), "Cafe1234".toCharArray(), photo);

        Optional<User> authenticated = service.authenticate("esaban", "Cafe1234".toCharArray());
        assertTrue(authenticated.isPresent(), "este era el fallo de fondo del esquema anterior");
        assertEquals("esaban", authenticated.get().getUsername());
        assertNotNull(authenticated.get().getPhotoPath());
    }

    @Test
    @DisplayName("rechaza credenciales incorrectas y usuarios inexistentes")
    void rejectsBadCredentials() throws Exception {
        service.register(newUser("esaban"), "Cafe1234".toCharArray(), photo);

        assertTrue(service.authenticate("esaban", "incorrecta1".toCharArray()).isEmpty());
        assertTrue(service.authenticate("no-existe", "Cafe1234".toCharArray()).isEmpty());
    }

    @Test
    @DisplayName("un usuario inactivo no puede autenticarse")
    void rejectsInactiveUser() throws Exception {
        service.register(newUser("esaban"), "Cafe1234".toCharArray(), photo);

        // Da de baja la cuenta reescribiendo el último campo del registro.
        Path file = tempDir.resolve("usuarios.txt");
        List<String> lines = new ArrayList<>(Files.readAllLines(file, StandardCharsets.UTF_8));
        int last = lines.size() - 1;
        lines.set(last, lines.get(last).replaceAll("true$", "false"));
        Files.write(file, lines, StandardCharsets.UTF_8);

        assertTrue(service.authenticate("esaban", "Cafe1234".toCharArray()).isEmpty());
    }
}
