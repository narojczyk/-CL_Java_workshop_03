package ctrl;

public class User {
    private int ID;
    private String login, userName, userEmail;

    public User(String login, String userName, String userEmail){
        ID = -1;
        this.login = login;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public int getID() {
        return ID;
    }

    public String getLogin() {
        return login;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        final String fmt = " Cannot obtain data this way";
        return fmt;
    }
    public String toStringForTesting() {
        final String fmt = " [user]: %3d %10s %s28s %s";
        return String.format(fmt, ID, login, userName, userEmail);
    }
}
