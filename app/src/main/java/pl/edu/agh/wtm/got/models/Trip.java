package pl.edu.agh.wtm.got.models;

import androidx.annotation.NonNull;

import java.util.List;

public class Trip {

    private int id;

    private String date;
    private int from;
    private int to;

    private double length;
    private int time;
    private int points;
    private int ups;
    private int downs;

    public Trip(int id, int from, int to, double length, int time, int points, int ups, int downs, String date) {
        this.id = id;
        this.date = date;
        this.from = from;
        this.to = to;
        this.length = length;
        this.time = time;
        this.points = points;
        this.ups = ups;
        this.downs = downs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getUps() {
        return ups;
    }

    public void setUps(int ups) {
        this.ups = ups;
    }

    public int getDowns() {
        return downs;
    }

    public void setDowns(int downs) {
        this.downs = downs;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }


    @Override
    public String toString() {
        return "Trip{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", from=" + from +
                ", to=" + to +
                ", length=" + length +
                ", time=" + time +
                ", points=" + points +
                ", ups=" + ups +
                ", downs=" + downs +
                '}';
    }
}
