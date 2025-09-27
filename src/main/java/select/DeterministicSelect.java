package select;

import metrics.Metrics;
import util.ArrayUtil;

public class DeterministicSelect {
    public static int select(int[] a, int k, Metrics m) { return select(a, 0, a.length - 1, k, m); }

    private static int select(int[] a, int lo, int hi, int k, Metrics m) {
        while (true) {
            if (lo == hi) return a[lo];
            int pivot = pivotMoM5(a, lo, hi, m);
            int pos = partitionAround(a, lo, hi, pivot, m);
            int left = pos - lo;
            if (k == left) return a[pos];
            if (k < left) { hi = pos - 1; } else { k -= left + 1; lo = pos + 1; }
        }
    }

    private static int partitionAround(int[] a, int lo, int hi, int pivotVal, Metrics m) {
        int piv = lo;
        for (int i = lo; i <= hi; i++) if (a[i] == pivotVal) { piv = i; break; }
        ArrayUtil.swap(a, piv, hi);
        int i = lo;
        for (int j = lo; j < hi; j++) if (m.cmpInt(a[j], pivotVal) <= 0) { ArrayUtil.swap(a, i, j); i++; }
        ArrayUtil.swap(a, i, hi);
        return i;
    }

    private static int pivotMoM5(int[] a, int lo, int hi, Metrics m) {
        int n = hi - lo + 1;
        if (n <= 5) { insertion(a, lo, hi, m); return a[lo + n/2]; }
        int groups = (n + 4) / 5;
        for (int g = 0; g < groups; g++) {
            int s = lo + g*5, e = Math.min(s + 4, hi);
            insertion(a, s, e, m);
            int med = s + (e - s)/2;
            ArrayUtil.swap(a, lo + g, med);
        }
        return select(a, lo, lo + groups - 1, groups/2, m);
    }

    private static void insertion(int[] a, int lo, int hi, Metrics m) {
        for (int i = lo + 1; i <= hi; i++) {
            int x = a[i], j = i - 1;
            while (j >= lo && m.cmpInt(a[j], x) > 0) { a[j+1] = a[j]; j--; }
            a[j+1] = x;
        }
    }
}
