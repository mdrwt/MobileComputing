package in.ac.iiitd.mt14033.passwordmanager.model;

/**
 * Created by jarvisx on 10/2/2016.
 */

public class PasswordManager {

    //private variables
    int _id;
    String _name;
    String _user_id;
    String _url;
    String _password;

    // Empty constructor
    public PasswordManager(){

    }

    public PasswordManager(int _id, String _name, String _user_id, String _url, String _password) {
        this._id = _id;
        this._name = _name;
        this._user_id = _user_id;
        this._url = _url;
        this._password = _password;
    }


    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
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
