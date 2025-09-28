1. Architecture Notes

Metrics module tracks:

number of comparisons

allocations

recursion depth

running time (nanoseconds)

Depth tracking is implemented via enter() / leave() calls in recursive functions.

Allocations are counted whenever temporary arrays are created (MergeSort buffer, Select copies, Closest Pair strip).

For QuickSort we recurse only into the smaller partition, which guarantees stack depth O(log n).

2. Recurrence Analysis

Insertion Sort
Recurrence: T(n) = T(n-1) + O(1) ≈ Θ(n²).
Fast on very small inputs but quadratic growth dominates for large n.

Merge Sort
Recurrence: T(n) = 2T(n/2) + Θ(n) → Master Theorem, Case 2 → Θ(n log n).
Uses a reusable buffer and switches to InsertionSort for small subarrays.

QuickSort (randomized, smaller-first recursion)
Expected recurrence: T(n) = T(k) + T(n-k-1) + Θ(n) with random pivot.
Expected running time Θ(n log n), recursion depth bounded by O(log n).

Deterministic Select (Median-of-Medians)
Recurrence: T(n) = T(n/5) + T(7n/10) + Θ(n) → Θ(n).
Analysis uses Akra–Bazzi intuition.

Closest Pair (2D D&C)
Recurrence: T(n) = 2T(n/2) + Θ(n) → Θ(n log n).
Strip-check limited to 7–8 neighbors → linear overhead.

3. Experimental Results
Insertion Sort vs Merge Sort

Up to n ≈ 100, Insertion Sort runs faster (lower overhead).

Beyond n ≈ 100, Merge Sort outperforms because of its Θ(n log n) growth.

Depth vs Input Size (QuickSort example)

(Insert the plot generated from CSV showing recursion depth vs n)

4. Constant-Factor Effects

Cache effects: MergeSort benefits from sequential memory access.

Cutoff optimization: Switching to Insertion Sort on small subarrays reduces overhead.

GC effects: For very large n, additional time is visible due to buffer allocations.

5. Summary

Theoretical asymptotics match the experimental data.

Differences only matter for small input sizes, where Insertion Sort is faster.

QuickSort’s recursion depth was effectively limited to ~2·log₂(n).

Deterministic Select matched the k-th order statistic from Arrays.sort in all tests.

Closest Pair runs in O(n log n) and gives the same result as O(n²) brute force.
<img width="744" height="577" alt="Снимок экрана 2025-09-28 154526" src="https://github.com/user-attachments/assets/f175a686-1199-4521-ba00-ea40153a70ce" />

