package modelTest;

import model.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskTest {

    static Task task1;
    static Task task2;

    @BeforeAll
    public static void beforeAll() {
        task1 = new Task(1, "test", "test");
        task2 = new Task(1, "test2", "test2");
    }

    @Test
    public void shouldBeEqualTasksSameId() {
        assertTrue(task1.equals(task2));
    }

}