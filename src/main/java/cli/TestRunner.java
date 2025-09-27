package cli;

import geometry.ClosestPair2D;
import geometry.Point2D;
import metrics.Metrics;
import select.DeterministicSelect;
import sort.MergeSort;
import sort.QuickSort;
import util.ArrayUtil;

import java.util.Arrays;
import java.util.Random;

public class TestRunner {
    public static void main(String[] args) {
        boolean ok = true;
        ok &= test("MergeSort", () -> { int[] a = ArrayUtil.randomIntArray(2000, 42); MergeSort.sort(a, new Metrics()); return ArrayUtil.isSorted(a); });
        ok &= test("QuickSort", () -> {
            int n = 20000; int[] a = ArrayUtil.randomIntArray(n, 7); Metrics m = new Metrics(); QuickSort.sort(a, m);
            int bound = 2 * (31 - Integer.numberOfLeadingZeros(n)) + 10;
            return ArrayUtil.isSorted(a) && m.maxDepth <= bound;
        });
        ok &= test("DeterministicSelect", () -> {
            int[] a = ArrayUtil.randomIntArray(5000, 123);
            for (int k = 0; k < a.length; k += 97) { int[] x = a.clone(), y = a.clone(); int got = DeterministicSelect.select(x, k, new Metrics()); Arrays.sort(y); if (got != y[k]) return false; }
            return true;
        });
        ok &= test("ClosestPair2D", () -> {
            int n = 500; Random rnd = new Random(1); Point2D[] pts = new Point2D[n];
            for (int i = 0; i < n; i++) pts[i] = new Point2D(rnd.nextDouble(), rnd.nextDouble());
            var fast = ClosestPair2D.solve(pts, new Metrics());
            double brute = Double.POSITIVE_INFINITY;
            for (int i = 0; i < n; i++) for (int j = i+1; j < n; j++) { double d = Math.hypot(pts[i].x()-pts[j].x(), pts[i].y()-pts[j].y()); if (d < brute) brute = d; }
            return Math.abs(brute - fast.dist()) < 1e-12;
        });
        System.out.println(ok ? "ALL OK ✅" : "SOME TESTS FAILED ❌");
        if (!ok) System.exit(1);
    }
    private static boolean test(String name, Check c) {
        try { boolean pass = c.run(); System.out.println((pass?"[OK] ":"[FAIL] ")+name); return pass; }
        catch (Throwable t) { System.out.println("[ERROR] "+name+": "+t); return false; }
    }
    @FunctionalInterface interface Check { boolean run(); }
}
