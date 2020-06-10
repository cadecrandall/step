package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.sps.data.Comment;
import com.google.sps.data.CommentUtil;

/** Delete all the comments from the server */
@WebServlet("/delete-data") 
public class DeleteComments extends HttpServlet {
  private static final String CONTENT_TYPE = "text/html;";
  private static final String REDIRECT_LINK = "/portfolio.html";

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();    
    
    if (CommentUtil.checkLogin(userService)) {
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      Query query = new Query(Comment.COMMENT_ENTITY).addSort(Comment.TIMESTAMP_PROPERTY, SortDirection.DESCENDING);
      PreparedQuery results = datastore.prepare(query);

      for (Entity e : results.asIterable()) {
        datastore.delete(e.getKey());
      }
    }
    
    response.sendRedirect(REDIRECT_LINK);   
  }
}
