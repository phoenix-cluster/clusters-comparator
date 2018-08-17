package cn.edu.cqupt.score.view;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchTest {
    public static void main(String[] args) {
        int times = 1000;
        CountDownLatch latch = new CountDownLatch(times);
        int[] array = new int[times];

        for (int i = 0; i < times; i++) {
            final int j = i;
            Thread t = new Thread(() -> {
                System.out.println("run " + j);
                array[j] = j;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                latch.countDown();
            });
            t.start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.print("array : ");
        for (int e : array) {
            System.out.print(e + "\t");
        }

    }
}
