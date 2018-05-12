package cn.edu.cqupt.cmgf;

import cn.edu.cqupt.cmgf.graph.CMGFGraph;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CMGFGrapyTest {
    public static void main(String[] args) {
        try {
            List<CMGF> cmgfList = CMGFReader.readCMGFList(new File("D:\\@project\\program\\documents\\java\\cluster-comparer\\MGF_clustering_visualization_DengChuan\\spectra.mgf"),
                    new File("D:\\@project\\program\\documents\\java\\cluster-comparer\\MGF_clustering_visualization_DengChuan\\labels.txt"));

            CMGFGraph graph = new CMGFGraph();
            graph.printGroup(cmgfList);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
