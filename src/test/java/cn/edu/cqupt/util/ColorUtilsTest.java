package cn.edu.cqupt.util;

import javafx.scene.paint.Color;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.stream.IntStream;

public class ColorUtilsTest extends TestCase {

    @Test
    public void testColorArray() {
        Color[] colors1 = ColorUtils.colorArray(75);
        Color[] colors2 = ColorUtils.colorArray(76);
        Color[] colors3 = ColorUtils.colorArray(77);
        IntStream.range(0, colors1.length).forEach(c->{
            System.out.print(c + ":" + colors1[c] + "\t");
            if((c+ 1) % 76 == 0){
                System.out.println();
            }
        });
        System.out.println();
        System.out.println("---------------------------");
        IntStream.range(0, colors2.length).forEach(c->{
            System.out.print(c + ":" + colors2[c] + "\t");
            if((c+ 1) % 76 == 0){
                System.out.println();
            }
        });
        System.out.println("---------------------------");
        IntStream.range(0, colors3.length).forEach(c->{
            System.out.print(c + ":" + colors3[c] + "\t");
            if((c+ 1) % 76 == 0){
                System.out.println();
            }
        });


    }

    @Test
    public void testRandom() {
        Random random = new Random();
        Object[] values = new Object[20];
        HashSet<Integer> hashSet = new HashSet<Integer>();

        // 生成随机数字并存入HashSet
        while (hashSet.size() < values.length) {
            int number = random.nextInt(100);
            hashSet.add(number);
        }

        values = hashSet.toArray();

        // 遍历数组并打印数据
        for (int i = 0; i < values.length; i++) {
            System.out.print(values[i] + "\t");

            if ((i + 1) % 10 == 0) {
                System.out.println("\n");
            }
        }

        System.out.println();
        hashSet.forEach(a -> {

            System.out.print(a + "\t");
        });
    }

    @Test
    public void testArray(){
        String[] strArr = new String[]{"A", "B"};
        String[] strArrCopy = Arrays.copyOf(strArr, strArr.length);
        String[] strArrClone = strArr.clone();

        for(int i = 0; i < strArr.length; i++){
            System.out.println("%%%%%%%%%%%%%%%%%" + i + "%%%%%%%%%%");
            System.out.println(strArr[i] + ":" + strArr.hashCode());
            System.out.println(strArrCopy[i] + ":" + strArrCopy.hashCode());
            System.out.println(strArrClone[i] + ":" + strArrClone.hashCode());
        }
    }


}