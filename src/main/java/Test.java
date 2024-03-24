import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Test {
    public static void main(String[] args) {
        UDRGenerator udrGen = new UDRGenerator();
        udrGen.generateReport("70097280301");
    }
}
