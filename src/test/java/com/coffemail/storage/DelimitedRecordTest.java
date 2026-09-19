package com.coffemail.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DelimitedRecordTest {

    @Test
    @DisplayName("un registro simple va y vuelve sin cambios")
    void roundTripsPlainFields() {
        List<String> fields = List.of("esaban", "Estuardo", "Sabán", "true");
        assertEquals(fields, DelimitedRecord.split(DelimitedRecord.join(fields)));
    }

    @Test
    @DisplayName("un campo con el delimitador no parte el registro")
    void escapesDelimiter() {
        List<String> fields = List.of("usuario", "O|Brien", "x");
        String line = DelimitedRecord.join(fields);
        assertEquals(2, line.chars().filter(c -> c == '|').count(),
                "solo los separadores reales deben quedar como '|'");
        assertEquals(fields, DelimitedRecord.split(line));
    }

    @Test
    @DisplayName("un campo con saltos de línea no rompe el archivo")
    void escapesNewlines() {
        List<String> fields = List.of("usuario", "linea1\nlinea2\r\nlinea3", "x");
        String line = DelimitedRecord.join(fields);
        assertEquals(1, line.lines().count(), "el registro debe caber en una sola línea");
        assertEquals(fields, DelimitedRecord.split(line));
    }

    @Test
    @DisplayName("la barra invertida se escapa a sí misma")
    void escapesBackslash() {
        List<String> fields = List.of("C:\\ruta\\foto.png", "a\\|b", "\\");
        assertEquals(fields, DelimitedRecord.split(DelimitedRecord.join(fields)));
    }

    @Test
    @DisplayName("los campos vacíos y nulos se conservan como vacíos")
    void handlesEmptyFields() {
        String line = DelimitedRecord.join(java.util.Arrays.asList("a", "", null, "d"));
        assertEquals(List.of("a", "", "", "d"), DelimitedRecord.split(line));
    }
}
