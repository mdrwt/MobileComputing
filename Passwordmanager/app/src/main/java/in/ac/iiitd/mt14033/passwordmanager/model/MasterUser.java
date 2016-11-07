package in.ac.iiitd.mt14033.passwordmanager.model;

/**
 * Created by anshika on 10/22/16.
 */

public class MasterUser {
    String email;
    String password;
    String help;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public MasterUser(String email, String password, String help) {
        this.email = email;
        this.password = password;
        this.help = help;
    }
}
