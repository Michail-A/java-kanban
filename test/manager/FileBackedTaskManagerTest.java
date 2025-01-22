package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    @BeforeEach
    void setUp() throws IOException {
        Path data = Files.createTempFile("tast_data", "csv");
    }

    @Test
    void save() {
    }

    @Test
    void loadFromFile() {
    }
}