import java.util.Random;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NameOfThreads {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        
        Runnable task = () -> {
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName());

                try {
                    Random random = new Random();
                    int seconds = random.nextInt(5) + 1;
                    TimeUnit.SECONDS.sleep(seconds);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        
        executor.submit(task);
        executor.submit(task);
        executor.shutdown();
    }
}