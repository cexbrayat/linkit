package controllers.admin;

import controllers.CRUD;
import controllers.Check;
import controllers.SecureLinkIt;
import models.Role;
import models.TwitterAccount;
import play.mvc.With;

/**
 * @author Julien Ripault <tluapir@gmail.com>
 */
@Check({Role.ADMIN_MEMBER})
@With(SecureLinkIt.class)
@CRUD.For(TwitterAccount.class)
public class TwitterAccountsAdmin extends CRUD
{
}
