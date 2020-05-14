package pl.edu.agh.wtm.got.models;

import java.util.List;

public class Route {
    private List<Subroute> subroutes;
    private List<GOTPoint> gotPoints;
    private int id;
    private double length;
    private int time;
    private int points;
    private int sumUps;
    private int sumDowns;

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

    public int getSumUps() {
        return sumUps;
    }

    public void setSumUps(int sumUps) {
        this.sumUps = sumUps;
    }

    public int getSumDowns() {
        return sumDowns;
    }

    public void setSumDowns(int sumDowns) {
        this.sumDowns = sumDowns;
    }

    public Route(int id, int points, double length, int time, int sumUps, int sumDowns) {
        this.id = id;
        this.length = length;
        this.time = time;
        this.points = points;
        this.sumUps = sumUps;
        this.sumDowns = sumDowns;
        System.out.println("creating route");
    }
}
