package controllers;

import play.jobs.*;
import play.test.*;
import models.*;
import models.generator.DataGenerator;
import play.Play;

@OnApplicationStart
public class PopulateOnStart extends Job {

    @Override
    public void doJob() {
        // Check if we are not in test mode
        if (!"test".equals(Play.id)) {
            // Check if the database is empty
            if(Member.count() == 0) {
                Fixtures.deleteAllModels();
                Fixtures.loadModels("init-data.yml");
                
                // Dummy data
                int nbDummyMembers = Integer.valueOf(Play.configuration.getProperty("dummy.members"));
                if (nbDummyMembers > 0) {
                    DataGenerator.createMembers(nbDummyMembers);
                }
                int nbAverageLinksPerMember = Integer.valueOf(Play.configuration.getProperty("dummy.averageLinksPerMember"));
                if (nbAverageLinksPerMember > 0) {
                    DataGenerator.generateLinks(nbAverageLinksPerMember);
                }
                int nbAverageCommentsPerMember = Integer.valueOf(Play.configuration.getProperty("dummy.averageCommentsPerMember"));
                if (nbAverageCommentsPerMember > 0) {
                    DataGenerator.generateSessionComments(nbAverageCommentsPerMember);
                    DataGenerator.generateArticleComments(nbAverageCommentsPerMember);
                }
            }
        }
    }
}