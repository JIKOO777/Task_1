package metrics;

public class Metrics {
    public long comparisons;
    public long allocations;
    public long nanos;
    public int  maxDepth;
    private int curDepth;

    public void enter() { curDepth++; if (curDepth > maxDepth) maxDepth = curDepth; }
    public void leave() { curDepth--; }
    public int cmpInt(int a, int b) { comparisons++; return Integer.compare(a, b); }
    public void addAlloc(long cnt) { allocations += cnt; }

    public void time(Runnable r) {
        long t0 = System.nanoTime();
        r.run();
        nanos = System.nanoTime() - t0;
    }
}
