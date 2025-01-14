package manager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ManagersTest {

    @Test
    public void testDefaultManegerNotNull() {
        assertNotNull(Managers.getDefault(), "Managers не возвращает по дефолту");
        assertNotNull(Managers.getDefaultHistory(), "Managers не возвращает ХисториПоДефолту");
    }

}
