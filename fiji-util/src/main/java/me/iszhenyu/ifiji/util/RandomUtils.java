package me.iszhenyu.ifiji.util;

import me.iszhenyu.ifiji.util.provider.RandomProvider;

import java.util.List;
import java.util.Random;

/**
 * @author zhen.yu
 * @since 2017/6/23
 */
public class RandomUtils {
    private static final Random ran;

    static {
        ran = RandomProvider.getRandom();
    }

    public static long nextLong() {
        return ran.nextLong();
    }

    public static int nextInt() {
        return ran.nextInt();
    }

    public static int nextInt(int n) {
        return ran.nextInt(n);
    }

    public static int nextInt(int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException("start greater than end");
        }
        if (start == end) {
            return start;
        }
        int i = end - start + 1;
        return nextInt(i) + start;
    }

    public static String randomChinese(int len) {
        StringBuilder sbuf = new StringBuilder();
        for (int i = 0; i < len; i++) {
            char c = (char) (0x4e00 + nextInt(0x9fa5 - 0x4e00 + 1));
            sbuf.append(c);
        }
        return sbuf.toString();
    }

    public static boolean hitProbability(double probability) {
        int base = 10000;
        int range = (int) (probability * base);
        int i = nextInt(0, base - 1);
        return i < range;
    }

    public static boolean hitPercentage(int percentage) {
        int i = nextInt(1, 100);
        return i <= percentage;
    }

    public static <T> T pickRandomElementFromList(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        int index = ran.nextInt(list.size());
        return list.get(index);
    }


    /**
     * 返回一个定长的随机字符串(只包含大小写字母、数字)
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String randomString(int length) {
        if (length < 0) {
            throw new IllegalArgumentException();
        }
        if (length == 0) {
            return "";
        }
        return randomString(length, Chars_Alpha_Number);
    }

    public static String randomNumeric(int length) {
        if (length < 0) {
            throw new IllegalArgumentException();
        }
        if (length == 0) {
            return "";
        }
        return randomString(length, Chars_Number);
    }

    public static final String Chars_Number = "0123456789";
    public static final String Chars_Alpha_Number = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String Chars_UpperAlpha_Number = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String randomString(int length, String chars) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(ran.nextInt(chars.length())));
        }
        return sb.toString();
    }


    // 从大量数据中随机抽取少量数据且原数组不能被破坏的时候, 效率比较高
    // 这个算法的复杂度是 O(n^2)，实际大概需要 3*n*(n-1)/4 次操作
    // 由于all不能被破坏, 所以这个算法的价值是在 count < sqrt(all.size()) 的时候才适用
    // 如果all可以被破坏, 直接用洗牌算法就可以了, 不用费这么多事.
    public static <T> void randomPickFew(List<T> all, int count, T[] result) {
        if (result == null || result.length < count)
            throw new IllegalArgumentException();

        int[] selected = new int[count];
        for (int i = 0; i < count; i++) {

            //随机取 [0, all-i-1] 的一个下标，概率是 1/(all-i)
            //但是这个不是最终要被选的下标。因为有些下标已经被取走了
            int k = nextInt(all.size() - i);

            //pos记录本次最终要选的下标，在选中表里面应该插入到哪个位置
            int pos = count - i - 1;

            //按照从小到大的顺序，遍历选中列表。如果发现某个下标已经被取走，说明当前随机出来的k应该指向下一个元素
            for (int j = count - i; j < count; j++) {
                if (k >= selected[j]) {
                    k++;
                    pos = j;
                }
            }

            result[i] = all.get(k);

            //如果已经处理到最后一个，就不需要后续处理了，直接返回即可
            if (i == count - 1) break;

            //把当前要取走的元素下标，按从小到大的顺序插入到选中表中
            for (int j = count - i; j <= pos; j++) {
                selected[j - 1] = selected[j];
            }
            selected[pos] = k;
        }
    }
}
