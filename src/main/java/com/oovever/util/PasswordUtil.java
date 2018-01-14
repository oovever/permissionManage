package com.oovever.util;

import java.util.Date;
import java.util.Random;

/**
 * PassWord工具类
 * @author OovEver
 * 2018/1/14 23:58
 */
public class PasswordUtil {
//定义可以使用的字符
    public final static String[] word = {
            "a", "b", "c", "d", "e", "f", "g",
            "h", "j", "k", "m", "n",
            "p", "q", "r", "s", "t",
            "u", "v", "w", "x", "y", "z",
            "A", "B", "C", "D", "E", "F", "G",
            "H", "J", "K", "M", "N",
            "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"
    };
//定义可以使用的数字
    public final static String[] num = {
            "2", "3", "4", "5", "6", "7", "8", "9"
    };

    /**
     * 随机生成密码
     * @return 返回随机生成的密码
     */
    public static String randomPassword() {
        StringBuffer stringBuffer = new StringBuffer();
        Random       random       = new Random(new Date().getTime());
//        随机出数字还是字符
        boolean      flag         = false;
//        随机出可以使用的长度
        int          length       = random.nextInt(3) + 8;
        for (int i = 0; i < length; i++) {
//            flag为true随机出一个数字
            if (flag) {
                stringBuffer.append(num[random.nextInt(num.length)]);
            } else {
//                随机出一个字符
                stringBuffer.append(word[random.nextInt(word.length)]);
            }
//            保证密码在字符和数字之间
            flag = !flag;
        }
        return stringBuffer.toString();
    }
    public static void main(String[] args) throws Exception {
        System.out.println(randomPassword());
        Thread.sleep(100);
        System.out.println(randomPassword());
        Thread.sleep(100);
        System.out.println(randomPassword());
    }
}
