package controllers;

import play.jobs.*;
import play.test.*;
import models.*;
import play.Play;

@OnApplicationStart
public class PopulateOnStart extends Job {

    @Override
    public void doJob() {
        // Check if we are not it test mode
        if (!"test".equals(Play.id)) {
            // Check if the database is empty
            if(Member.count() == 0) {
                Fixtures.loadModels("init-data.yml");
            }
        }
    }
}