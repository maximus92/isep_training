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

public class ProfessorSecurity extends play.mvc.Action.Simple{

	@Override
	public CompletionStage<Result> call(Context ctx) {
		Logger.info("Calling action for " + ctx);
		try {
			if(checkStudent(ctx)){
				return delegate.call(ctx);
			}else{
				return CompletableFuture.completedFuture(redirect("/student"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; 
	}
	
	public static boolean checkStudent(Context ctx) throws SQLException  {
        String token = ctx.session().get("token");
        User u = User.getUserByToken(token);
				if(u.getIsProf() == 0){
					return false;
				}else{
					return true;
				}
      }
}
