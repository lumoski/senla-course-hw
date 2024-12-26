import java.util.Random;

public class LargestDigit {
    
    public static void main(String[] args) {
        Random random = new Random();
        int number = 100 + random.nextInt(900);

        int hundreds = number / 100;
        int tens = (number / 10) % 10;
        int ones = number % 10;

        int maxDigit = Math.max(hundreds, Math.max(tens, ones));

        System.out.println("Случайное трёхзначное число: " + number);
        System.out.println("Наибольшая цифра: " + maxDigit);
    }
}