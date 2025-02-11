package com.phasmidsoftware.dsaipg.sort.elementary;

import com.phasmidsoftware.dsaipg.sort.Helper;
import com.phasmidsoftware.dsaipg.sort.HelperFactory;
import com.phasmidsoftware.dsaipg.util.Config;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import java.util.Arrays;
import java.util.Random;

public class InsertionSortBenchmark {

    public static void main(String[] args) {
        int[] sizes = {1000, 2000, 4000, 8000, 16000}; // 倍增法选择 n
        for (int n : sizes) {
            System.out.println("\n=== Benchmark for n = " + n + " ===");
            benchmark("Random", generateRandomArray(n));
            benchmark("Ordered", generateOrderedArray(n));
            benchmark("Partially Ordered", generatePartiallyOrderedArray(n));
            benchmark("Reverse Ordered", generateReverseOrderedArray(n));
        }
    }

    /**
     * 运行基准测试，计算排序时间
     *
     * @param type  数组类型
     * @param array 需要排序的数组
     */
    private static void benchmark(String type, Integer[] array) {
        Integer[] copy = Arrays.copyOf(array, array.length);

        long startTime = System.nanoTime();
        insertionSort(copy);
        long endTime = System.nanoTime();

        double elapsedTime = (endTime - startTime) / 1_000_000.0; // 转换为毫秒
        System.out.printf("%s Sort Time: %.3f ms\n", type, elapsedTime);
    }

    /**
     * 插入排序算法
     */
    private static void insertionSort(Integer[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; i++) {
            int key = arr[i];
            int j = i - 1;

            // 向左移动所有比 key 大的元素
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    /**
     * 生成一个随机数组
     */
    private static Integer[] generateRandomArray(int n) {
        Random random = new Random();
        Integer[] array = new Integer[n];
        for (int i = 0; i < n; i++) {
            array[i] = random.nextInt(100000);
        }
        return array;
    }

    /**
     * 生成一个有序数组
     */
    private static Integer[] generateOrderedArray(int n) {
        Integer[] array = new Integer[n];
        for (int i = 0; i < n; i++) {
            array[i] = i;
        }
        return array;
    }

    /**
     * 生成一个部分有序的数组（前半部分有序，后半部分随机）
     */
    private static Integer[] generatePartiallyOrderedArray(int n) {
        Integer[] array = generateOrderedArray(n);
        Random random = new Random();
        for (int i = n / 2; i < n; i++) {
            array[i] = random.nextInt(100000);
        }
        return array;
    }

    /**
     * 生成一个逆序的数组
     */
    private static Integer[] generateReverseOrderedArray(int n) {
        Integer[] array = new Integer[n];
        for (int i = 0; i < n; i++) {
            array[i] = n - i;
        }
        return array;
    }
}
