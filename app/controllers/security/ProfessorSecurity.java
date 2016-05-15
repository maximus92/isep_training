package controllers.security;

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
	public CompletionStage<Result> call(Context ctx){
		Logger.info("Calling action for " + ctx);
		if(checkStudent(ctx)){
			return delegate.call(ctx);
		}else{
			return CompletableFuture.completedFuture(redirect("/student"));
		} 
	}
	
	public static boolean checkStudent(Context ctx) {
        String token = ctx.session().get("token");
        User u = User.getUserByToken(token);
				if(u.getIsprof() == 0){
					return false;
				}else{
					return true;
				}
      }
}
