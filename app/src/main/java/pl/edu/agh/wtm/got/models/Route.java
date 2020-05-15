package pl.edu.agh.wtm.got.models;

import java.util.List;

public class Route {
    public List<Subroute> getSubroutes() {
        return subroutes;
    }

    public void setSubroutes(List<Subroute> subroutes) {
        this.subroutes = subroutes;
    }

    private List<Subroute> subroutes;
    private List<GOTPoint> gotPoints;
    private int id;
    private double length;
    private int time;
    private int points;
    private int ups;
    private int downs;

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

    public Route(int id, int points, double length, int time, int sumUps, int sumDowns) {
        this.id = id;
        this.length = length;
        this.time = time;
        this.points = points;
        this.ups = sumUps;
        this.downs = sumDowns;
        System.out.println("creating route");
    }

    public Route(int id, int points, double length, int time, int sumUps, int sumDowns,List<GOTPoint> gotPoints) {
        this.id = id;
        this.length = length;
        this.time = time;
        this.points = points;
        this.ups = sumUps;
        this.downs = sumDowns;
        this.gotPoints = gotPoints;
        System.out.println("creating route");
    }

    public List<GOTPoint> getGOTPoints() {
        return gotPoints;
    }
}
