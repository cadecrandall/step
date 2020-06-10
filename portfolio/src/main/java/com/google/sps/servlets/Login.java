package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.PrintWriter;
import java.util.*;
import com.google.sps.data.Comment;
import com.google.sps.data.CommentUtil;



@WebServlet("/login") 
public class Login extends HttpServlet {
  private static final String CONTENT_TYPE = "text/html;";
  private static final String REDIRECT_LINK = "/portfolio.html";
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType(CONTENT_TYPE);
    PrintWriter out = response.getWriter();
    ArrayList<String> output = new ArrayList<>();
    UserService userService = UserServiceFactory.getUserService();

    
    if (CommentUtil.checkLogin(userService)) {
      String redirect = userService.createLogoutURL(REDIRECT_LINK);
      output.add("true");
      output.add(redirect);
    } else {
      String redirect = userService.createLoginURL(REDIRECT_LINK);
      output.add("false");
      output.add(redirect);
    } 
    out.println(CommentUtil.convertToJson(output));
  }
}