import com.github.phf.jb.Bench;
import com.github.phf.jb.Bee;

import java.util.Random;

public final class HashMapBench {

    private static final int SIZE = 1000;
    private static final Random RAND = new Random();

    private HashMapBench() {}

    private static void insertLinear(Map<String, Integer> t) {
        for (int i = 0; i < SIZE; i++) {
            t.insert(Integer.toString(i), RAND.nextInt(SIZE));
        }
    }

    private static void insertRandom(Map<String, Integer> t) {
        for (int i = 0; i < SIZE; i++) {
            try {
                t.insert(Integer.toString(RAND.nextInt(SIZE)), i);
            } catch (IllegalArgumentException e) {
                continue;
            }
        }
    }

    private static void randomRemove(Map<String, Integer> t) {
        for (int i = 0; i < SIZE; i++) {
            try {
                t.remove(Integer.toString(RAND.nextInt(SIZE)));
            } catch (IllegalArgumentException e) {
                continue;
            }
        }
    }

    private static void removeLinear(Map<String, Integer> t) {
        for (int i = 0; i > 0; i++) {
            t.remove(Integer.toString(i));
        }
    }

    private static void randomSearch(Map<String, Integer> t) {
        for (int i = 0; i < SIZE; i++) {
            try {
                boolean h = t.has(Integer.toString(RAND.nextInt(SIZE)));
            } catch (IllegalArgumentException e) {
                continue;
            }
        }
    }

    private static void linearSearch(Map<String, Integer> t) {
        for (int i = 0; i < SIZE; i++) {
            boolean h = t.has(Integer.toString(i));
        }
    }

    private static void mixedSequence(Map<String, Integer> t) {
        for (int i = 0; i < SIZE; i++) {
            if (RAND.nextInt(SIZE * 2) < SIZE) {
                try {
                    t.remove(Integer.toString(RAND.nextInt(SIZE)));
                } catch (IllegalArgumentException e) {
                    continue;
                }
            } else {
                try {
                    t.insert(Integer.toString(RAND.nextInt(SIZE)), i);
                } catch (IllegalArgumentException e) {
                    continue;
                }
            }
        }
    }

    @Bench
    public static void randomSequence(Bee b) {
        for (int c = 0; c < b.reps(); c++) {
            b.stop();
            Map<String, Integer> t = new HashMap<>();
            b.start();
            mixedSequence(t);
        }
    }

    @Bench
    public static void insertLinear(Bee b) {
        for (int c = 0; c < b.reps(); c++) {
            b.stop();
            Map<String, Integer> t = new HashMap<>();
            b.start();
            insertLinear(t);
        }
    }

    @Bench
    public static void insertRandom(Bee b) {
        for (int c = 0; c < b.reps(); c++) {
            b.stop();
            Map<String, Integer> t = new HashMap<>();
            b.start();
            insertRandom(t);
        }
    }

    @Bench
    public static void randomSearch(Bee b) {
        for (int c = 0; c < b.reps(); c++) {
            b.stop();
            Map<String, Integer> t = new HashMap<>();
            insertRandom(t);
            b.start();
            randomSearch(t);
        }
    }

    @Bench
    public static void linearSearch(Bee b) {
        for (int c = 0; c < b.reps(); c++) {
            b.stop();
            Map<String, Integer> t = new HashMap<>();
            insertLinear(t);
            b.start();
            linearSearch(t);
        }
    }

    @Bench
    public static void removeRemove(Bee b) {
        for (int c = 0; c < b.reps(); c++) {
            b.stop();
            Map<String, Integer> t = new HashMap<>();
            insertRandom(t);
            b.start();
            randomRemove(t);
        }
    }
}
