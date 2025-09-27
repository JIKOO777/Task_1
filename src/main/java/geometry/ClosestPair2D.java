package geometry;

import metrics.Metrics;
import java.util.*;

public class ClosestPair2D {
    public static record Result(Point2D a, Point2D b, double dist) { }

    public static Result solve(Point2D[] pts, Metrics m) {
        Point2D[] byX = pts.clone();
        Arrays.sort(byX, Comparator.comparingDouble(Point2D::x));
        m.addAlloc(pts.length);
        return rec(byX, 0, byX.length, m);
    }

    private static Result rec(Point2D[] byX, int lo, int hi, Metrics m) {
        int n = hi - lo;
        if (n <= 3) return brute(byX, lo, hi);
        int mid = lo + n/2;
        m.enter(); Result L = rec(byX, lo,  mid, m); m.leave();
        m.enter(); Result R = rec(byX, mid, hi,  m); m.leave();
        Result best = L.dist < R.dist ? L : R;
        double d = best.dist, midX = byX[mid].x();

        List<Point2D> strip = new ArrayList<>();
        for (int i = lo; i < hi; i++) if (Math.abs(byX[i].x() - midX) < d) strip.add(byX[i]);
        strip.sort(Comparator.comparingDouble(Point2D::y));

        for (int i = 0; i < strip.size(); i++) {
            for (int j = i + 1; j < strip.size() && (strip.get(j).y() - strip.get(i).y()) < d; j++) {
                double di = Math.hypot(strip.get(i).x() - strip.get(j).x(), strip.get(i).y() - strip.get(j).y());
                if (di < d) { d = di; best = new Result(strip.get(i), strip.get(j), di); }
            }
        }
        return best;
    }

    private static Result brute(Point2D[] a, int lo, int hi) {
        Result best = new Result(a[lo], a[lo+1], Math.hypot(a[lo].x()-a[lo+1].x(), a[lo].y()-a[lo+1].y()));
        for (int i = lo; i < hi; i++)
            for (int j = i+1; j < hi; j++) {
                double d = Math.hypot(a[i].x()-a[j].x(), a[i].y()-a[j].y());
                if (d < best.dist) best = new Result(a[i], a[j], d);
            }
        return best;
    }
}
