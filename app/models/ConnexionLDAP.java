package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.UUID;

import play.mvc.*;
import views.html.*;
import play.data.*;
import play.db.*;

import javax.inject.Inject;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import play.Logger;

public class ConnexionLDAP{
	
	String serverIP = "ldap.isep.fr";
    String serverPort = "389";
    
	
	public User connectLDAP(DynamicForm form){
		String pwd = form.get("pwd");
		String login = form.get("login");
		
		String serverLogin = "uid=" + login + ", " + "ou=People, dc=isep.fr";
        Hashtable environnement = new Hashtable();
        environnement.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        environnement.put(Context.PROVIDER_URL, "ldap://"+serverIP+":"+serverPort+"/");
        environnement.put(Context.SECURITY_AUTHENTICATION, "simple");
        environnement.put(Context.SECURITY_PRINCIPAL, serverLogin);
        environnement.put(Context.SECURITY_CREDENTIALS, pwd);
        try {
        	// Try to connect to LDAP
            DirContext contexte = new InitialDirContext(environnement);
            try {
            	Attributes attrs = contexte.getAttributes(serverLogin);
            	User u = attributeLDAP(attrs,login);           	
            	return u;
            } catch (NamingException e) {
                e.printStackTrace();
                return null;
            }
        } catch (NamingException e) {
            e.printStackTrace();
            return null;
        }
	}
	
	public User attributeLDAP(Attributes attrs,String login){
		//Try to get datas
        String emp = attrs.get("employeeType").toString().replaceAll("^(employeeType: )", "");
        String ln = attrs.get("sn").toString().replaceAll("^(sn: )", "");
        String fn = attrs.get("givenname").toString().replaceAll("^(givenName: )", "");
        String uuID = UUID.randomUUID().toString();
        //pwd = BCrypt.hashpw(pwd, BCrypt.gensalt());
        int isprof = isProf(emp);
        User u = new User(login,fn,ln,isprof,uuID);
        return  u;
	}
	
	public int isProf(String emp){
		if(emp.equals("eleve")){
			return 0;
		}else{
			return 1;
		}
	}
	
	
	public User connect(DynamicForm form){
		String pwd = form.get("pwd");
		String login = form.get("login");
		//pwd = BCrypt.hashpw(pwd, BCrypt.gensalt());
		
		  Connection conn = null;
	      ResultSet rs=null;
	      PreparedStatement stmt=null;
	      User u = null;
	      String fn = null;
	      String ln=null;
	     String token = null;
	      int isprof=0;
	      try{
	    	  conn = DB.getConnection();
	          stmt = conn.prepareStatement("SELECT * FROM user WHERE username='mabboud'");
	          rs = stmt.executeQuery();
	          while(rs.next()){
	        	  ln = rs.getString("lastname");
	        	  fn = rs.getString("firstname");
	        	  token = rs.getString("token");
	        	  u = new User(login,fn,ln,isprof,token);
	          }
	          stmt.close();
	          return u;
	      }catch(SQLException e){
	    	  e.printStackTrace();
	       return u;
	      }finally{
	    	  if(conn != null){
	    		  try{
	    			 conn.close(); 
	    		  }catch(SQLException ignore){
	    			  ignore.printStackTrace();
	    		  }
	    	  }
	      }
	}
}
