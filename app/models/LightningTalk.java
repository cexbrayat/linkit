package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * @author Julien Ripault <tluapir@gmail.com>
 */
@Entity
public class LightningTalk extends Session
{
  @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
  public List<Vote> votes;
}
