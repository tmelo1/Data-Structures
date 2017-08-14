import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

/**
 * A hashmap implementation with an array that uses linear probing.
 * @author tmelo1
 *
 * @param <K> generic key type
 * @param <V> generic value type
 */
public class HashMap<K, V> implements Map<K, V> {

    /**
    * Nested Entry class.
    * This one keeps track of where it gets inserted to make removal easy.
    * @param <K> key type
    * @param <V> value type
    */
    private static class Entry<K, V> {
        K key;
        V value;
        int actualIndex;

        Entry(K k, V v) {
            this.key = k;
            this.value = v;
            this.actualIndex = -1;
        }
    }


    private class HashMapIterator implements Iterator<K> {
        Iterator<K> it = HashMap.this.keys().iterator();

        public boolean hasNext() {
            return this.it.hasNext();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public K next() {
            return this.it.next();
        }
    }

    /** The array to represent our hashmap. */
    private Entry<K, V>[] hmap;

    /** The number of elements present. */
    private int size;

    /** The initial size. */
    private final int initialSize = 8;

    /** The maximum load factor. Rehashes once this is reached. */
    private final double maxLoadFactor = 0.4;

    /** A list of keys to make iteration simple and more time efficient. */
    private List<K> keys;

    /** Constructor. */
    HashMap() {
        this.hmap = new Entry[this.initialSize];
        this.keys = new ArrayList<>();
    }

    /** Return the list of keys.
     * @return keys the list of keys
     */
    public List<K> keys() {
        return this.keys;
    }

    /** A simple hashing method.
     * @param k the key to hash
     * @return the int representing the hash value
     */
    private int hash(K k) {
        return this.abs(k.hashCode() % this.hmap.length);
    }

    /**
     * Find an appropriate table spot.
     * @param i the starting index
     * @return index of the next open spot, -1 if no open spot exists
     */
    private int probe(int i) {
        int index = (i + 1) % this.hmap.length;
        while (index != i) {
            if (this.hmap[index] != null) {
                index = (index + 1) % this.hmap.length;
            } else {
                return index;
            }
        }
        return -1;
    }

    /**
     * Find an entry with the key if it exists
     * @param k the key to match
     * @return the entry if found, null otherwise
     */
    private Entry<K, V> find(K k) {
        if (k == null) {
            throw new IllegalArgumentException("null key not allowed");
        }
        if (this.empty()) {
            return null;
        }
        int code = this.hash(k);
        if (this.hmap[code] != null) {
            if (this.hmap[code].key.equals(k)) {
                return this.hmap[code];
            }
        }
        for (int i = 0; i < this.hmap.length; i++) {
            if (this.hmap[i] == null) {
                continue;
            }
            if (this.hmap[i].key.equals(k)) {
                return this.hmap[i];
            }
        }
        return null;
    }

    /**
     * Calls the find helper method. Essentially does the same as above.
     * @param k the key to match
     * @return the entry if found, throws an exception otherwise
     */
    private Entry<K, V> findForSure(K k) {
        Entry<K, V> e = this.find(k);
        if (e == null) {
            throw new IllegalArgumentException("key " + k + " not found");
        }
        return e;
    }

    /**
     * Returns the value associated with the key.
     * @param k the key to find
     * @return the value associated with it if it exists
     */
    public V get(K k) {
        if (k == null) {
            throw new IllegalArgumentException("null key not allowed");
        }
        Entry<K, V> n = this.findForSure(k);
        return n.value;
    }

    /**
     * Determine whether the given key is in the map.
     * @param k the key to find
     * @return true if found, false if key is null or not found
     */
    public boolean has(K k) {
        if (k == null) {
            return false;
        }
        return this.find(k) != null;
    }

    /**
     * Insert a key value pair into the map.
     * @param k the key to insert
     * @param v the value to insert
     */
    public void insert(K k, V v) {
        if (k == null) {
            throw new IllegalArgumentException("null key not allowed");
        }
        if (this.has(k)) {
            throw new IllegalArgumentException("duplicate key");
        }
        if (this.loadFactor() >= this.maxLoadFactor) {
            this.rehash();
        }
        Entry<K, V> n = new Entry<K, V>(k, v);
        this.size++;
        this.insert(n);
        this.keys.add(k);
    }

    /**
     * An insert helper method.
     * Linearly probes if the slot associated with hash value is taken.
     * We rehash if insertion somehow fails.
     * @param e the entry to insert
     *
     */
    private void insert(Entry<K, V> e) {
        int code = this.hash(e.key);
        int index = 0;
        if (this.hmap[code] != null) {
            index = this.probe(code);
        } else {
            this.hmap[code] = e;
            this.hmap[code].actualIndex = code;
            return;
        }
        if (index != -1) {
            this.hmap[index] = e;
            this.hmap[index].actualIndex = index;
        } else {
            this.rehash();
        }
    }

    /**
     * Find the current load of the table.
     * @return the load factor
     */
    public double loadFactor() {
        return this.size / this.hmap.length;
    }

    /**
     * Just an absolute value function. Can't have negative indices can we.
     * @param i the int to absolute value
     * @return the positive value
     */
    private int abs(int i) {
        if (i < 0) {
            return -i;
        }
        return i;
    }

    /**
     * Rehash to a new table of larger size.
     * New hash function based on the size of the new table.
     */
    private void rehash() {
        int newSize = this.hmap.length * 2;
        Entry<K, V>[] newTable = new Entry[newSize];
        Entry<K, V> temp;
        int index;
        int count = 0;
        for (int i = 0; i < this.hmap.length; i++) {
            temp = this.hmap[i];
            if (this.hmap[i] == null) {
                continue;
            }
            index = this.abs(temp.key.hashCode() % newSize);
            if (newTable[index] == null) {
                newTable[index] = temp;
                newTable[index].actualIndex = index;
                continue;
            } else {
                int c = (index + 1) % newSize;
                while (count != newSize) {
                    if (newTable[c] == null) {
                        newTable[c] = temp;
                        newTable[c].actualIndex = c;
                        break;
                    } else {
                        c = (c + 1) % newSize;
                        count++;
                    }
                }
            }
        }
        this.hmap = newTable;
    }

    /**
     * Remove an entry with the given key from the table, if it exists.
     * @param k the key for the entry we want to remove
     * @return the value associated with that entry
     */
    public V remove(K k) {
        Entry<K, V> e = this.findForSure(k);
        this.hmap[e.actualIndex] = null;
        V val = e.value;
        this.size--;
        this.keys.remove(k);
        return val;
    }

    /**
     * Replace the value associated with a key with a new one.
     * @param k the key to find
     * @param v the new value
     */
    public void put(K k, V v) {
        Entry<K, V> e = this.findForSure(k);
        e.value = v;
    }

    /**
     * Returns the number of elements in the table.
     * @return size the number of elements
     */
    public int size() {
        return this.size;
    }

    @Override
    public Iterator<K> iterator() {
        return new HashMapIterator();
    }

    /**
     * Checks if the table is empty.
     * @return true if no elements exist, false otherwise
     */
    public boolean empty() {
        return this.size == 0;
    }

    /**
     * Return a string representation of entries in the table.
     * @return the string to represent the table
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("{");
        for (int i = 0; i < this.keys.size(); i++) {
            s.append(this.keys.get(i) + ": " + this.get(this.keys.get(i)));
            if (i < this.keys.size() - 1) {
                s.append(", ");
            }
        }
        s.append("}");
        return s.toString();
    }
}
