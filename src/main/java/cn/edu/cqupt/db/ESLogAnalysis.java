package cn.edu.cqupt.db;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ESLogAnalysis {
    public static void handle(String logFile, String resultPath) throws IOException {
        List<Double> writeDataTime = new ArrayList<>();
        List<Integer> dataSize = new ArrayList<>();
        List<Integer> spectrumCount = new ArrayList<>();

        List<String> lines = Files.lines(Paths.get(logFile), StandardCharsets.UTF_8)
                .collect(Collectors.toList());
        Iterator<String> itr = lines.iterator();
        Pattern p1 = Pattern.compile("6000 clusters has (\\d+) spectra");
        Pattern p2 = Pattern.compile("(\\d+) data costs (\\d+\\.\\d+)m");
        while (itr.hasNext()) {
            Matcher m1 = p1.matcher(itr.next());
            if (m1.find()) {
                spectrumCount.add(Integer.parseInt(m1.group(1)));
                Matcher m2 = p2.matcher(itr.next());
                if (m2.find()) {
                    dataSize.add(Integer.parseInt(m2.group(1)));
                    writeDataTime.add(Double.parseDouble(m2.group(2)));
                } else {
                    System.exit(2);
                }
            }
        }

        // write data into file
        Gson gson = new Gson();
        Arrays.stream(new String[]{"write_data_time.json", "data_size.json", "spectrum_count.json"})
                .map(s -> Paths.get(resultPath, s))
                .forEach(p -> {
                    try {
                        if (!Files.exists(p)) {
                            Files.createFile(p);
                        }
                        switch (p.getFileName().toString()) {
                            case "write_data_time.json":
                                Files.write(p, gson.toJson(writeDataTime).getBytes());
                                break;
                            case "data_size.json":
                                Files.write(p, gson.toJson(dataSize).getBytes());
                                break;
                            case "spectrum_count.json":
                                Files.write(p, gson.toJson(spectrumCount).getBytes());
                                break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }
}
