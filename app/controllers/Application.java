package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.*;

import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index() {
        render();
    }
    
    public static void register() {
    	render("Application/register.html");
    }

    public static void endRegistration(@Required String firstname, @Required String lastname, @Required @Email String email) {
     	Logger.info("firstname {" + firstname + "}, lastname {" + lastname + "}, email {" + email + "}");
	    if (validation.hasErrors()) {
	    	Logger.error(validation.errors().toString());
	    	render("Application/register.html");	    	
	    }
    	Member member = Member.find("byEmail", email).first();
    	if(member == null){
	    	member = new Member(firstname, lastname, email);
   	    	member.save();
   	    	flash.success("Profil enregistré!");
   	    	Logger.info("Profil enregistré");
    	}
    	render("Application/profile.html", member);
    }

}