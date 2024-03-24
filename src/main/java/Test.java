import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Test {
    public static void main(String[] args) {
        Random random = new Random();
//        UDRGenerator udrFile = new UDRGenerator("78516897504", Map.of("totalTime", "02:12:05"), Map.of("totalTime", "00:34:27"));
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("msisdn", udrFile.getMsisdn());
//        jsonObject.put("incomingCall", udrFile.getIncomingCall());
//        jsonObject.put("outcomingCall", udrFile.getOutcomingCall());
//
//        try {
//            File file = new File("reports/test.json");
//            file.createNewFile();
//            FileWriter fileWriter = new FileWriter(file);
//
//            fileWriter.write(jsonObject.toJSONString());
//            fileWriter.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        UDRGenerator udrGenerator = new UDRGenerator();
        udrGenerator.generateReport();

        //вывод суммарных минут на каждого пользователя
        long totalCall = 0;
        for (Map.Entry<String, Map<String, AdvancedLong>> m: udrGenerator.getSubscribersSummary().entrySet()){
            String msisdn = m.getKey();
            totalCall = m.getValue().get("01").getValue() + m.getValue().get("02").getValue();      //суммарное время разговоров
            System.out.println(msisdn + " " + totalCall);
        }
    }
}
