package in.ac.iiitd.mt14033.passwordmanager.model;

import java.io.Serializable;

/**
 * Created by Madhur on 09/11/16.
 */
public class UserProfile implements Serializable {

    private String email;


    public UserProfile(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
