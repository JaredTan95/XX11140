package cn.tanjianff.sheetsmana.util.HuffMan; /*************************************************************************
 *  Compilation:  javac MinPQ.java
 *  Execution:    java MinPQ < input.txt
 *
 *  通过二叉堆实现一个最小优先队列
 *  可以用作一个比较器而不是自然秩序。
 *  % java MinPQ < tinyPQ.txt
 *  E A E (6 left on pq)
 *
 *  我们使用一个数组从父母和孩子来简化计算。
 *
 *************************************************************************/

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MinPQ<Key> implements Iterable<Key> {
    private Key[] pq;                    // store items at indices 1 to N
    private int N;                       // number of items on priority queue
    private Comparator<Key> comparator;  // optional comparator

   /**
     * 根据指定的容量创建一个空的优先队列
     */
    public MinPQ(int initCapacity) {
        pq = (Key[]) new Object[initCapacity + 1];
        N = 0;
    }

   /**
     * 创建一个空的优先队列
     */
    public MinPQ() { this(1); }

   /**
     * 通过指定的容量建立初始优先队列，
     * 同时使用给定的比较器
     */
    public MinPQ(int initCapacity, Comparator<Key> comparator) {
        this.comparator = comparator;
        pq = (Key[]) new Object[initCapacity + 1];
        N = 0;
    }

   /**
     *  通过给定的比较器创建一个空的优先队列
     */
    public MinPQ(Comparator<Key> comparator) { this(1, comparator); }

   /**
     *  通过传入的数组建立优先队列
     */
    public MinPQ(Key[] keys) {
        N = keys.length;
        pq = (Key[]) new Object[keys.length + 1];
        for (int i = 0; i < N; i++)
            pq[i+1] = keys[i];
        for (int k = N/2; k >= 1; k--)
            sink(k);
        assert isMinHeap();
    }

   /**
     * 返回队列是否为空？
     */
    public boolean isEmpty() {
        return N == 0;
    }

   /**
     * 返回队列中元素的个数
     */
    public int size() {
        return N;
    }

   /**
     *  返回优先队列中最小元素
     *  当队列为空时，抛出异常
     */
    public Key min() {
        if (isEmpty()) throw new RuntimeException("Priority queue underflow");
        return pq[1];
    }

    //将对数组大小扩展为2倍的辅助函数
    private void resize(int capacity) {
        assert capacity > N;
        Key[] temp = (Key[]) new Object[capacity];
        for (int i = 1; i <= N; i++) temp[i] = pq[i];
        pq = temp;
    }

   /**
     *  向优先队列中添加一个新的元素
     */
    public void insert(Key x) {
        //如果需要，将数组扩展至原来的两倍
        if (N == pq.length - 1) resize(2 * pq.length);

        //将元素X加入到队列中，并将队列恢复至原来状态
        pq[++N] = x;
        swim(N);
        assert isMinHeap();
    }

   /**
     *  删除并返回队列中最小元素
     *  当队列为时，抛出异常
     */
    public Key delMin() {
        if (N == 0) throw new RuntimeException("Priority queue underflow");
        exch(1, N);
        Key min = pq[N--];
        sink(1);
        pq[N+1] = null;         // 通过GC（垃圾回收机制）防止对象游离
        if ((N > 0) && (N == (pq.length - 1) / 4)) resize(pq.length  / 2);
        assert isMinHeap();
        return min;
    }


   /***********************************************************************
    *   恢复队列有序化的辅助函数
    **********************************************************************/

    private void swim(int k) {
        while (k > 1 && greater(k/2, k)) {
            exch(k, k/2);
            k = k/2;
        }
    }

    private void sink(int k) {
        while (2*k <= N) {
            int j = 2*k;
            if (j < N && greater(j, j+1)) j++;
            if (!greater(k, j)) break;
            exch(k, j);
            k = j;
        }
    }

   /***********************************************************************
    *   对于比较和交换操作的辅助函数
    **********************************************************************/
    private boolean greater(int i, int j) {
        if (comparator == null) {
            return ((Comparable<Key>) pq[i]).compareTo(pq[j]) > 0;
        }
        else {
            return comparator.compare(pq[i], pq[j]) > 0;
        }
    }

    private void exch(int i, int j) {
        Key swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
    }

    //判断pq[1..N]是否为一个最小堆
    private boolean isMinHeap() {
        return isMinHeap(1);
    }

    //  pq[1 . .N]的子树且根为k的子树是否为最小堆
    private boolean isMinHeap(int k) {
        if (k > N) return true;
        int left = 2*k, right = 2*k + 1;
        if (left  <= N && greater(k, left))  return false;
        if (right <= N && greater(k, right)) return false;
        return isMinHeap(left) && isMinHeap(right);
    }


   /***********************************************************************
    * 迭代器
    **********************************************************************/

    public Iterator<Key> iterator() { return new HeapIterator(); }

    private class HeapIterator implements Iterator<Key> {
        // 创建新的副本优先队列
        private MinPQ<Key> copy;

        //复制队列元素到此副本队列中
        //由于原队列已经呈线性序列，因此没有元素移动
        public HeapIterator() {
            if (comparator == null) copy = new MinPQ<Key>(size());
            else                    copy = new MinPQ<Key>(size(), comparator);
            for (int i = 1; i <= N; i++)
                copy.insert(pq[i]);
        }

        public boolean hasNext()  { return !copy.isEmpty();                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Key next() {
            if (!hasNext()) throw new NoSuchElementException();
            return copy.delMin();
        }
    }

/*   *//**
     * 测试用例
     *//*
    public static void main(String[] args) {
        MinPQ<String> pq = new MinPQ<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) pq.insert(item);
            else if (!pq.isEmpty()) StdOut.print(pq.delMin() + " ");
        }
        StdOut.println("(" + pq.size() + " left on pq)");
    }*/

}
