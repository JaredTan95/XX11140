package cn.tanjianff.sheetsmana;

import org.junit.Test;

import java.util.Arrays;

import cn.tanjianff.sheetsmana.util.HuffMan.HunffManAndroidImg;

/**
 * Created by tanjian on 16/11/10.
 */

public class HuffManCopress_test {
    private static byte[] bytes = new byte[]{2, 0, 8, 3, 9, 73, 98, 64, 87, 64, 8, 7, 5, 8, 7};

    @Test
    public void compressbytes_isOk() {
        System.out.println(Arrays.toString(HunffManAndroidImg.compressbytes(bytes)));
        System.out.println("------------------------------------------------------");
    }

    @Test
    public void HuffManexpend_isOk() {
        byte[] bytes1 = HunffManAndroidImg.compressbytes(bytes);
        System.out.println("HuffManexpend_isOk() ing ...");
        System.out.println(Arrays.toString(HunffManAndroidImg.expendbytes(bytes1)));
    }
}
