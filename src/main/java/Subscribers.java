import java.util.ArrayList;
import java.util.Random;


public class Subscribers {
    //множество уникальных номеров (абонентов)
    private ArrayList<String> numbers = new ArrayList<>();

    /**Констуктор заполняет множество номеров (абонентов) случайными уникальными значениями.
     * Количиство абонентов так же случаное число более 10 (для удобства и быстроты работы программы ограничил число до 100 раличных абонентов) */
    public Subscribers(){
        int amount = new Random().nextInt(100) + 10;
        System.out.println("Абонентов " + amount);
        while(numbers.size() != amount){
            String randomNumber = generateOneNumber();
            if (!numbers.contains(randomNumber))
                numbers.add(randomNumber);
        }
    }

    public ArrayList<String> getNumbers() {
        return numbers;
    }


    //Генерация одного номера
    private static String generateOneNumber(){
        StringBuilder number = new StringBuilder("");
        Random random = new Random();
        for (int i = 0; i < 10; i++){
            number.append(random.nextInt(10));
        }
        return number.toString();
    }
}
