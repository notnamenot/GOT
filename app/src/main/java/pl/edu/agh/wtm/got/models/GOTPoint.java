package pl.edu.agh.wtm.got.models;

public class GOTPoint {
    private int id;
    private String name;
    private int height; // m
    private int mountainChainId; // pasmo górskei

    public GOTPoint(int id, String name, int height, int mountainChainId) {
        this.id = id;
        this.name = name;
        this.height = height;
        this.mountainChainId = mountainChainId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getHeight() {
        return height;
    }

    public int getMountainChainId() {
        return mountainChainId;
    }



    @Override
    public String toString() {
        return "GOTPoint{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", height=" + height +
                ", mountainRangeId=" + mountainChainId +
                '}';
    }
}
