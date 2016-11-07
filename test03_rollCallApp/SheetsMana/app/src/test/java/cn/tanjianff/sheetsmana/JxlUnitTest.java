package cn.tanjianff.sheetsmana;

import org.junit.Test;

import java.io.IOException;

import jxl.read.biff.BiffException;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class JxlUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void importExcel2Arralist_isOk() throws IOException, BiffException {
        //System.out.println(jExcel.readExcelTable(String.valueOf(new File("test.xls"))));
    }
}