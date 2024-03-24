import java.io.IOException;

public class Application {
    public static void main(String[] args) throws IOException {
        Subscribers subscribers = new Subscribers();    //генерация абонентов
        CDR cdrFile = new CDR();

        cdrFile.generateCDRFile(subscribers.getNumbers());    //генерация cdr файлов
        System.out.println("CDR файлы успешно сгенерированы");

        UDRGenerator udrGen = new UDRGenerator();
        udrGen.generateReport();    //генерация udr отчета
    }
}
