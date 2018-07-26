package cn.edu.cqupt.test;

public class ExecuteOrder {
    private static String str1 = "111";

    public ExecuteOrder() {
        System.out.println("构造: str1=" + str1);
    }

    static {
        System.out.println("static: str1=" + str1);
    }


    {
        System.out.println("{}: str1=" + str1);
    }

    public static void main(String[] args) {
        new ExecuteOrder();
        System.out.println("main: str1=" + str1);
    }
}
