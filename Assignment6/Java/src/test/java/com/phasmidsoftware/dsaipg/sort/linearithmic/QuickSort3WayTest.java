/*
 * Copyright (c) 2017. Phasmid Software
 */

package com.phasmidsoftware.dsaipg.sort.linearithmic;

import com.phasmidsoftware.dsaipg.sort.generic.Sort;
import com.phasmidsoftware.dsaipg.sort.generic.SortWithHelper;
import com.phasmidsoftware.dsaipg.sort.helper.Helper;
import com.phasmidsoftware.dsaipg.sort.helper.HelperFactory;
import com.phasmidsoftware.dsaipg.sort.helper.InstrumentedComparableHelper;
import com.phasmidsoftware.dsaipg.sort.helper.Instrumenter;
import com.phasmidsoftware.dsaipg.util.PrivateMethodTester;
import com.phasmidsoftware.dsaipg.util.benchmark.SortBenchmark;
import com.phasmidsoftware.dsaipg.util.benchmark.StatPack;
import com.phasmidsoftware.dsaipg.util.config.Config;
import com.phasmidsoftware.dsaipg.util.general.Utilities;
import com.phasmidsoftware.dsaipg.util.logging.LazyLogger;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import static com.phasmidsoftware.dsaipg.sort.helper.Instrument.*;
import static com.phasmidsoftware.dsaipg.util.benchmark.SortBenchmarkHelper.getWords;
import static com.phasmidsoftware.dsaipg.util.config.Config_Benchmark.setupConfig;
import static com.phasmidsoftware.dsaipg.util.general.Utilities.round;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("ALL")
public class QuickSort3WayTest {

    // NOTE this doesn't test quicksort because cutoff is set to the default.
    @Test
    public void testSort() throws Exception {
        Integer[] xs = new Integer[4];
        xs[0] = 3;
        xs[1] = 4;
        xs[2] = 2;
        xs[3] = 1;
        Sort<Integer> s = new QuickSort_3way<>(xs.length, 1, config);
        Integer[] ys = s.sort(xs);
        assertEquals(Integer.valueOf(1), ys[0]);
        assertEquals(Integer.valueOf(2), ys[1]);
        assertEquals(Integer.valueOf(3), ys[2]);
        assertEquals(Integer.valueOf(4), ys[3]);
    }

    @Test
    public void testSortWithInstrumenting0() throws Exception {
        int n = 64;
        final SortWithHelper<Integer> sorter = new QuickSort_3way<>(n, 3L, config);
        final Helper<Integer> helper = sorter.getHelper();
        final Integer[] xs = helper.random(Integer.class, r -> r.nextInt(10));
        final Integer[] sorted = sorter.sort(xs);
        assertTrue(helper.isSorted(sorted));
    }

    @Test
    public void testSortWithInstrumenting1() throws Exception {
        int n = 541; // a prime number
        final SortWithHelper<Integer> sorter = new QuickSort_3way<>(n, 0L, config);
        final Helper<Integer> helper = sorter.getHelper();
        final Integer[] xs = helper.random(Integer.class, r -> r.nextInt(97));
        final Integer[] sorted = sorter.sort(xs);
        assertTrue(helper.isSorted(sorted));
    }

    @Test
    public void testSortWithInstrumenting2() throws Exception {
        int n = 1000;
        final SortWithHelper<Integer> sorter = new QuickSort_3way<>(n, 0L, config);
        final Helper<Integer> helper = sorter.getHelper();
        final Integer[] xs = helper.random(Integer.class, r -> r.nextInt(100));
        final Integer[] sorted = sorter.sort(xs);
        assertTrue(helper.isSorted(sorted));
    }

    @Test
    public void testSortWithInstrumenting3() throws Exception {
        int n = 1000;
        final SortWithHelper<Integer> sorter = new QuickSort_3way<>(n, 0L, config);
        final Helper<Integer> helper = sorter.getHelper();
        final Integer[] xs = helper.random(Integer.class, r -> r.nextInt(1000));
        final Integer[] sorted = sorter.sort(xs);
        assertTrue(helper.isSorted(sorted));
    }

    @Test
    public void testSortWithInstrumenting4() throws Exception {
        int n = 1000;
        final SortWithHelper<Integer> sorter = new QuickSort_3way<>(n, 0L, config);
        final Helper<Integer> helper = sorter.getHelper();
        final Integer[] xs = helper.random(Integer.class, r -> r.nextInt(10000));
        final Integer[] sorted = sorter.sort(xs);
        assertTrue(helper.isSorted(sorted));
    }

    @Test
    public void testSortWithInstrumenting5() throws Exception {
        int n = 1000;
        final SortWithHelper<Integer> sorter = new QuickSort_3way<>(n, 0L, config);
        final Helper<Integer> helper = sorter.getHelper();
        final Integer[] xs = helper.random(Integer.class, r -> r.nextInt(10000));
        final Integer[] sorted = sorter.sort(xs);
        assertTrue(helper.isSorted(sorted));
    }

    @Test
    public void testPartition1() throws Exception {
        String testString = "PBAXWPPVPCPDZY";
        char[] charArray = testString.toCharArray();
        Character[] array = new Character[charArray.length];
        for (int i = 0; i < array.length; i++) array[i] = charArray[i];
        final Config config = setupConfig("true", "false", "0", "1", "", "");
        final Helper<Character> helper = HelperFactory.create("3-way quick sort", array.length, config);
        QuickSort<Character> sorter = new QuickSort_3way<>(helper);
        sorter.init(array.length);
        Partitioner<Character> partitioner = sorter.partitioner;
        List<Partition<Character>> partitions = partitioner.partition(QuickSort.createPartition(array));
        assertEquals(2, partitions.size());
        Partition<Character> p0 = partitions.get(0);
        assertEquals(0, p0.from);
        assertEquals(4, p0.to);
        Partition<Character> p1 = partitions.get(1);
        assertEquals(9, p1.from);
        assertEquals(14, p1.to);
        char[] chars = new char[array.length];
        for (int i = 0; i < chars.length; i++) chars[i] = array[i];
        String partitionedString = new String(chars);
        assertEquals("BADCPPPPPVWZYX", partitionedString);
        sorter.postProcess(new Character[1]);
        final PrivateMethodTester privateMethodTester = new PrivateMethodTester(helper);
        final StatPack statPack = (StatPack) privateMethodTester.invokePrivate("getStatPack");
        final int compares = (int) statPack.getStatistics(COMPARES).mean();
        final int swaps = (int) statPack.getStatistics(SWAPS).mean();
        assertEquals(14, compares);
        assertEquals(9, swaps);
        assertEquals(compares + swaps + 19, (int) statPack.getStatistics(HITS).mean());
    }

    @Test
    public void testPartition2() throws Exception {
        String testString = "SEAYRLFVZQTCMK";
        char[] charArray = testString.toCharArray();
        Character[] array = new Character[charArray.length];
        for (int i = 0; i < array.length; i++) array[i] = charArray[i];
        final Config config = setupConfig("true", "false", "0", "1", "", "");
        final Helper<Character> helper = HelperFactory.create("3-way quick sort", array.length, config);
        QuickSort<Character> sorter = new QuickSort_3way<>(helper);
        sorter.init(array.length);
        Partitioner<Character> partitioner = sorter.partitioner;
        List<Partition<Character>> partitions = partitioner.partition(QuickSort.createPartition(array));
        assertEquals(2, partitions.size());
        Partition<Character> p0 = partitions.get(0);
        assertEquals(0, p0.from);
        assertEquals(4, p0.to);
        Partition<Character> p1 = partitions.get(1);
        assertEquals(5, p1.from);
        assertEquals(14, p1.to);
        char[] chars = new char[array.length];
        for (int i = 0; i < chars.length; i++) chars[i] = array[i];
        String partitionedString = new String(chars);
        assertEquals("EACFKLVZQTRMSY", partitionedString);
        sorter.postProcess(new Character[1]);
        final PrivateMethodTester privateMethodTester = new PrivateMethodTester(helper);
        final StatPack statPack = (StatPack) privateMethodTester.invokePrivate("getStatPack");
        final int compares = (int) statPack.getStatistics(COMPARES).mean();
        final int swaps = (int) statPack.getStatistics(SWAPS).mean();
        assertEquals(14, compares);
        assertEquals(13, swaps);
        assertEquals(compares + swaps + 27, (int) statPack.getStatistics(HITS).mean());
    }

    @Test
    public void testSortA() throws Exception {
        Integer[] xs = new Integer[]{3, 4, 2, 1, 4, 1, 2, 3, 2, 1};
        Sort<Integer> s = new QuickSort_3way<>(xs.length, 1, setupConfig("true", "false", "0", "1", "1", ""));
        Integer[] ys = s.sort(xs);
        assertEquals(Integer.valueOf(1), ys[0]);
        assertEquals(Integer.valueOf(1), ys[1]);
        assertEquals(Integer.valueOf(1), ys[2]);
        assertEquals(Integer.valueOf(2), ys[3]);
        assertEquals(Integer.valueOf(4), ys[9]);
    }

    @Test
    public void testSortDetailed() throws Exception {
        int k = 7;
        int N = (int) Math.pow(2, k);
        // NOTE this depends on the cutoff value for quick sort.
        int levels = k - 2;
        final Config config = setupConfig("true", "true", "0", "1", "", "");
        final Helper<Integer> helper = HelperFactory.create("3-way quick sort", N, config);
        System.out.println(helper);
        Sort<Integer> s = new QuickSort_3way<>(helper);
        s.init(N);
        final Integer[] xs = helper.random(Integer.class, r -> r.nextInt(10000));
        assertEquals(Integer.valueOf(1360), xs[0]);
        helper.preProcess(xs);
        Integer[] ys = s.sort(xs);
        assertTrue(helper.isSorted(ys));
        helper.postProcess(ys);
        final PrivateMethodTester privateMethodTester = new PrivateMethodTester(helper);
        final StatPack statPack = (StatPack) privateMethodTester.invokePrivate("getStatPack");
        System.out.println(statPack);
        final int compares = (int) statPack.getStatistics(COMPARES).mean();
        final int inversions = (int) statPack.getStatistics(Instrumenter.INVERSIONS).mean();
        final int fixes = (int) statPack.getStatistics(FIXES).mean();
        final int swaps = (int) statPack.getStatistics(SWAPS).mean();
        final int hits = (int) statPack.getStatistics(HITS).mean();
        final long worstCompares = round(2.0 * N * Math.log(N));
        System.out.println("compares: " + compares + ", worstCompares: " + worstCompares);
        assertTrue(compares <= worstCompares);
        assertTrue(inversions <= fixes);
        assertEquals(swaps + compares + 1343, hits, 1000);
    }

    //    @Test  CONSIDER re-instituting
    public void testSortHuge() throws Exception {
        int k = 16;
        int N = (int) Math.pow(2, k);
        final Config config = setupConfig("true", "false", "0", "1", "", "");
        final Helper<String> helper = HelperFactory.create("3-way quick sort", N, config);
        String[] words = getWords("eng-uk_web_2002_100K-sentences.txt", SortBenchmark::getLeipzigWords);
        String[] xs = Utilities.fillRandomArray(String.class, new Random(0L), N, r -> words[r.nextInt(words.length)]);
        Sort<String> s = new QuickSort_3way<>(helper);
        s.init(N);
        helper.preProcess(xs);
        String[] ys = s.sort(xs);
        assertTrue(helper.isSorted(ys));
        helper.postProcess(ys);
        final PrivateMethodTester privateMethodTester = new PrivateMethodTester(helper);
        final StatPack statPack = (StatPack) privateMethodTester.invokePrivate("getStatPack");
        System.out.println(statPack);
        final int compares = (int) statPack.getStatistics(COMPARES).mean();
        final int inversions = (int) statPack.getStatistics(INVERSIONS).mean();
        final int fixes = (int) statPack.getStatistics(FIXES).mean();
        final int swaps = (int) statPack.getStatistics(SWAPS).mean();
        final int hits = (int) statPack.getStatistics(HITS).mean();
        final long worstCompares = round(2.0 * N * Math.log(N));
        System.out.println("compares: " + compares + ", worstCompares: " + worstCompares);
        assertTrue(compares <= worstCompares);
        assertTrue(inversions <= fixes);
        assertEquals(swaps + compares, hits, 100000);
    }

    @Test
    public void testPartitionWithSort() {
        String[] xs = new String[]{"g", "f", "e", "d", "c", "b", "a"};
        int n = xs.length;
        final Config config = setupConfig("true", "true", "0", "1", "", "");
        final Helper<String> helper = new InstrumentedComparableHelper<>("test", config);
        final PrivateMethodTester privateMethodTester = new PrivateMethodTester(helper);
        QuickSort<String> sorter = new QuickSort_3way<>(helper);
        int inversions = n * (n - 1) / 2;
        assertEquals(inversions, helper.inversions(xs));
        Partitioner<String> partitioner = sorter.createPartitioner();
        List<Partition<String>> partitions = partitioner.partition(new Partition<>(xs, 0, xs.length));
        assertEquals(14L, privateMethodTester.invokePrivate("getFixes"));
        assertEquals(7, helper.inversions(xs));
        sorter.sort(xs, 0, partitions.get(0).to, 0);
        assertEquals(14L, privateMethodTester.invokePrivate("getFixes"));
        assertEquals(7, helper.inversions(xs));
        sorter.sort(xs, partitions.get(1).from, n, 0);
        assertEquals(0, helper.inversions(xs));
        assertTrue(helper.isSorted(xs));
        long fixes = (long) privateMethodTester.invokePrivate("getFixes");
        // NOTE: there are at least as many fixes as inversions -- sort methods aren't necessarily perfectly efficient in terms of swaps.
        System.out.println("inversions: " + inversions + ", fixes: " + fixes);
        assertTrue(inversions <= fixes);
        assertEquals(13L, privateMethodTester.invokePrivate("getSwaps")); // XXX check this
        assertEquals(16L, privateMethodTester.invokePrivate("getCompares")); // XXX check this
        assertEquals(50L, privateMethodTester.invokePrivate("getHits")); // XXX check this
    }

    final static LazyLogger logger = new LazyLogger(QuickSort_3way.class);

    private static String[] setupWords(final int n) {
        if (n > 36) throw new RuntimeException("cannot have n > 36");
        String alphabet = "abcdefghijklmnopqrstuvwxyz0123456789";
        String[] words = new String[n * n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                words[i * n + j] = alphabet.substring(i, i + 1) + alphabet.substring(j, j + 1);
        return words;
    }

    @BeforeClass
    public static void beforeClass() throws IOException {
        config = Config.load();
    }

    private static Config config;
}