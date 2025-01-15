package manager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ManagersTest {

    @Test
    public void testDefaultManagerNotNull() {
        assertNotNull(Managers.getDefault(), "Managers не возвращает по дефолту");
    }

    @Test
    public void testDefaultHistoryManagerNotNull() {
        assertNotNull(Managers.getDefaultHistory(), "Managers не возвращает ХисториПоДефолту");
    }

}
