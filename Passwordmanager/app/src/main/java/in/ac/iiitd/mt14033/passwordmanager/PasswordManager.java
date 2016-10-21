package in.ac.iiitd.mt14033.passwordmanager;

/**
 * Created by jarvisx on 10/2/2016.
 */

public class PasswordManager {

    //private variables
    int _id;
    String _user_id;
    String _url;
    String _password;

    // Empty constructor
    public PasswordManager(){

    }
    // constructor
    public PasswordManager(int id, String user_id, String url, String password){
        this._id = id;
        this._user_id = user_id;
        this._url = url;
        this._password = password;
    }

    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting user_id
    public String getUserId(){
        return this._user_id;
    }

    // setting user_id
    public void setUserId(String userId){
        this._user_id = userId;
    }

    // getting url
    public String getUrl(){
        return this._url;
    }

    // setting url
    public void setUrl(String url){
        this._url = url;
    }

    // getting password
    public String getPassword(){
        return this._password;
    }

    // setting Password
    public void setPassword(String password){
        this._password = password;
    }
}
