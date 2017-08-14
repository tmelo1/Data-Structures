import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public abstract class MapTestBase {

    private Map<Integer, Integer> map;

    protected abstract Map<Integer, Integer> createMap();

    @Before
    public void setupTreeTests() {
        map = this.createMap();
    }

    @Test
    public void testInit() {
        assertEquals(0, map.size());
        int c = 0;
        for (Integer i: map) {
            c++;
        }
        assertEquals(0, c);
    }

    @Test(expected=IllegalArgumentException.class)
    public void puttingNullKeys() {
        map.put(null, 1);
    }

    @Test(expected=IllegalArgumentException.class)
    public void gettingNullKeys() {
        map.get(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void insertingNullKey() {
        map.insert(null, 2);
    }

    @Test(expected=IllegalArgumentException.class)
    public void removingNullKey() {
        map.remove(null);
    }

    @Test
    public void insertWorks() {
        map.insert(1, 2);
        map.insert(3, 4);
        map.insert(5, 6);
        assertEquals("{1: 2, 3: 4, 5: 6}", map.toString());
    }

    @Test
    public void removeWorks() {
        map.insert(1, 2);
        map.insert(3, 4);
        map.insert(5, 6);
        assertEquals("{1: 2, 3: 4, 5: 6}", map.toString());
        int v = map.remove(1);
        assertEquals("{3: 4, 5: 6}", map.toString());
        assertEquals(2, v);
    }

    @Test
    public void iteratorWorks() {
        map.insert(1, 2);
        map.insert(3, 4);
        map.insert(5, 6);
        String test = "";
        for (Integer i: map) {
            test += map.get(i) + " ";
        }
        assertEquals("2 4 6 ", test);
    }

    @Test
    public void putWorks() {
        map.insert(1, 2);
        assertEquals(2, (int) map.get(1));
        map.put(1, 5);
        assertEquals(5, (int) map.get(1));
    }

    @Test
    public void hasWorks() {
        map.insert(1, 2);
        map.insert(3, 4);
        map.insert(7, 8);
        assertTrue(map.has(1));
        assertTrue(map.has(3));
        assertFalse(map.has(5));
    }

    @Test
    public void sizeWorks() {
        map.insert(1, 2);
        map.insert(3, 4);
        map.insert(5, 6);
        int c = 0;
        for (Integer i: map) {
            c++;
        }
        assertEquals(c, map.size());
    }

    @Test(expected=IllegalArgumentException.class)
    public void cantInsertDuplicates() {
        map.insert(1, 2);
        map.insert(1, 3);
    }

    @Test
    public void getWorks() {
        map.insert(5, 9);
        assertEquals(9, (int)map.get(5));
    }

    @Test(expected=IllegalArgumentException.class)
    public void cantGetWhatsNotThere() {
        map.get(9);
    }

    @Test(expected=IllegalArgumentException.class)
    public void cantPutWhatIsntThereEither() {
        map.put(8, 9);
    }
}
