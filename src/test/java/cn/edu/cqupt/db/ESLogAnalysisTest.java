package cn.edu.cqupt.db;

import junit.framework.TestCase;

import java.io.IOException;

public class ESLogAnalysisTest extends TestCase {

    public void testHandle() throws IOException {
        ESLogAnalysis.handle("E:\\workspace\\@project\\@program\\documents" +
                "\\java\\clusters-compararor\\database\\es\\idea-bulk-write-6000.txt",
                "E:\\workspace\\@project\\@program\\documents" +
                        "\\java\\clusters-compararor\\database\\es\\result");
    }
}