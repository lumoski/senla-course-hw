import java.util.Random;

public class NameOfThreads {
    private static final Object lock = new Object();
    private static volatile boolean isFirstThreadTurn = true;

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                synchronized (lock) {
                    while (!isFirstThreadTurn) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(Thread.currentThread().getName());
                    randomSleep();
                    isFirstThreadTurn = false;
                    lock.notifyAll();
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                synchronized (lock) {
                    while (isFirstThreadTurn) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(Thread.currentThread().getName());
                    randomSleep();
                    isFirstThreadTurn = true;
                    lock.notifyAll();
                }
            }
        });

        thread1.start();
        thread2.start();
    }

    private static void randomSleep() {
        try {
            Random random = new Random();
            Thread.sleep((random.nextInt(5) + 1) * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}