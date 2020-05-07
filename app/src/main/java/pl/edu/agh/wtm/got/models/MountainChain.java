package pl.edu.agh.wtm.got.models;

public class MountainChain { // pasmo
    private int id;
    private String name;
    private int length; // km
    private int mountainRangeId; // łańcuch

    public MountainChain(String name, int length) {
        this.name = name;
        this.length = length;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
