package cn.edu.cqupt.mgf;

import cn.edu.cqupt.score.calculate.MS;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangjs on 2018/4/1.
 */
public class MgfFileReaderTest {
    public static void main(String[] args) {
        try {
            List<MS> msList = MgfFileReader.getAllSpectra(new File("C:\\Users\\huangjs\\Desktop\\mgf\\header_test.mgf"));
            for (MS ms : msList) {
                System.out.println(ms.getTitle());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
