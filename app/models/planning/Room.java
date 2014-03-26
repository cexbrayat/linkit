package models.planning;

public enum Room {

    A1("Grand Amphi"), //used by mixit14
    A2("Petit Amphi"), //used by mixit14
    S1("Gosling"), //used by mixit13, mixit14
    S2("Eich"), //used by mixit13, mixit14
    S3("Nonaka"), //used by mixit13, mixit14
    S4("Dijkstra"), //used by mixit13, mixit14
    S5("Turing"); //used by mixit13

    private String name;

    private Room(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
