package models.mailing;

import java.util.List;
import models.Member;

/**
 *
 * @author Sryl <cyril.lacote@gmail.com>
 */
public interface MembersSetQuery {
    
    List<? extends Member> find();
}
