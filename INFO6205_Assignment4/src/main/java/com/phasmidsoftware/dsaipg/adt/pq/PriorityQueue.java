/*
 * Copyright (c) 2024. Robin Hillyard
 */
package com.phasmidsoftware.dsaipg.adt.pq;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

/**
 * Modified Priority Queue Data Structure which supports binary and d-ary heaps.
 *
 * @param <K> The type of elements stored in the priority queue.
 */
public class PriorityQueue<K> implements Iterable<K> {

    private final boolean max;
    private final int first;
    private final Comparator<K> comparator;
    private final K[] binHeap;
    private int last;
    private final boolean floyd;
    private final int d;

    private K highestPrioritySpilled = null; // 记录最高优先级的溢出元素

    /**
     * Primary constructor with full customization options.
     */
    public PriorityQueue(boolean max, Object[] binHeap, int first, int last, Comparator<K> comparator, boolean floyd, int d) {
        this.max = max;
        this.first = first;
        this.comparator = comparator;
        this.last = last;
        //noinspection unchecked
        this.binHeap = (K[]) binHeap;
        this.floyd = floyd;
        this.d = d;
    }

    public PriorityQueue(int n, boolean max, Comparator<K> comparator, boolean floyd, int d) {
        this(max, new Object[n + 1], 1, 0, comparator, floyd, d);
    }

    public PriorityQueue(int n, boolean max, Comparator<K> comparator, boolean floyd) {
        this(n, max, comparator, floyd, 2);
    }

    public PriorityQueue(int n, boolean max, Comparator<K> comparator) {
        this(n, max, comparator, false, 2);
    }

    public PriorityQueue(int n, Comparator<K> comparator) {
        this(n, true, comparator, true, 2);
    }

    public boolean isEmpty() {
        return last == 0;
    }

    public int size() {
        return last;
    }

    /**
     * Insert an element into the priority queue.
     */
    public void give(K key) {
        if (last == binHeap.length - first) {

            K spilled = binHeap[first];


            if (highestPrioritySpilled == null || comparator.compare(spilled, highestPrioritySpilled) > 0) {
                highestPrioritySpilled = spilled;
            }
            last--;
        }
        binHeap[++last + first - 1] = key;
        swimUp(last + first - 1);
    }

    /**

     */
    public K getHighestPrioritySpilled() {
        return highestPrioritySpilled;
    }

    /**
     * Remove and return the root element.
     */
    public K take() throws PQException {
        if (isEmpty()) throw new PQException("Priority queue is empty");
        return floyd ? doTake(this::snake) : doTake(this::sink);
    }

    private K doTake(Consumer<Integer> f) {
        K result = binHeap[first];
        swap(first, last-- + first - 1);
        f.accept(first);
        binHeap[last + first] = null;
        return result;
    }

    private void sink(int k) {
        doHeapify(k, (a, b) -> !unordered(a, b));
    }

    private void snake(int k) {
        swimUp(doHeapify(k, (a, b) -> !unordered(a, b)));
    }

    private void swimUp(int k) {
        int i = k;
        while (i > first && unordered(parent(i), i)) {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    private boolean unordered(int i, int j) {
        return (comparator.compare(binHeap[i], binHeap[j]) > 0) ^ max;
    }

    private int doHeapify(int k, BiPredicate<Integer, Integer> p) {
        int i = k;
        while (firstChild(i) <= last + first - 1) {
            int bestChild = firstChild(i);
            for (int j = 1; j < d; j++) { // Find best child among d children
                int nextChild = bestChild + j;
                if (nextChild <= last + first - 1 && unordered(bestChild, nextChild))
                    bestChild = nextChild;
            }
            if (p.test(i, bestChild)) break;
            swap(i, bestChild);
            i = bestChild;
        }
        return i;
    }

    private void swap(int i, int j) {
        K tmp = binHeap[i];
        binHeap[i] = binHeap[j];
        binHeap[j] = tmp;
    }

    private int parent(int k) {
        return (k - first) / d + first;
    }

    private int firstChild(int k) {
        return d * (k - first) + first + 1;
    }

    @Override
    public Iterator<K> iterator() {
        return Arrays.stream(binHeap, first, last + first).iterator();
    }

    public static void main(String[] args) {
        try {
            PriorityQueue<Integer> binaryHeap = new PriorityQueue<>(10, true, Integer::compare, false, 2);
            binaryHeap.give(3);
            binaryHeap.give(5);
            binaryHeap.give(1);
            System.out.println("Binary Heap root: " + binaryHeap.take());

            System.out.println("Highest priority spilled element: " + binaryHeap.getHighestPrioritySpilled());

        } catch (PQException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
