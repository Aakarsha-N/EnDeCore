public class Session {
    private static String currentUser;
    public static void setCurrentUser(String username) {
        currentUser = username;
    }
    public static String getCurrentUser() {
        return currentUser == null ? "guest" : currentUser;
    }
    public static boolean isLoggedIn() {
        return currentUser != null;
    }
    public static void logout() {
        currentUser = null;
    }
}