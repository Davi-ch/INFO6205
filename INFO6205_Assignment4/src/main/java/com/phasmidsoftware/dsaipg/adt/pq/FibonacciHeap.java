package com.phasmidsoftware.dsaipg.adt.pq;
import java.util.Comparator;

public class FibonacciHeap<K> {
    private static class Node<K> {
        K key;
        Node<K> parent, child, next, prev;
        int degree;
        boolean mark;
    }

    private Node<K> min;
    private int size;
    private final Comparator<K> comparator;

    public FibonacciHeap(Comparator<K> comparator) {
        this.comparator = comparator;
    }

    public void insert(K key) {
        Node<K> node = new Node<>();
        node.key = key;
        node.next = node.prev = node;
        if (min == null) {
            min = node;
        } else {
            insertIntoRootList(node);
            if (comparator.compare(node.key, min.key) < 0) {
                min = node;
            }
        }
        size++;
    }

    public K extractMin() {
        if (min == null) return null;
        K minKey = min.key;
        if (min.child != null) {
            mergeWithRootList(min.child);
        }
        removeFromRootList(min);
        consolidate();
        size--;
        return minKey;
    }

    private void insertIntoRootList(Node<K> node) {
        node.next = min.next;
        min.next.prev = node;
        min.next = node;
        node.prev = min;
    }

    private void mergeWithRootList(Node<K> node) {
        Node<K> first = node;
        do {
            Node<K> next = node.next;
            insertIntoRootList(node);
            node = next;
        } while (node != first);
    }

    private void removeFromRootList(Node<K> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void consolidate() {

    }
}