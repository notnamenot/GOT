package pl.edu.agh.wtm.got.models;

public class Subroute {
    private int id;
    private int from;
    private int to;
    private int points;
    private double length;
    private int time;
    private int ups;
    private int downs;

    public Subroute(int id, int from, int to, int points, double length, int time, int sum_ups, int sum_downs) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.points = points;
        this.length = length;
        this.time = time;
        this.ups = sum_ups;
        this.downs = sum_downs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
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
}
