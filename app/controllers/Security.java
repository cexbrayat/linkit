/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;


import models.*;

/**
 * By default, the login page will accept any login/password.
 * To customize it application has to provide a Security provider which extend Secure.Security class
 * 
 * @author agnes007
 */
public class Security extends Secure.Security {
    
    public static boolean authenticate(String username, String password) {
        return Member.connect(username, password);
    }
}
   