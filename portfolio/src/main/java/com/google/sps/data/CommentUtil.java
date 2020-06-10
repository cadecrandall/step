package com.google.sps.data;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import java.util.ArrayList;

public class CommentUtil {
  private CommentUtil() {
    throw new AssertionError();
  }

  /** Return true when a user is logged in */
  public static boolean checkLogin(UserService userServ) {
    boolean isLoggedIn = userServ.isUserLoggedIn();
    return isLoggedIn;
  }

  /** Return a json string from a generic ArrayList */
  public static <T> String convertToJson(ArrayList<T> list) {
    Gson jsonConverter = new Gson();
    String output = jsonConverter.toJson(list);
    return output;
  }
}