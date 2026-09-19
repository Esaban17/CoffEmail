package com.coffemail.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.coffemail.models.Role;
import com.coffemail.models.User;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class UserRepositoryTest {

    @TempDir
    Path tempDir;

    private Path file;
    private UserRepository repository;

    @BeforeEach
    void setUp() {
        file = tempDir.resolve("usuarios.txt");
        repository = new UserRepository(file);
    }

    private static User sampleUser(String username) {
        return new User(username, "Estuardo", "Sabán", "pbkdf2$210000$c2FsdA==$aGFzaA==",
                Role.USER, LocalDate.of(1998, 3, 14), username + "@coffemail.test",
                "+502 5555-1234", "/home/user/.coffemail/images/" + username + ".png", true);
    }

    @Test
    @DisplayName("un repositorio sin archivo está vacío y no falla al leer")
    void emptyWhenFileMissing() throws IOException {
        assertTrue(repository.isEmpty());
        assertEquals(List.of(), repository.findAll());
        assertEquals(Optional.empty(), repository.findByUsername("esaban"));
    }

    @Test
    @DisplayName("save crea el directorio, el encabezado y el registro")
    void savesFirstUserWithHeader() throws IOException {
        repository.save(sampleUser("esaban"));

        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        assertEquals(2, lines.size());
        assertEquals(UserRepository.HEADER, lines.get(0));
        assertTrue(lines.get(1).startsWith("esaban|"));
    }

    @Test
    @DisplayName("el encabezado se escribe una sola vez aunque se guarden varios usuarios")
    void writesHeaderOnlyOnce() throws IOException {
        repository.save(sampleUser("uno"));
        repository.save(sampleUser("dos"));
        repository.save(sampleUser("tres"));

        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        assertEquals(4, lines.size());
        assertEquals(1, lines.stream().filter(UserRepository.HEADER::equals).count());
        assertEquals(3, repository.findAll().size());
    }

    @Test
    @DisplayName("si el archivo existe pero está vacío, igual se escribe el encabezado")
    void writesHeaderWhenFileExistsButIsEmpty() throws IOException {
        Files.createDirectories(tempDir);
        Files.createFile(file);

        repository.save(sampleUser("esaban"));

        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        assertEquals(UserRepository.HEADER, lines.get(0));
        assertEquals(1, repository.findAll().size());
    }

    @Test
    @DisplayName("los datos sobreviven al viaje de ida y vuelta")
    void roundTripsAllFields() throws IOException {
        User saved = sampleUser("esaban");
        repository.save(saved);

        User read = repository.findByUsername("esaban").orElseThrow();
        assertEquals(saved.getUsername(), read.getUsername());
        assertEquals(saved.getName(), read.getName());
        assertEquals(saved.getLastName(), read.getLastName());
        assertEquals(saved.getPasswordHash(), read.getPasswordHash());
        assertEquals(saved.getRole(), read.getRole());
        assertEquals(saved.getBirthDate(), read.getBirthDate());
        assertEquals(saved.getEmail(), read.getEmail());
        assertEquals(saved.getPhone(), read.getPhone());
        assertEquals(saved.getPhotoPath(), read.getPhotoPath());
        assertTrue(read.isActive());
    }

    @Test
    @DisplayName("los acentos se guardan en UTF-8, no en el charset del sistema")
    void preservesAccentsRegardlessOfPlatformEncoding() throws IOException {
        User user = sampleUser("esaban");
        user.setLastName("Sabán Muñoz");
        repository.save(user);

        assertTrue(Files.readString(file, StandardCharsets.UTF_8).contains("Sabán Muñoz"));
        assertEquals("Sabán Muñoz", repository.findByUsername("esaban").orElseThrow().getLastName());
    }

    @Test
    @DisplayName("un campo con '|' o con saltos de línea no corrompe el archivo")
    void survivesDelimiterInsideAField() throws IOException {
        User user = sampleUser("esaban");
        user.setLastName("Sabán|Muñoz\nsegunda línea");
        repository.save(user);

        assertEquals(2, Files.readAllLines(file, StandardCharsets.UTF_8).size());
        assertEquals("Sabán|Muñoz\nsegunda línea",
                repository.findByUsername("esaban").orElseThrow().getLastName());
    }

    @Test
    @DisplayName("la búsqueda por usuario ignora mayúsculas y espacios")
    void findByUsernameIsCaseInsensitive() throws IOException {
        repository.save(sampleUser("esaban"));

        assertTrue(repository.findByUsername("ESABAN").isPresent());
        assertTrue(repository.findByUsername("  esaban  ").isPresent());
        assertFalse(repository.findByUsername("otro").isPresent());
    }

    @Test
    @DisplayName("una línea corrupta se omite sin tumbar la lectura completa")
    void skipsMalformedLines() throws IOException {
        repository.save(sampleUser("esaban"));
        Files.writeString(file, "basura|con|pocos|campos\n",
                StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
        repository.save(sampleUser("otro"));

        assertEquals(2, repository.findAll().size());
    }

    @Test
    @DisplayName("se leen registros antiguos con el rol guardado como booleano")
    void readsLegacyBooleanRole() throws IOException {
        Files.createDirectories(tempDir);
        Files.writeString(file, UserRepository.HEADER + "\n"
                + "admin|A|B|hash|true|1998-03-14|a@b.c|55551234|/x.png|true\n"
                + "normal|A|B|hash|false|1998-03-14|a@b.c|55551234|/x.png|true\n",
                StandardCharsets.UTF_8);

        assertEquals(Role.ADMIN, repository.findByUsername("admin").orElseThrow().getRole());
        assertEquals(Role.USER, repository.findByUsername("normal").orElseThrow().getRole());
    }
}
