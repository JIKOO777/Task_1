package cli;

import metrics.CSVWriter;
import metrics.Metrics;
import sort.MergeSort;
import sort.QuickSort;
import select.DeterministicSelect;
import geometry.ClosestPair2D;
import geometry.Point2D;
import util.ArrayUtil;

import java.io.File;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws Exception {
        String algo  = arg(args, "--algo", "mergesort");
        int minN     = Integer.parseInt(arg(args, "--min", "1000"));
        int maxN     = Integer.parseInt(arg(args, "--max", "100000"));
        int step     = Integer.parseInt(arg(args, "--step", "1000"));
        int trials   = Integer.parseInt(arg(args, "--trials", "5"));
        File outFile = new File(arg(args, "--out", "metrics.csv"));

        try (CSVWriter csv = new CSVWriter(outFile, "algo,n,trial,time_ns,comparisons,allocations,depth,seed")) {
            for (int n = minN; n <= maxN; n += step) {
                for (int t = 0; t < trials; t++) {
                    long seed = 1234L + 31L*n + t;
                    Metrics m = new Metrics();
                    switch (algo) {
                        case "mergesort" -> runMerge(n, seed, m);
                        case "quicksort" -> runQuick(n, seed, m);
                        case "select"    -> runSelect(n, seed, m);
                        case "closest"   -> runClosest(n, seed, m);
                        default -> throw new IllegalArgumentException("Unknown algo: " + algo);
                    }
                    csv.row(algo, n, t, m.nanos, m.comparisons, m.allocations, m.maxDepth, seed);
                }
            }
        }
    }
    private static String arg(String[] a, String key, String def) {
        for (int i = 0; i < a.length - 1; i++) if (a[i].equals(key)) return a[i+1];
        return def;
    }
    private static void runMerge(int n, long seed, Metrics m) {
        int[] arr = ArrayUtil.randomIntArray(n, seed);
        m.time(() -> MergeSort.sort(arr, m));
        if (!ArrayUtil.isSorted(arr)) throw new AssertionError("mergesort failed");
    }
    private static void runQuick(int n, long seed, Metrics m) {
        int[] arr = ArrayUtil.randomIntArray(n, seed);
        m.time(() -> QuickSort.sort(arr, m));
        if (!ArrayUtil.isSorted(arr)) throw new AssertionError("quicksort failed");
    }
    private static void runSelect(int n, long seed, Metrics m) {
        int[] arr = ArrayUtil.randomIntArray(n, seed);
        int k = new Random(seed).nextInt(n);
        final int[] a = arr.clone(); m.addAlloc(n);
        final int[] b = arr.clone(); m.addAlloc(n);
        int kth = DeterministicSelect.select(a, k, m);
        java.util.Arrays.sort(b);
        if (kth != b[k]) throw new AssertionError("select failed");
    }
    private static void runClosest(int n, long seed, Metrics m) {
        Random rnd = new Random(seed);
        var pts = new Point2D[n];
        for (int i = 0; i < n; i++) pts[i] = new Point2D(rnd.nextDouble(), rnd.nextDouble());
        m.addAlloc(n);
        m.time(() -> ClosestPair2D.solve(pts, m));
    }
}
