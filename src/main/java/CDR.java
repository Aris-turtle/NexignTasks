import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class CDR {
    private Map<String, List<TimeInterval>> callLog = new HashMap<>();  //журнал звонков

    public Map<String, List<TimeInterval>> getCallLog() {
        return callLog;
    }

    /**
     * Метод генерирующий 12 CDR файлов на каждый месяц.
     * На вход принимает список номеров (абонентов)*/
    public void generateCDRFile(List<String> subscribers) throws IOException {
        Random random = new Random();
        int year = (random.nextInt(54)+1970);       //год случайный в промежутке от 1970 до 2024
        Date period = new Date(2629743 * 1000L);    //Период 1 месяц

        for (int j = 0; j < 12; j++) {
            File file = new File("CDRFiles/cdr_" + j +".txt");
            file.createNewFile();   //создание файла
            Calendar beginOfPeriod = new GregorianCalendar(year, j, 1);;

            try (PrintWriter writer = new PrintWriter(file)) {
                int amount = random.nextInt(100) + 1;   //количество записей в CDR файле (также для быстроты ограничил число значением от 1 до 100)
                for (int i = 0; i < amount; i++) {
                    String currentSubscriber = subscribers.get(random.nextInt(subscribers.size())); //Выбор случайного абонента
                    writer.println("0" + (random.nextInt(2) + 1) + "," +                    //Запись одной строки в CDR файл
                            "7" + currentSubscriber + "," +
                            generateStartAndEndOfCall(beginOfPeriod.getTimeInMillis(), period.getTime(), currentSubscriber)
                    );
                }
                System.out.println("Количество записей в файле cdr_" + j + " - " + amount);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Данный метод возвращает случайное время разговора в формате строки "начало звонка, конец звонка".
     * <p>На вход принимается начальное время и период генерации (в Unix time) и абонент для которого генерируется время звонка.</p>
     * <p> Метод реализован так, что конец звонка позже начала звонка и абоненту, переданному в параметре currentSubscriber,
     * не может принадлежать время разовора, которое у него была записано ранее, так как абонент не может иметь два телефонных звонка с одного номера одновременно.
     * <p>Для реалистичности данных была использована генерация псевдослучаных чисел по нормальному закону с мат ожиданием в 68 секунд и отклонением 10 секунд
     */
    private String generateStartAndEndOfCall(long beginOfPeriod, long period, String currentSubscriber) {
        Random random = new Random();
        Date endOfPeriod = new Date(period + beginOfPeriod);
        Date callStart;
        Date callEnd;
        boolean isIntersects = true;

         do {  //проверка пересечения временных интервалов у одного абонента
            //генерация начала и конца звонка
            callStart = new Date(random.nextLong(endOfPeriod.getTime() - beginOfPeriod) + beginOfPeriod);
            callEnd = new Date(callStart.getTime() + (long) (random.nextGaussian() * 10*1000 + 68*1000));

            if (!callLog.containsKey(currentSubscriber)) {      //если абонента нет в словаре, то временной интервал для него генерируется в первый раз и пересечений нет
                callLog.put(currentSubscriber, new ArrayList<>(Arrays.asList(new TimeInterval(callStart, callEnd))));
                return callStart.getTime() + "," + callEnd.getTime();

            } else {    //в противном случае, сгенерированный временной интервал текущего абонента проверяется на пересечение с другими его интервалами
                for (int i = 0; i < callLog.get(currentSubscriber).size(); i++) {
                    if ((callLog.get(currentSubscriber).get(i).isIntersects(callStart, callEnd))) {
                        isIntersects = true;
                        break;
                    }
                    isIntersects = false;
                }
            }
        } while (isIntersects);

        callLog.get(currentSubscriber).add(new TimeInterval(callStart, callEnd));
        return callStart.getTime() + "," + callEnd.getTime();
    }
}