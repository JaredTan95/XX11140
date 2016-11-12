package cn.tanjianff.sheetsmana.util.HuffMan;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * Created by tanjian on 16/11/10.
 * 此类主要利用HuffMan算法完成Android图像的压缩存储
 */

public class HunffManAndroidImg {
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
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        /**
         * 判断是否为叶子结点
         *
         * @return boolean true | false
         */
        private boolean isLeaf() {
            assert (left == null && right == null) || (left != null && right != null);
            return (left == null && right == null);
        }

        /**
         * 基于频率比较
         *
         * @param that 要比较的对象
         * @return int  负整数、零或正整数，
         * 根据此对象比较结果是小于、等于还是大于指定对象。
         */
        @Override
        public int compareTo(Node that) {
            return this.freq - that.freq;
        }
    }

    public static byte[] compressbytes(byte[] bytes) {
        //转换输入
        char[] input = bytes2chars(bytes);

        //用于保存压缩后的二进制字符
        char[] outchars = new char[bytes.length * 8];

        //统计频率
        int[] freq = new int[R];
        for (int i = 0; i < input.length; i++) {
            freq[input[i]]++;
        }

        //构造霍夫曼编码树
        Node root = buildTrie(freq);
        String[] st = new String[R];

        //(递归地)构建解码用的单词查找树
        buildCode(st, root, "");

        // 打印字符总数
        int charslength = input.length;

        int bitscouts = 0;
        //使用HuffMan编码处理输入
        for (int i = 0; i < charslength; i++) {
            String code = st[input[i]];
            for (int j = 0; j < code.length(); j++) {
                if (code.charAt(j) == '0') {
                    ++bitscouts;
                    System.out.println(0);
                    outchars[0] = '0';
                } else if (code.charAt(j) == '1') {
                    ++bitscouts;
                    System.out.println(1);
                    outchars[i] = '1';
                } else throw new RuntimeException("Illegal state");
            }
        }
        return char2bytes(outchars);
    }

    public static byte[] expendbytes(byte[] bytes) {
        //从标准输出中读入HunffMan树
        Node root=readTrie();

        //需要写入的数量
        int length=bytes.length;

        //用于存放解码后的字符
        char[] chars = new char[length];

        //通过HuffMan树进行解码
        for(int i=0;i<length;i++){
            //展开第i个编码所对应的字母
            Node x=root;
            while (!x.isLeaf()){
                boolean bit=BinaryStdIn.readBoolean();
                if(bit) x=x.right;
                else    x=x.left;
            }
            chars[i]=x.ch;
        }
        return char2bytes(chars);
    }

    /**
     * 基于给定的频率来创建霍夫曼（Huffman）查找树
     *
     * @param freq 指定频率
     * @return Node
     */
    private static Node buildTrie(int[] freq) {

        // 使用多棵结点树初始化优先队列
        MinPQ<Node> pq = new MinPQ<>();
        for (char i = 0; i < R; i++)
            if (freq[i] > 0)
                pq.insert(new Node(i, freq[i], null, null));

        // 合并两棵频率最小的树
        while (pq.size() > 1) {
            Node left = pq.delMin();
            Node right = pq.delMin();
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            pq.insert(parent);
        }
        return pq.delMin();
    }

    /**
     * 使用单词查找树构造编译表
     *
     * @param st [description]
     * @param x  [description]
     * @param s  [description]
     */
    private static void buildCode(String[] st, Node x, String s) {
        if (!x.isLeaf()) {
            buildCode(st, x.left, s + '0');
            buildCode(st, x.right, s + '1');
        } else {
            st[x.ch] = s;
        }
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

    /**
     * 将字符数组(char [])转换为字节数组(byte[])类型
     *
     * @Params char []
     **/
    public static byte[] char2bytes(char[] chars) {
        CharBuffer cb = CharBuffer.allocate(chars.length);
        cb.put(chars).flip();
        ByteBuffer bb = Charset.forName("UTF-8").encode(cb);
        return bb.array();
    }

    /**
     * 将字节数组(byte[])类型转换为字符数组(char [])
     *
     * @Params byte []
     **/
    public static char[] bytes2chars(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.allocate(bytes.length);
        bb.put(bytes).flip();
        return Charset.forName("UTF-8").decode(bb).array();
    }
}
