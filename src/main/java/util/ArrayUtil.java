package util;

import java.util.Random;

public class ArrayUtil {
    public static int[] randomIntArray(int n, long seed) {
        Random rnd = new Random(seed);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = rnd.nextInt();
        return a;
    }
    public static boolean isSorted(int[] a) {
        for (int i = 1; i < a.length; i++) if (a[i] < a[i-1]) return false;
        return true;
    }
    public static void swap(int[] a, int i, int j) {
        int t = a[i]; a[i] = a[j]; a[j] = t;
    }
}
