package models.planning;

public enum Room {

    S1("Gosling"),
    S2("Eich"),
    S3("Nonaka"),
    S4("Dijkstra"),
    S5("Turing");

    private String name;

    private Room(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
