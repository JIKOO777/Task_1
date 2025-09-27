package sort;

import metrics.Metrics;
import util.ArrayUtil;
import java.util.Random;

public class QuickSort {
    private static final Random RND = new Random(1);

    public static void sort(int[] a, Metrics m) { qsort(a, 0, a.length - 1, m); }

    private static void qsort(int[] a, int lo, int hi, Metrics m) {
        while (lo < hi) {
            int p = partition(a, lo, hi, m);
            int left  = p - lo;
            int right = hi - p;
            if (left < right) {
                m.enter(); qsort(a, lo, p - 1, m); m.leave();
                lo = p + 1;
            } else {
                m.enter(); qsort(a, p + 1, hi, m); m.leave();
                hi = p - 1;
            }
        }
    }

    private static int partition(int[] a, int lo, int hi, Metrics m) {
        int pivotIdx = lo + RND.nextInt(hi - lo + 1);
        ArrayUtil.swap(a, pivotIdx, hi);
        int pivot = a[hi], i = lo;
        for (int j = lo; j < hi; j++) if (m.cmpInt(a[j], pivot) <= 0) { ArrayUtil.swap(a, i, j); i++; }
        ArrayUtil.swap(a, i, hi);
        return i;
    }
}
