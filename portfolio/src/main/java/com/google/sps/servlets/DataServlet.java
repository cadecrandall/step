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

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import com.google.gson.Gson;


@WebServlet("/data")
public class DataServlet extends HttpServlet {

  public ArrayList<String> comments = new ArrayList<>();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String json = convertToJson(comments);
    // Send the JSON as the response
    response.setContentType("text/html;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String newComment = parseForm(request);
    comments.add(newComment);
    // Return user to portfolio page after comment is posted
    response.sendRedirect("/portfolio.html");   
  }


  private String convertToJson(ArrayList<String> messageList) {
    Gson jsonConverter = new Gson();
    String output = jsonConverter.toJson(messageList);
    return output;
  }


  // TODO: return an object or ArrayList of all 3 fields from form
  private String parseForm(HttpServletRequest request) {
    String message = request.getParameter("comment-message");
    return message;
  }
}
