package cn.tanjianff.sheetsmana.util.HuffMan; /*************************************************************************
 *  Compilation:  javac Huffman.java
 *
 *  Compress or expand a binary input stream using the Huffman algorithm.
 *
 *  % java Huffman - < abra.txt | java BinaryDump 60
 *  010100000100101000100010010000110100001101010100101010000100
 *  000000000000000000000000000110001111100101101000111110010100
 *  120 bits
 *
 *  % java Huffman - < abra.txt | java Huffman +
 *  ABRACADABRA!
 *
 *************************************************************************/
import java.util.Scanner;
public class Huffman {

    /**
     * ASCII字母表
     */
    private static final int R = 256;

    /***********************************************************************
    * Huffman trie node-霍夫曼单词查找树中的结点
    ***********************************************************************/
    private static class Node implements Comparable<Node> {
       /**
        * 用于表示叶子结点中需要被编码的字符；内部结点不会使用该变量
        */
        private final char ch;
       /**
        * 用于统计频率；展开过程不会使用该变量
        */
        private final int freq;
        private final Node left, right;

        Node(char ch, int freq, Node left, Node right) {
            this.ch    = ch;
            this.freq  = freq;
            this.left  = left;
            this.right = right;
        }
       /**
        * 判断是否为叶子结点
        * @return boolean true | false
        */
        private boolean isLeaf() {
            assert (left == null && right == null) || (left != null && right != null);
            return (left == null && right == null);
        }

       /**
        * 基于频率比较
        * @param  that 要比较的对象
        * @return  int  负整数、零或正整数，
        * 根据此对象比较结果是小于、等于还是大于指定对象。
        */
        public int compareTo(Node that) {
            return this.freq - that.freq;
        }
    }

   /**
    * 对标准输入进行压缩，并将字节写入标准输出
    */
    public static void compress() {
        //读取输入
        String s = BinaryStdIn.readString();
        char[] input = s.toCharArray();

        // 统计频率
        int[] freq = new int[R];
        for (int i = 0; i < input.length; i++)
            freq[input[i]]++;

        // 构造霍夫曼编码树
        //此时，根结点（root）为全局中最小的元素，即freq值为最大
        Node root = buildTrie(freq);

        // （递归地）构造编译表
        String[] st = new String[R];
        buildCode(st, root, "");

        // （递归地）打印解码用的单词查找树
        writeTrie(root);

        // 打印字符总数
        BinaryStdOut.write(input.length);

        // 使用Huffman编码处理输入
        for (int i = 0; i < input.length; i++) {
            String code = st[input[i]];
            for (int j = 0; j < code.length(); j++) {
                if (code.charAt(j) == '0') {
                    BinaryStdOut.write(false);
                }
                else if (code.charAt(j) == '1') {
                    BinaryStdOut.write(true);
                }
                else throw new RuntimeException("Illegal state");
            }
        }

        // 刷新输出流
        BinaryStdOut.flush();
    }

    /**
     * 基于给定的频率来创建霍夫曼（Huffman）查找树
     * @param  freq 指定频率
     * @return Node
     */
    private static Node buildTrie(int[] freq) {

        // 使用多棵结点树初始化优先队列
        MinPQ<Node> pq = new MinPQ<Node>();
        for (char i = 0; i < R; i++)
            if (freq[i] > 0)
                pq.insert(new Node(i, freq[i], null, null));

        // 合并两棵频率最小的树
        while (pq.size() > 1) {
            Node left  = pq.delMin();
            Node right = pq.delMin();
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            pq.insert(parent);
        }
        return pq.delMin();
    }

    /**
     * 将已经编码完的比特字符串写到标准输出中
     * @param x 树结点
     */
    private static void writeTrie(Node x) {
        if (x.isLeaf()) {
            BinaryStdOut.write(true);
            BinaryStdOut.write(x.ch);
            return;
        }
        BinaryStdOut.write(false);
        writeTrie(x.left);
        writeTrie(x.right);
    }

    /**
     * 使用单词查找树构造编译表
     * @param st [description]
     * @param x  [description]
     * @param s  [description]
     */
    private static void buildCode(String[] st, Node x, String s) {
        if (!x.isLeaf()) {
            buildCode(st, x.left,  s + '0');
            buildCode(st, x.right, s + '1');
        }
        else {
            st[x.ch] = s;
        }
    }

    /**
    * 将从标准输入中的编码展开并写到标准输出中；
    */
    public static void expand() {
        //从标准输出读入Huffman树
        Node root = readTrie();

        // 需要写入的数量
        int length = BinaryStdIn.readInt();

        //通过Huffman树进行解码
        for (int i = 0; i < length; i++) {
          //展开第i个编码所对应的字母
            Node x = root;
            while (!x.isLeaf()) {
                boolean bit = BinaryStdIn.readBoolean();
                if (bit) x = x.right;
                else     x = x.left;
            }
            BinaryStdOut.write(x.ch);
        }
        BinaryStdOut.flush();
    }

    /**
     * 从比特流的前序表示中重建单词树
     * @return Node
     */
    private static Node readTrie() {
        boolean isLeaf = BinaryStdIn.readBoolean();
        if (isLeaf) {
            return new Node(BinaryStdIn.readChar(), -1, null, null);
        }
        else {
            return new Node('\0', -1, readTrie(), readTrie());
        }
    }


    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();   //压缩
        else if (args[0].equals("+")) expand();     //解压
        else throw new RuntimeException("Illegal command line argument");//非法参数
    }
}
