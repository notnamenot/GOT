package pl.edu.agh.wtm.got.models;

import androidx.annotation.NonNull;

public class MountainChain { // pasmo
    private int id;
    private String name;
    private int length; // km
    private int mountainRangeId; // łańcuch

    public MountainChain(int id, String name, int length, int mountainRangeId) {
        this.id = id;
        this.name = name;
        this.length = length;
        this.mountainRangeId = mountainRangeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }

    public int getMountainRangeId() {
        return mountainRangeId;
    }
}
