package controllers.security;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import models.User;
import play.mvc.Http.Context;
import play.Logger;
import play.db.Database;
import play.mvc.Result;

public class StudentSecurity extends play.mvc.Action.Simple{

	@Override
	public CompletionStage<Result> call(Context ctx){
		Logger.info("Calling action for " + ctx);
		try {
			if(checkStudent(ctx)){
				return delegate.call(ctx);
			}else{
				return CompletableFuture.completedFuture(redirect("/prof"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null; 
	}
	
	public static boolean checkStudent(Context ctx) throws SQLException {
        String token = ctx.session().get("token");
        User u = User.getUserByToken(token);
				if(u.getIsProf() == 1){
					return false;
				}else{
					return true;
				}
      }
}
