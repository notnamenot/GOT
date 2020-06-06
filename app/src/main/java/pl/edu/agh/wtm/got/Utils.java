package pl.edu.agh.wtm.got;

public class Utils {
    public static String convertIntToTime(int time) {
        int h = time / 60;
        int m = time % 60;
        return h + ":" + m + "h";
    }
}
