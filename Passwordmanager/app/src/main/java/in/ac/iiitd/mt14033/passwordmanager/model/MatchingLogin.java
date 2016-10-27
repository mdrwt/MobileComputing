package in.ac.iiitd.mt14033.passwordmanager.model;

/**
 * Created by devashish on 10/25/16.
 */

public class MatchingLogin {
    private String username;
    private String packagename;
    private String password;

    public MatchingLogin(String username, String packagename, String password) {
        this.username = username;
        this.packagename = packagename;
        this.password = password;
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
