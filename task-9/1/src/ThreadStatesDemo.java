public class ThreadStatesDemo {
    private static final Object lock = new Object();
    
    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = () -> {
            try {
                Thread.sleep(1000);

                synchronized (lock) {
                    lock.wait();
                }

            } catch (InterruptedException e) {
                System.out.println("Thread is interrupted");
            }
        };

        // NEW
        Thread thread = new Thread(runnable);
        System.out.println("Thread state: " + thread.getState());

        // RUNNABLE
        thread.start();
        System.out.println("Thread state: " + thread.getState());

        // TIMED_WAITING
        Thread.sleep(500);
        System.out.println("Thread state: " + thread.getState());

        // WAITING
        Thread.sleep(1000);
        System.out.println("Thread state: " + thread.getState());

        synchronized (lock) {
            lock.notifyAll();

            // BLOCKED
            Thread.sleep(1000);
            System.out.println("Thread state: " + thread.getState());
        }

        // TERMINATED
        Thread.sleep(1000);
        System.out.println("Thread state: " + thread.getState());
    }
}