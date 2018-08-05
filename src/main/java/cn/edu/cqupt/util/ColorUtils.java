package cn.edu.cqupt.util;

import javafx.scene.paint.Color;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ColorUtils {

    public static String colorToHex(Color color) {
        String hex1;
        String hex2;

        hex1 = Integer.toHexString(color.hashCode()).toUpperCase();

        switch (hex1.length()) {
            case 2:
                hex2 = "000000";
                break;
            case 3:
                hex2 = String.format("00000%s", hex1.substring(0, 1));
                break;
            case 4:
                hex2 = String.format("0000%s", hex1.substring(0, 2));
                break;
            case 5:
                hex2 = String.format("000%s", hex1.substring(0, 3));
                break;
            case 6:
                hex2 = String.format("00%s", hex1.substring(0, 4));
                break;
            case 7:
                hex2 = String.format("0%s", hex1.substring(0, 5));
                break;
            default:
                hex2 = hex1.substring(0, 6);
        }
        return "#" + hex2;
    }

    public static List<Color> originalColorList;

    static {
        try {

            originalColorList = Files.lines(Paths.get(
                    ColorUtils.class.getResource("/color.list").toURI()))
                    .map(c -> Color.web(c)).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static Color[] colorArray(int count) {
        Color[] colorArray = null;
        if (count < originalColorList.size()) {
            colorArray = Arrays.copyOfRange(originalColorList.stream().toArray(Color[]::new), 0, count);
        } else {
            colorArray = Arrays.copyOf(originalColorList.stream().toArray(Color[]::new), count);
            for (int i = originalColorList.size(); i < count; i++) {
                colorArray[i] = originalColorList.get(i % originalColorList.size());
            }
        }
        return colorArray;
    }

}
