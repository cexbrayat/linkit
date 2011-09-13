package models;
 
import java.util.*;
import javax.persistence.*;
 
import play.db.jpa.*;
 
@Entity
public class Member extends Model {
 
    public String email;
    public String firstname;
    public String lastname;
    public String description;
    
    public Member(String firstname, String lastname, String email, String description) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.description = description;
    }
 
}