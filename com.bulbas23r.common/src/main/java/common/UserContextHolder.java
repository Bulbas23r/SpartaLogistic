package common;

public class UserContextHolder {
  private static final ThreadLocal<String> currentUser = new ThreadLocal<>();

  // 현재 사용자 설정
  public static void setCurrentUser(String user) {
    currentUser.set(user);
  }

  // 현재 사용자 조회
  public static String getCurrentUser() {
    return currentUser.get();
  }

  // ThreadLocal 값 초기화
  public static void clear() {
    currentUser.remove();
  }
}
