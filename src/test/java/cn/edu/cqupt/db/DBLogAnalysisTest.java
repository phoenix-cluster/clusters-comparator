package cn.edu.cqupt.db;

import junit.framework.TestCase;

import java.io.IOException;

public class DBLogAnalysisTest extends TestCase {

    public void testHandleES() throws IOException {
        DBLogAnalysis.handle("E:\\workspace\\@project\\@program\\documents" +
                        "\\java\\clusters-compararor\\database\\es\\idea-bulk-write-6000.txt",
                "E:\\workspace\\@project\\@program\\documents" +
                        "\\java\\clusters-compararor\\database\\es\\result");
    }

    public void testHandleMysql() throws IOException {
        DBLogAnalysis.handle("E:\\workspace\\@project\\@program\\documents\\java\\clusters-compararor" +
                        "\\database\\mysql\\mysql-bulk-write.txt",
                "E:\\workspace\\@project\\@program\\documents\\java\\clusters-compararor" +
                        "\\database\\mysql\\result");
    }

    public void testHandleMysql6000() throws IOException {
        DBLogAnalysis.handle("E:\\workspace\\@project\\@program\\documents\\java\\clusters-compararor\\database\\mysql\\mysql-bulk-write-6000.txt",
                "E:\\workspace\\@project\\@program\\documents\\java\\clusters-compararor\\database\\mysql\\result");
    }
}