package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * @author Julien Ripault <tluapir@gmail.com>
 */
@Entity
public class Favorite extends Model
{
  @OneToOne
  public Member member;
}
