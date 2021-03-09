package ctrl;

public class User {
    private int ID;
    private String login, name, email, passwd;

    public User(){
        ID      =0;
        login   ="-undefined-";
        name    ="-undefined-";
        email   ="-undefined-";
        passwd  ="";
    }
    public User(String login, String name, String email, String passwd){
        ID = -1;
        this.login = login;
        this.name = name;
        this.email = email;
        this.passwd = passwd;
    }

    public int getID() {
        return ID;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    @Override
    public String toString() {
        return "Can't obtain data this way";
    }
    public String toStringForTesting() {
        final String fmt = "[user]: %3d %-10s %-28s %s";
        return String.format(fmt, ID, login, name, email);
    }
}
