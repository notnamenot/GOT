package pl.edu.agh.wtm.got.models;

import androidx.annotation.NonNull;

import java.util.List;

public class Route {

    private int id;
    private double length;
    private int time;
    private int points;
    private int ups;
    private int downs;
    private List<Subroute> subroutes;
    private List<GOTPoint> gotPoints;

    public Route(int id, int points, double length, int time, int sumUps, int sumDowns) {
        this.id = id;
        this.length = length;
        this.time = time;
        this.points = points;
        this.ups = sumUps;
        this.downs = sumDowns;
    }

    public Route(int id, int points, double length, int time, int sumUps, int sumDowns,List<GOTPoint> gotPoints) {
        this.id = id;
        this.length = length;
        this.time = time;
        this.points = points;
        this.ups = sumUps;
        this.downs = sumDowns;
        this.gotPoints = gotPoints;
    }

    public Route(int id, int points, double length, int time, int sumUps, int sumDowns,List<GOTPoint> gotPoints,List<Subroute> subroutes) {
        this.id = id;
        this.length = length;
        this.time = time;
        this.points = points;
        this.ups = sumUps;
        this.downs = sumDowns;
        this.gotPoints = gotPoints;
        this.subroutes = subroutes;
    }

    public Route(){};

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

    public List<Subroute> getSubroutes() {
        return subroutes;
    }

    public void setSubroutes(List<Subroute> subroutes) {
        this.subroutes = subroutes;
    }

    public List<GOTPoint> getGotPoints() {
        return gotPoints;
    }

    public void setGotPoints(List<GOTPoint> gotPoints) {
        this.gotPoints = gotPoints;
    }

    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", length=" + length +
                ", time=" + time +
                ", points=" + points +
                ", ups=" + ups +
                ", downs=" + downs +
                ", subroutes=" + subroutes +
                ", gotPoints=" + gotPoints +
                '}';
    }
}
