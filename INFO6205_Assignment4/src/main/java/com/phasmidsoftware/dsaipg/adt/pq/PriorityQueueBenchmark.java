package com.phasmidsoftware.dsaipg.adt.pq;

import com.phasmidsoftware.dsaipg.adt.pq.PriorityQueue;
import com.phasmidsoftware.dsaipg.util.Benchmark_Timer;
import java.util.Random;
import java.util.function.Supplier;

public class PriorityQueueBenchmark {

    private static final int M = 4095;
    private static final int INSERT_COUNT = 16000;
    private static final int REMOVE_COUNT = 4000;

    public static void main(String[] args) {
        Random random = new Random();

        testPriorityQueue("Binary Heap", () -> new PriorityQueue<>(M, true, Integer::compare, false, 2));
        testPriorityQueue("Binary Heap + Floyd", () -> new PriorityQueue<>(M, true, Integer::compare, true, 2));
        testPriorityQueue("4-ary Heap", () -> new PriorityQueue<>(M, true, Integer::compare, false, 4));
        testPriorityQueue("4-ary Heap + Floyd", () -> new PriorityQueue<>(M, true, Integer::compare, true, 4));

        testFibonacciHeap();

    }

    private static void testPriorityQueue(String name, Supplier<PriorityQueue<Integer>> supplier) {
        PriorityQueue<Integer> pq = supplier.get();
        Random random = new Random();

        Benchmark_Timer<Integer> insertBenchmark = new Benchmark_Timer<>(name + " Insert", pq::give);
        double insertTime = insertBenchmark.runFromSupplier(() -> random.nextInt(100000), INSERT_COUNT);
        System.out.println(name + " Insert Time: " + insertTime + " ms");

        Benchmark_Timer<Integer> removeBenchmark = new Benchmark_Timer<>(name + " Remove", k -> {
            try {
                pq.take();
            } catch (PQException e) {
                System.out.println("Error: " + e.getMessage());
            }
        });

        double removeTime = removeBenchmark.runFromSupplier(() -> 0, REMOVE_COUNT);
        System.out.println(name + " Remove Time: " + removeTime + " ms");

        System.out.println(name + " Highest priority spilled element: " + pq.getHighestPrioritySpilled());
    }

    private static void testFibonacciHeap() {
        FibonacciHeap<Integer> pq = new FibonacciHeap<>(Integer::compare);

        Benchmark_Timer<Integer> insertBenchmark = new Benchmark_Timer<>("Fibonacci Heap Insert", pq::insert);
        double insertTime = insertBenchmark.runFromSupplier(() -> new Random().nextInt(), INSERT_COUNT);
        System.out.println("Fibonacci Heap Insert Time: " + insertTime + " ms");

        Benchmark_Timer<Integer> removeBenchmark = new Benchmark_Timer<>("Fibonacci Heap Remove", k -> pq.extractMin());
        double removeTime = removeBenchmark.runFromSupplier(() -> 0, REMOVE_COUNT);
        System.out.println("Fibonacci Heap Remove Time: " + removeTime + " ms");
    }

}
