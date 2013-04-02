package models.planning;

public enum Room {

    S1("1 (100p)"),
    S2("2 (100p)"),
    S3("3 (80p)"),
    S4("4 (60p)"),
    S5("5 (25p)");

    private String name;

    private Room(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
