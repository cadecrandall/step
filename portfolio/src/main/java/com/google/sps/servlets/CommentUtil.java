import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;


public class CommentUtil {
  private LoginUtil() {
    throw new AssertionError();
  }

  /** Return true when a user is logged in */
  public static boolean checkLogin(UserService userServ) {
    boolean isLoggedIn = userServ.isUserLoggedIn();
    return isLoggedIn;
  }

  /** Return a json string from a generic ArrayList */
  public static String convertToJson(ArrayList<T> list) {
    Gson jsonConverter = new Gson();
    String output = jsonConverter.toJson(list);
    return output;
  }
}