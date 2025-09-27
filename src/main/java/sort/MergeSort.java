package sort;

import metrics.Metrics;

public class MergeSort {
    private static final int CUTOFF = 16;

    public static void sort(int[] a, Metrics m) {
        int[] buf = new int[a.length];
        m.addAlloc(a.length);
        sort(a, 0, a.length, buf, m);
    }

    private static void sort(int[] a, int lo, int hi, int[] buf, Metrics m) {
        int n = hi - lo;
        if (n <= 1) return;
        if (n <= CUTOFF) { insertion(a, lo, hi, m); return; }
        int mid = lo + (n >> 1);
        m.enter(); sort(a, lo, mid, buf, m); m.leave();
        m.enter(); sort(a, mid, hi, buf, m); m.leave();
        if (a[mid-1] <= a[mid]) return;
        merge(a, lo, mid, hi, buf, m);
    }

    private static void insertion(int[] a, int lo, int hi, Metrics m) {
        for (int i = lo + 1; i < hi; i++) {
            int x = a[i], j = i - 1;
            while (j >= lo && m.cmpInt(a[j], x) > 0) { a[j+1] = a[j]; j--; }
            a[j+1] = x;
        }
    }

    private static void merge(int[] a, int lo, int mid, int hi, int[] buf, Metrics m) {
        int i = lo, j = mid, k = 0;
        while (i < mid && j < hi) {
            if (m.cmpInt(a[i], a[j]) <= 0) buf[k++] = a[i++]; else buf[k++] = a[j++];
        }
        while (i < mid) buf[k++] = a[i++];
        while (j < hi) buf[k++] = a[j++];
        System.arraycopy(buf, 0, a, lo, k);
    }
}
