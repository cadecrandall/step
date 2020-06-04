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


@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private static final String CONTENT_TYPE = "text/html;";
  private static final String REDIRECT_LINK = "/portfolio.html";
  private static final String COMMENT_FORM_ID = "comment-message";
  private static final String MESSAGE_PROPERTY = "message";
  private static final String TIMESTAMP_PROPERTY = "timestamp";
  private static final String COMMENT_ENTITY = "Comment";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query(COMMENT_ENTITY).addSort(TIMESTAMP_PROPERTY, SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    ArrayList<String> comments = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      String currentComment = (String) entity.getProperty(MESSAGE_PROPERTY;
      comments.add(currentComment);
    }

    String json = convertToJson(comments);
    response.setContentType(CONTENT_TYPE);
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String message = parseForm(request);
    long timestamp = System.currentTimeMillis();

    Entity commentEntity = new Entity(COMMENT_ENTITY);
    commentEntity.setProperty(MESSAGE_PROPERTY, message);
    commentEntity.setProperty(TIMESTAMP_PROPERTY, timestamp);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    // Return user to portfolio page after comment is posted
    response.sendRedirect(REDIRECT_LINK);   
  }


  private String convertToJson(ArrayList<String> messageList) {
    Gson jsonConverter = new Gson();
    String output = jsonConverter.toJson(messageList);
    return output;
  }


  // TODO: return an object or ArrayList of all 3 fields from form
  private String parseForm(HttpServletRequest request) {
    String message = request.getParameter(COMMENT_FORM_ID);
    return message;
  }
}
