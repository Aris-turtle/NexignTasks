import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

public class Test {
    public static void main(String[] args) {
        Random random = new Random();
//        Date beginOfPeriod = new Date();
//        System.out.println(beginOfPeriod.getTime());
//        beginOfPeriod.setTime(random.nextLong(beginOfPeriod.getTime()));
//        System.out.println(beginOfPeriod.getTime());
//
//        Date period = new Date(2629743*1000L);
//        System.out.println(period);
//        Date firstDate = new Date(1711146514877L);
//        //1711146514877
//        Date month = new Date(2629743000L);
//        Date anotherMonth = month;
//        System.out.println(anotherMonth.getTime());
//
//        TimeInterval ti = new TimeInterval(month, firstDate);
//        System.out.println(ti);
        int j = 6;
            Calendar calendar = new GregorianCalendar(1952, j, 1);
        System.out.println(calendar.getTimeInMillis());
        Date beginOfPeriod = new Date(calendar.getTimeInMillis());
        System.out.println(beginOfPeriod.getTime());
    }
}
