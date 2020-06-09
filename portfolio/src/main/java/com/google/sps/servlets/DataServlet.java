// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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
import java.util.*;
import com.google.gson.Gson;
import com.google.sps.data.Comment;


@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private static final String CONTENT_TYPE = "text/html;";
  private static final String REDIRECT_LINK = "/portfolio.html";
  private static final String NUM_COMMENTS_PROPERTY = "numComments";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query(Comment.COMMENT_ENTITY).addSort(Comment.TIMESTAMP_PROPERTY, SortDirection.DESCENDING);

    int numComments = Integer.parseInt(request.getParameter(NUM_COMMENTS_PROPERTY));

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    ArrayList<Comment> comments = new ArrayList<>();

    int counter = 0;
    for (Entity entity : results.asIterable()) {
      if (counter == numComments) {
        break;
      }
      Comment currentComment = new Comment(entity);
      comments.add(currentComment);
      counter++;
    }

    String json = convertToJson(comments);
    response.setContentType(CONTENT_TYPE);
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    if (checkLogin(userService)) {
      Entity commentEntity = parseForm(request);
      // add user email to the entity
      commentEntity.setProperty(Comment.EMAIL_PROPERTY, userService.getCurrentUser().getEmail());

      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      datastore.put(commentEntity);

      // Return user to portfolio page after comment is posted
      response.sendRedirect(REDIRECT_LINK);  
    } else {
      // return that the comment post failed?
      response.sendRedirect(REDIRECT_LINK);
    }    
  }


  private String convertToJson(ArrayList<Comment> messageList) {
    Gson jsonConverter = new Gson();
    String output = jsonConverter.toJson(messageList);
    return output;
  }

  private Entity parseForm(HttpServletRequest request) {
    return new Comment(request).toEntity();
  }

  /** Check login status before allowing comment to post */
  private boolean checkLogin(UserService userService) {
    boolean isLoggedIn = userService.isUserLoggedIn();
    return isLoggedIn;
  }
}
