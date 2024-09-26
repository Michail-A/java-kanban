package modelTest;

import model.Status;
import model.SubTask;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SubTaskTest {

    static SubTask subTask1;
    static SubTask subTask2;

    @BeforeAll
    static void beforeAll() {
        subTask1 = new SubTask("test", "test", 1, Status.NEW, 1);
        subTask2 = new SubTask("test1", "test1", 1, Status.DONE, 11);
    }

    @Test
    public void shouldBeEqualSubTaskSameId() {
        assertTrue(subTask1.equals(subTask2));
    }

}