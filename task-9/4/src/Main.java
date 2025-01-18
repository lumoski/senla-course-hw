public class Main {
    public static void main(String[] args) throws InterruptedException {
        TimeThread timeThread = new TimeThread(1);
        timeThread.setDaemon(true);
        timeThread.start();

        // Задержка для демонстрации работы daemon потока
        while (true) {
            Thread.sleep(1000);
        }
    }
}

class TimeThread extends Thread {
    private final int seconds;
    
    public TimeThread(int seconds) {
        this.seconds = seconds;
    }
    
    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Текущее время: " + java.time.LocalTime.now());
                Thread.sleep(seconds * 1000);
            } catch (InterruptedException e) {
                System.out.println("Поток был прерван");
                break;
            }
        }
    }
}