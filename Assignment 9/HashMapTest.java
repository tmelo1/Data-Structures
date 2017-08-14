public final class HashMapTest extends MapTestBase {

    private Map<Integer, Integer> map = this.createMap();

    @Override
    public Map<Integer, Integer> createMap() {
        return new HashMap<>();
    }
}
