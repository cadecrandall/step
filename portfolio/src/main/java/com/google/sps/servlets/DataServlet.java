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
import java.util.*;
import com.google.gson.Gson;
import com.google.sps.data.Comment;


@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private static final String CONTENT_TYPE = "text/html;";
  private static final String REDIRECT_LINK = "/portfolio.html";

  private static final String COMMENT_FORM_ID = "comment-message";
  private static final String USERNAME_FORM_ID = "username";
  private static final String SUBJECT_FORM_ID = "subject";

  private static final String USERNAME_PROPERTY = "username";
  private static final String SUBJECT_PROPERTY = "subject";
  private static final String TIMESTAMP_PROPERTY = "timestamp";
  private static final String MESSAGE_PROPERTY = "message";

  private static final String COMMENT_ENTITY = "Comment";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query(COMMENT_ENTITY).addSort(TIMESTAMP_PROPERTY, SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    ArrayList<Comment> comments = new ArrayList<>();

    // TODO: constructor should take entity object
    for (Entity entity : results.asIterable()) {
      Comment currentComment = new Comment(entity);
      comments.add(currentComment);
    }

    String json = convertToJson(comments);
    response.setContentType(CONTENT_TYPE);
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Entity commentEntity = parseForm(request);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    // Return user to portfolio page after comment is posted
    response.sendRedirect(REDIRECT_LINK);   
  }


  private String convertToJson(ArrayList<Comment> messageList) {
    Gson jsonConverter = new Gson();
    String output = jsonConverter.toJson(messageList);
    return output;
  }

  private Entity parseForm(HttpServletRequest request) {
    return new Comment(request).toEntity();
  }
}
