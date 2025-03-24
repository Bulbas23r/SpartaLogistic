package common;

public class UserContextHolder {
  private static final ThreadLocal<String> authorizationHolder = new ThreadLocal<>();
  private static final ThreadLocal<String> userHolder = new ThreadLocal<>();
  private static final ThreadLocal<String> roleHolder = new ThreadLocal<>();

  public static void setCurrentUser(String authorization, String user, String role) {
    authorizationHolder.set(authorization);
    userHolder.set(user);
    roleHolder.set(role);
  }

  public static String getAuthorization() {
    return authorizationHolder.get();
  }

  public static String getUser() {
    return userHolder.get();
  }

  public static String getRole() {
    return roleHolder.get();
  }

  public static void clear() {
    authorizationHolder.remove();
    userHolder.remove();
    roleHolder.remove();
  }
}
