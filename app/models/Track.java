package models;

import play.db.jpa.Model;



public class Track extends Model {

    public String name;
    public String color;
    public String rgb;
    
    private Track(String name) {
        this.name = name;
    }
    public String toString() {
        return name;
    }
}
