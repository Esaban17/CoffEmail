package com.coffemail.storage;

import com.coffemail.models.Role;
import com.coffemail.models.User;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Persistencia de usuarios sobre un archivo de texto delimitado.
 *
 * <p>Cambios respecto de la versión original:
 * <ul>
 *   <li>UTF-8 explícito, en vez del charset por defecto del sistema, que
 *       corrompía los acentos al mover el archivo entre máquinas.</li>
 *   <li>Campos escapados mediante {@link DelimitedRecord}.</li>
 *   <li>Fechas en ISO-8601 ({@code yyyy-MM-dd}) en lugar de
 *       {@code Date.toString()}, que dependía del locale y no podía volver a
 *       leerse con el formato de entrada.</li>
 *   <li>Escritura con {@code try-with-resources}, así que el descriptor de
 *       archivo se cierra aunque la escritura falle.</li>
 *   <li>El encabezado se escribe también cuando el archivo existe pero está
 *       vacío, caso en el que antes se perdía.</li>
 * </ul>
 *
 * @author Estuardo Sabán
 */
public class UserRepository {

    /** Encabezado del archivo, escrito una sola vez. */
    static final String HEADER = "usuario|nombre|apellido|passwordHash|rol|fechaNacimiento"
            + "|correo|telefono|pathFotografia|estatus";

    private static final int FIELD_COUNT = 10;

    private final Path file;

    /** Crea un repositorio sobre la ubicación estándar de la aplicación. */
    public UserRepository() {
        this(AppPaths.usersFile());
    }

    /**
     * Crea un repositorio sobre un archivo concreto.
     *
     * @param file ruta del archivo de usuarios
     */
    public UserRepository(Path file) {
        this.file = file;
    }

    /** @return {@code true} si todavía no hay ningún usuario registrado */
    public boolean isEmpty() throws IOException {
        return findAll().isEmpty();
    }

    /**
     * Busca un usuario por nombre de usuario, sin distinguir mayúsculas.
     *
     * @param username nombre a buscar
     * @return el usuario, si existe
     */
    public Optional<User> findByUsername(String username) throws IOException {
        if (username == null) {
            return Optional.empty();
        }
        String target = username.trim().toLowerCase(Locale.ROOT);
        return findAll().stream()
                .filter(u -> u.getUsername() != null
                        && u.getUsername().trim().toLowerCase(Locale.ROOT).equals(target))
                .findFirst();
    }

    /**
     * Lee todos los usuarios.
     *
     * <p>Las líneas malformadas se omiten en vez de tumbar la lectura completa:
     * un registro corrupto no debería impedir el acceso al resto.
     *
     * @return lista de usuarios, vacía si el archivo aún no existe
     */
    public List<User> findAll() throws IOException {
        if (!Files.exists(file)) {
            return List.of();
        }
        List<User> users = new ArrayList<>();
        for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
            if (line.isBlank() || line.equals(HEADER)) {
                continue;
            }
            parse(line).ifPresent(users::add);
        }
        return users;
    }

    /**
     * Agrega un usuario al final del archivo, creando el directorio y el
     * encabezado si hace falta.
     *
     * @param user usuario a guardar; debe traer el hash ya calculado
     */
    public void save(User user) throws IOException {
        Path parent = file.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        boolean needsHeader = !Files.exists(file) || Files.size(file) == 0L;
        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            if (needsHeader) {
                writer.write(HEADER);
                writer.newLine();
            }
            writer.write(serialize(user));
            writer.newLine();
        }
    }

    static String serialize(User user) {
        return DelimitedRecord.join(List.of(
                nullToEmpty(user.getUsername()),
                nullToEmpty(user.getName()),
                nullToEmpty(user.getLastName()),
                nullToEmpty(user.getPasswordHash()),
                user.getRole() == null ? Role.USER.name() : user.getRole().name(),
                user.getBirthDate() == null ? "" : user.getBirthDate().toString(),
                nullToEmpty(user.getEmail()),
                nullToEmpty(user.getPhone()),
                nullToEmpty(user.getPhotoPath()),
                Boolean.toString(user.isActive())));
    }

    static Optional<User> parse(String line) {
        List<String> fields = DelimitedRecord.split(line);
        if (fields.size() != FIELD_COUNT) {
            return Optional.empty();
        }
        User user = new User();
        user.setUsername(fields.get(0));
        user.setName(fields.get(1));
        user.setLastName(fields.get(2));
        user.setPasswordHash(fields.get(3));
        user.setRole(Role.fromStorage(fields.get(4)));
        user.setBirthDate(parseDate(fields.get(5)));
        user.setEmail(fields.get(6));
        user.setPhone(fields.get(7));
        user.setPhotoPath(fields.get(8));
        user.setActive(Boolean.parseBoolean(fields.get(9)));
        return Optional.of(user);
    }

    private static LocalDate parseDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(value.trim());
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
