package model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

class EpicTest {


    static Epic epic1;
    static Epic epic2;

    @BeforeAll
    public static void beforeAll() {
        epic1 = new Epic(1, "Test", "Test", new ArrayList<>());
        epic2 = new Epic(1, "Test2", "Test2", new ArrayList<>());
    }

    @Test
    public void shouldBeEqualEpicSameId() {
        assertTrue(epic1.equals(epic2));
    }
}