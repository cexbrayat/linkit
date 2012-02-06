package controllers.admin;

import controllers.CRUD;
import controllers.Check;
import controllers.SecureLinkIt;
import models.Role;
import models.Session;
import play.mvc.With;

/**
 * @author Julien Ripault <tluapir@gmail.com>
 */
@Check({Role.ADMIN_MEMBER, Role.ADMIN_PLANNING, Role.ADMIN_SESSION, Role.ADMIN_SPEAKER})
@With(SecureLinkIt.class)
@CRUD.For(Session.class)
public class SessionsAdmin extends CRUD
{
}
