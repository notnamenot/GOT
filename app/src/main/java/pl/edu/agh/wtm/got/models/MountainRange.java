package pl.edu.agh.wtm.got.models;

public class MountainRange {
    private int id;
    private String name;
    private int length; // km
    private String peak;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }

    public String getPeak() {
        return peak;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public MountainRange(int id, String name, int length, String peak) {
        this.id = id;
        this.name = name;
        this.length = length;
        this.peak = peak;
    }


}
