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
    public String login;
    public String password;
    
    public Member(String firstname, String lastname, String email, String description, String login, String password) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.description = description;
        this.login = login;
        this.password = password;
    }
    
    public static boolean connect(String login, String password){
    	Member member = Member.find("byLogin", login).first();
    	return (member != null && member.password.equals(password));
    }
 
}