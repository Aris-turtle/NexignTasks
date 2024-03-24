import org.json.simple.JSONObject;

import java.io.*;
import java.util.*;

public class UDRGenerator implements Serializable {
    /**Словарь, который содержит данные для каждого абонента о суммарном времени разговоров на каждый тип звонка, на каждый месяц */
    private Map<String, Map<Integer, Map<String, AdvancedLong>>> reportMap = new HashMap<>();

    public Map<String, Map<Integer, Map<String, AdvancedLong>>> getReportMap() {
        return reportMap;
    }

    /**Метод генерации UDR отчета*/
    public void generateReport(){
        if (reportMap.size()==0){       //проверяем заполнялся ли reportMap другим методом
            //если нет, то заполняем
            this.fillReportMap();
        }

        //вывод отчета в консоль
        //шапка вывода в консоль
        final String firstMethodOutputHeader = "___________________________________________________________________________________________________________________________________________________\n" + "|   msisdn    |";
        //вывод шапки
        System.out.print(firstMethodOutputHeader);
        for (int i = 0; i < 12; i++){
            System.out.printf("%6s    |", i);
        }
        System.out.println();

        //вывод содержимого
        for (Map.Entry<String, Map<Integer, Map<String, AdvancedLong>>> e : reportMap.entrySet()){

            //создание json файлов
            for (Map.Entry<Integer, Map<String, AdvancedLong>> m : e.getValue().entrySet()){
                createJSONFile(e.getKey(), m.getKey());
            }

            System.out.print("| " + e.getKey() + " |"); //вывод номера

            for (int i = 0; i < 12; i++){   //вывод итогового времени
                long totalTime = e.getValue().get(i).get("01").getValue() + e.getValue().get(i).get("02").getValue();
                //перевод миллисекунд в формат hh:mm:ss
                long hours = totalTime/(1000*60*60);
                long minutes = (totalTime / (1000 * 60)) % 60;
                long seconds = (totalTime / 1000) % 60;
                String timeString = hours+":"+minutes+":"+seconds;
                System.out.printf("%9s |", timeString);
            }
            System.out.println();
        }

    }
    public void generateReport(String msisdn){
        if (reportMap.size()==0){       //проверяем заполнялся ли reportMap другим методом
            //если нет, то заполняем
            this.fillReportMap();
        }

        //создание json файлов
        for (Map.Entry<String, Map<Integer, Map<String, AdvancedLong>>> e : reportMap.entrySet()) {
            for (Map.Entry<Integer, Map<String, AdvancedLong>> m : e.getValue().entrySet()) {
                createJSONFile(e.getKey(), m.getKey());
            }
        }

        //вывод отчета в консоль
        //вывод шапки
        System.out.printf("________________________________\n"
                        + "| Subscriber: %s      |\n"
                        + "|                              |\n"
                        + "|   month   |     totalTime    |\n"
                        + "|------------------------------|\n", msisdn);



        //вывод содержимого reportMap
        for (Map.Entry<Integer, Map<String, AdvancedLong>> e : reportMap.get(msisdn).entrySet()){
                long totalTime = e.getValue().get("01").getValue() + e.getValue().get("02").getValue();

                //перевод миллисекунд в формат hh:mm:ss
                long hours = totalTime/(1000*60*60);
                long minutes = (totalTime / (1000 * 60)) % 60;
                long seconds = (totalTime / 1000) % 60;
                String timeString = hours+":"+minutes+":"+seconds;
                System.out.printf("| %9s |" + "%17s |", e.getKey(), timeString);
                System.out.println();
        }
    }

    public void generateReport(String msisdn, int month){
        if (reportMap.size()==0){       //проверяем заполнялся ли reportMap другим методом
            //если нет, то заполняем
            this.fillReportMap();
        }

        createJSONFile(msisdn, month);

        //вывод отчета в консоль
        //вывод шапки
        System.out.printf("________________________________\n"
                + "| Subscriber: %s      |\n"
                + "|                              |\n"
                + "|   month   |     totalTime    |\n"
                + "|------------------------------|\n", msisdn);

        long totalTime = reportMap.get(msisdn).get(month).get("01").getValue() + reportMap.get(msisdn).get(month).get("02").getValue();

        //перевод миллисекунд в формат hh:mm:ss
        long hours = totalTime/(1000*60*60);
        long minutes = (totalTime / (1000 * 60)) % 60;
        long seconds = (totalTime / 1000) % 60;
        String timeString = hours+":"+minutes+":"+seconds;
        System.out.printf("| %9s |" + "%17s |", month, timeString);
    }


    /**Данный метод заполняет словарь reportMap.
     * Каждый cdr файл прочитывается, затем каждая строка разбивается на состовляющие:
     * callStatus (тип звонка), msisdn(номер телефона), callStart, callEnd, которые заполняют словарь в соответствии
     * номеру, месяцу, типу звонка и суммарном времени звонков*/
    private void fillReportMap(){
        for (int i = 0; i < 12; i++){   //перебор 12 файлов

            File file = new File("CDRFiles/cdr_" + i +".txt");  //путьь к cdr файлам прописывается внутри тела метода, так как во входных параметрах путь не может быть указан по заданию

            try (Scanner scanner = new Scanner(file)) {     //чтение cdr файла

                while(scanner.hasNextLine()){               //чтение строки

                    //разбиение строки на состовляющие
                    String[] udrLine = scanner.nextLine().split(",");
                    String callStatus = udrLine[0];     //тип звонка
                    String msisdn = udrLine[1];     //номер телефона
                    long callStart = Long.parseLong(udrLine[2]);    //начало звонка
                    long callEnd = Long.parseLong(udrLine[3]);      //конец звонка

                    //заполнение словаря reportMap
                    if (!reportMap.containsKey(msisdn)){    //проверяем есть ли такой номер уже в словаре

                        //запись номера в словарь производиться в первый раз
                        reportMap.put(msisdn, new HashMap<>());        //добавляем в словарь текущий номер
                        for (int j = 0; j < 12; j++){           //заполняем структуру внутренних словарей (добавляем месяцы, типы звонков и нулевые значения для них)
                            reportMap.get(msisdn).put(j, new HashMap<>(Map.of("01", new AdvancedLong(), "02", new AdvancedLong())));
                        }
                        reportMap.get(msisdn).get(i).get(callStatus).increment(callEnd - callStart);    //заполняем значения последней мапы
                    } else if (reportMap.containsKey(msisdn)){
                        //запись номера в словарь производиться не в первый раз
                        reportMap.get(msisdn).get(i).get(callStatus).increment(callEnd - callStart);    //заполняем значения последней мапы
                    }

                }


            } catch (FileNotFoundException e) {
                System.out.println("File not found " + e);
            }
        }
    }

    private void createJSONFile(String msisdn, int month){
        long totalTime01 = reportMap.get(msisdn).get(month).get("01").getValue();
        long totalTime02 = reportMap.get(msisdn).get(month).get("02").getValue();

        long hours01 = totalTime01/(1000*60*60);
        long minutes01 = (totalTime01 / (1000 * 60)) % 60;
        long seconds01 = (totalTime01 / 1000) % 60;
        String timeString01 = hours01+":"+minutes01+":"+seconds01;

        long hours02 = totalTime02/(1000*60*60);
        long minutes02 = (totalTime02 / (1000 * 60)) % 60;
        long seconds02 = (totalTime02 / 1000) % 60;
        String timeString02 = hours02+":"+minutes02+":"+seconds02;


        JSONObject jsonObject = new JSONObject();
        JSONObject incomingCall = new JSONObject();
        JSONObject outcomingCall = new JSONObject();

        jsonObject.put("msisdn", msisdn);
        incomingCall.put("totalTime", timeString01);
        outcomingCall.put("totalTime", timeString02);
        jsonObject.put("incomingCall", incomingCall);
        jsonObject.put("outcomingCall", outcomingCall);

        File file = new File("reports/" + msisdn + "_" + month + ".json");
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("File not found " + e);
        }

        try(FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(jsonObject.toJSONString());
        } catch (IOException e) {
            System.out.println("File not found " + e);
        }
    }
}

/**Класс лонг с методом инкремента*/
class AdvancedLong extends Number {
    private long value;

    public AdvancedLong() {
        value = 0;
    }

    @Override
    public int intValue() {
        return (int)value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return (float) value;
    }

    @Override
    public double doubleValue() {
        return (double) value;
    }

    public AdvancedLong(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public void increment(Number number){
        value+=(long)number;
    }

    @Override
    public String toString() {
        return value + "";
    }
}