import java.io.IOException;

public class Application {
    public static void main(String[] args) throws IOException {
        Subscribers subscribers = new Subscribers();
        CDR cdrFile = new CDR();

        cdrFile.generateCDRFile(subscribers.getNumbers());
        System.out.println("CDR файл успешно сгенерирован");
        System.out.println(cdrFile.getCallLog());
    }
}
