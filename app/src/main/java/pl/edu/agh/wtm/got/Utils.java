package pl.edu.agh.wtm.got;

public class Utils {
    public static String convertIntToTime(int time) {
        int h = time / 60;
        int m = time % 60;
        return h + ":" + m + " h";
    }

    public static Double metersToKilometers(int m) {

        double km = m / 1000.0;
        return km;
    }
}
