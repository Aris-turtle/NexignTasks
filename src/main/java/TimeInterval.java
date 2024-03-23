import java.util.Date;

public class TimeInterval {
    Date start;
    Date end;
    public TimeInterval(Date start, Date end){
        this.start = start;
        this.end = end;
    }

    public boolean isIntersects(Date start, Date end){
        if (this.start.getTime() <= start.getTime() && start.getTime() <= this.end.getTime() ||
                this.start.getTime() <= end.getTime() && end.getTime() <= this.end.getTime())
            return true;
        else
            return false;
    }

    @Override
    public String toString() {
        return this.start + " - " + this.end;
    }
}
