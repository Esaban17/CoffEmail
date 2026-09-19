package com.coffemail.storage;

import java.util.ArrayList;
import java.util.List;

/**
 * Serialización de registros separados por {@code |} con escapado.
 *
 * <p>La versión original concatenaba los campos con {@code "|"} sin escapar
 * nada, así que un apellido como {@code "O|Brien"} —o cualquier salto de línea
 * pegado en un campo— partía el registro en dos y corrompía el archivo entero.
 *
 * <p>Reglas de escapado, aplicadas en este orden al serializar:
 * <ul>
 *   <li>{@code \} &rarr; {@code \\}</li>
 *   <li>{@code |} &rarr; {@code \p}</li>
 *   <li>salto de línea &rarr; {@code \n}</li>
 *   <li>retorno de carro &rarr; {@code \r}</li>
 * </ul>
 *
 * @author Estuardo Sabán
 */
public final class DelimitedRecord {

    /** Separador de campos. */
    public static final char DELIMITER = '|';

    private static final char ESCAPE = '\\';

    private DelimitedRecord() {
        // clase de utilidades
    }

    /**
     * Une los campos en una sola línea, escapando lo necesario.
     *
     * @param fields campos en orden; un {@code null} se serializa como cadena vacía
     * @return la línea resultante, sin salto de línea final
     */
    public static String join(List<String> fields) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.size(); i++) {
            if (i > 0) {
                sb.append(DELIMITER);
            }
            sb.append(escape(fields.get(i)));
        }
        return sb.toString();
    }

    /**
     * Descompone una línea en sus campos, deshaciendo el escapado.
     *
     * @param line línea leída del archivo
     * @return los campos en el mismo orden en que se escribieron
     */
    public static List<String> split(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean escaping = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (escaping) {
                current.append(unescape(c));
                escaping = false;
            } else if (c == ESCAPE) {
                escaping = true;
            } else if (c == DELIMITER) {
                fields.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        if (escaping) {
            // Barra invertida suelta al final: se toma literal en vez de perderla.
            current.append(ESCAPE);
        }
        fields.add(current.toString());
        return fields;
    }

    private static String escape(String field) {
        if (field == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder(field.length());
        for (int i = 0; i < field.length(); i++) {
            char c = field.charAt(i);
            switch (c) {
                case ESCAPE -> sb.append(ESCAPE).append(ESCAPE);
                case DELIMITER -> sb.append(ESCAPE).append('p');
                case '\n' -> sb.append(ESCAPE).append('n');
                case '\r' -> sb.append(ESCAPE).append('r');
                default -> sb.append(c);
            }
        }
        return sb.toString();
    }

    private static char unescape(char escaped) {
        return switch (escaped) {
            case 'p' -> DELIMITER;
            case 'n' -> '\n';
            case 'r' -> '\r';
            default -> escaped;
        };
    }
}
