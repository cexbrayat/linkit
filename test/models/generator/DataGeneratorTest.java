package models.generator;

import java.util.Set;
import models.BaseDataUnitTest;
import models.Member;
import org.junit.Test;

/**
 * Unit tests for {@link DataGenerator}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class DataGeneratorTest extends BaseDataUnitTest {
    
    @Test
    public void createMembers() {
        final int nbMembers = 10;
        Set<Member> members = DataGenerator.createMembers(nbMembers);
        assertNotNull(members);
        assertEquals(nbMembers, members.size());
    }
    
    @Test
    public void generateLinks() {
        final int averageLinksPerMember = 3;
        DataGenerator.generateLinks(averageLinksPerMember);
    }
    
    @Test
    public void generateSessionComments() {
        final int averageCommentsPerMember = 3;
        DataGenerator.generateSessionComments(averageCommentsPerMember);
    }
    
    @Test
    public void generateArticleComments() {
        final int averageCommentsPerMember = 3;
        DataGenerator.generateArticleComments(averageCommentsPerMember);
    }
}
