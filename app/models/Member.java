package models;
 
import java.util.*;
import javax.persistence.*;
 
import play.db.jpa.*;
 
@Entity
public class Member extends Model {
 
    public String email;
    public String firstname;
    public String lastname;
    
    public Member(String firstname, String lastname, String email) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
    }
 
}