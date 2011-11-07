package controllers.admin;

import controllers.CRUD;
import controllers.Check;
import controllers.Secure;
import models.Interest;
import models.Role;
import play.mvc.With;

/**
 * @author Julien Ripault <tluapir@gmail.com>
 */
@Check({Role.ADMIN_MEMBER, Role.ADMIN_PLANNING, Role.ADMIN_SESSION, Role.ADMIN_SPEAKER})
@With(Secure.class)
@CRUD.For(Interest.class)
public class InterestsAdmin extends CRUD
{
}
