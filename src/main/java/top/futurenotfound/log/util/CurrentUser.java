package top.futurenotfound.log.util;

public class CurrentUser {
    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    private CurrentUser() {
    }

    public static String get() {
        String username = threadLocal.get();
        threadLocal.remove();
        return username;
    }

    public static void set(String username) {
        threadLocal.set(username);
    }
}
