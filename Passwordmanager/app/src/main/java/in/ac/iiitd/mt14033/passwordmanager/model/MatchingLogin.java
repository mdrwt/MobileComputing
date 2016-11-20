package in.ac.iiitd.mt14033.passwordmanager.model;

/**
 * Created by Madhur on 09/11/16.
 */

public class MatchingLogin {
    private String name;
    private String username;
    private String packagename;
    private String password;

    public MatchingLogin(String password, String name, String username, String packagename) {
        this.password = password;
        this.name = name;
        this.username = username;
        this.packagename = packagename;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
