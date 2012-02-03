package controllers.admin;

import controllers.CRUD;
import controllers.Check;
import controllers.Secure;
import models.GoogleAccount;
import models.Role;
import play.mvc.With;

/**
 * @author Julien Ripault <tluapir@gmail.com>
 */
@Check({Role.ADMIN_MEMBER})
@With(Secure.class)
@CRUD.For(GoogleAccount.class)
public class GoogleAccountsAdmin extends CRUD
{
}
