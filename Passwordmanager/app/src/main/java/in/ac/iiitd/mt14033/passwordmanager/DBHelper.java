package in.ac.iiitd.mt14033.passwordmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import in.ac.iiitd.mt14033.passwordmanager.model.MasterUser;
import in.ac.iiitd.mt14033.passwordmanager.model.MatchingLogin;
import in.ac.iiitd.mt14033.passwordmanager.model.SavedPassword;

/**
 * Created by jarvisx on 10/2/2016.
 */

public class DBHelper extends SQLiteOpenHelper {


    static final String TAG = "MyAccessibilityService";
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 11;

    // Database Name
    private static final String DATABASE_NAME = "SavedPassword.sqlitedb";

    // Passwords table name
    private static final String TABLE_PASSWORDS = "passwords";

    private static final String TABLE_MASTER = "mastertable";


    // Passwords Table Columns names
    protected static final String KEY_ID = "_id";
    protected static final String KEY_NAME = "name";
    protected static final String KEY_USERNAME = "username";
    protected static final String KEY_URL = "url";
    protected static final String KEY_PASSWORD = "password";
    protected static final String KEY_SAVE_ALLOWED = "saveallowed";
    protected static final String KEY_MASTER_HELP = "masterhelp";
    protected static final String KEY_MASTER_EMAIL = "masteremail";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PASSWORD_TABLE = "CREATE TABLE " + TABLE_PASSWORDS + "( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_MASTER_EMAIL + " TEXT,"
                + KEY_USERNAME + " TEXT,"+ KEY_NAME + " TEXT," + KEY_URL + " TEXT," + KEY_PASSWORD + " TEXT,"
                +KEY_SAVE_ALLOWED+" INTEGER)";
        String CREATE_MASTER_TABLE = "CREATE TABLE " + TABLE_MASTER + "( " +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_MASTER_EMAIL +
                " TEXT," + KEY_PASSWORD + " TEXT,"+
                KEY_MASTER_HELP+" TEXT)";

        db.execSQL(CREATE_PASSWORD_TABLE);
        db.execSQL(CREATE_MASTER_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PASSWORDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new password
    boolean addPassword(SavedPassword pm) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MASTER_EMAIL, pm.getMaster_email());
        values.put(KEY_USERNAME, pm.getUsername());
        values.put(KEY_NAME, pm.getName());
        values.put(KEY_URL, pm.getUrl());
        values.put(KEY_PASSWORD, pm.getPassword());
        values.put(KEY_SAVE_ALLOWED, 1); // if adding password assuming that wants to save password

        // Inserting Row
        long ret = db.insert(TABLE_PASSWORDS, null, values);
        db.close(); // Closing database connection
        return (ret!=-1);
    }

    boolean addMasterPassword(MasterUser masterUser) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MASTER_EMAIL, masterUser.getEmail());
        values.put(KEY_PASSWORD, masterUser.getPassword());
        values.put(KEY_MASTER_HELP, masterUser.getHelp());

        // Inserting Row
        long ret = db.insert(TABLE_MASTER, null, values);
        db.close(); // Closing database connection
        return (ret!=-1);
    }

    MasterUser getMasterPassword(MasterUser masterUser) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = new String[] { KEY_ID, KEY_MASTER_EMAIL, KEY_PASSWORD, KEY_MASTER_HELP};
        String where = KEY_MASTER_EMAIL+"=\""+masterUser.getEmail()+"\"";
        Log.v(TAG, "where: "+where);
        Cursor cursor = db.query(
                TABLE_MASTER,
                projection,
                where,
                null,
                null,
                null,
                null);
        if (cursor != null && cursor.getCount()>0)
            cursor.moveToFirst();
        else
            return null;
        String email = cursor.getString(cursor.getColumnIndexOrThrow(KEY_MASTER_EMAIL));
        String password = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PASSWORD));
        String help = cursor.getString(cursor.getColumnIndexOrThrow(KEY_MASTER_HELP));
        MasterUser masterUser1 = new MasterUser(email, password, help);

        return masterUser1;
    }

    /**
     * Return true if matching email and password entry found
     * @param masterUser
     * @return boolean
     */
    boolean hasMasterUser(MasterUser masterUser) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = new String[] { KEY_ID, KEY_MASTER_EMAIL, KEY_PASSWORD, KEY_MASTER_HELP};
        String where = KEY_MASTER_EMAIL+"=\""+masterUser.getEmail()+"\" AND "
                + KEY_PASSWORD+"=\""+masterUser.getPassword()+"\"";
        Log.v(TAG, "where: "+where);
        Cursor cursor = db.query(
                TABLE_MASTER,
                projection,
                where,
                null,
                null,
                null,
                null);
        if (cursor != null && cursor.getCount()>0)
            return true;
        else
            return false;

    }

    // Getting single password
    SavedPassword getPassword(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PASSWORDS, new String[] { KEY_ID,
                        KEY_USERNAME, KEY_NAME, KEY_URL, KEY_PASSWORD, KEY_SAVE_ALLOWED }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null && cursor.getCount()>0)
            cursor.moveToFirst();
        else
            return null;
        int _id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID));
        String master_email = cursor.getString(cursor.getColumnIndexOrThrow(KEY_MASTER_EMAIL));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME));
        String password = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PASSWORD));
        String url = cursor.getString(cursor.getColumnIndexOrThrow(KEY_URL));
        String username = cursor.getString(cursor.getColumnIndexOrThrow(KEY_USERNAME));
        return new SavedPassword(_id, master_email, name, username, url, password);
    }

    /**
     * Get Matching password for given packagename. Used for Matching Logins
     * @param packagename
     * @return ArrayList
     */
    public ArrayList<MatchingLogin> getPasswordsForPackagename(String packagename, String master_email) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = new String[] { KEY_ID, KEY_MASTER_EMAIL, KEY_USERNAME, KEY_NAME, KEY_URL, KEY_PASSWORD, KEY_SAVE_ALLOWED};
        String where = KEY_URL+"=\""+packagename+"\"AND "
                + KEY_MASTER_EMAIL+"=\""+master_email+"\"";
        Log.v(TAG, "where: "+where);
        Cursor cursor = db.query(
                TABLE_PASSWORDS,
                projection,
                where,
                null,
                null,
                null,
                null);


        ArrayList<MatchingLogin> matchingLogins = new ArrayList<>();
        if (cursor == null)
            return matchingLogins;
        while(cursor.moveToNext()) {
            String username = cursor.getString(cursor.getColumnIndexOrThrow
                    (KEY_USERNAME));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME));
            String password = cursor.getString(cursor.getColumnIndexOrThrow
                    (KEY_PASSWORD));
            String packname = cursor.getString(cursor.getColumnIndexOrThrow
                    (KEY_URL));
            matchingLogins.add(new MatchingLogin(password, name, username, packname));
        }
        return  matchingLogins;
    }

    int isPasswordSaveAllowed(String username, String packagename) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = new String[] { KEY_ID, KEY_MASTER_EMAIL, KEY_USERNAME, KEY_URL, KEY_PASSWORD, KEY_SAVE_ALLOWED};
        String where = KEY_USERNAME+"=\""+username+"\" AND "
                + KEY_URL+"=\""+packagename+"\"";
        Log.v(TAG, "where: "+where);
        Cursor cursor = db.query(
                TABLE_PASSWORDS,
                projection,
                where,
                null,
                null,
                null,
                null);
        if (cursor != null && cursor.getCount()>0)
            cursor.moveToFirst();
        else
            return 1; // if no entry is found assume that this is first time and allow to save password

        return cursor.getInt(cursor.getColumnIndexOrThrow(KEY_SAVE_ALLOWED));
    }

    /**
     * Returns list of Saved Password in DB
     * @return ArrayList
     */
    public ArrayList<SavedPassword> getAllPasswords(String master_email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = new String[] { KEY_ID, KEY_MASTER_EMAIL, KEY_USERNAME, KEY_NAME, KEY_URL, KEY_PASSWORD, KEY_SAVE_ALLOWED};
        String where = KEY_MASTER_EMAIL+"=\""+master_email+"\"";
        Log.v(TAG, "where: "+where);
        Cursor cursor = db.query(
                TABLE_PASSWORDS,
                projection,
                null,
                null,
                null,
                null,
                null);
        ArrayList<SavedPassword> savedPasswords = new ArrayList<>();
        if (cursor == null)
            return savedPasswords;
        while(cursor.moveToNext()) {
            int _id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID));
            String mast_email = cursor.getString(cursor.getColumnIndexOrThrow(KEY_MASTER_EMAIL));
            String username = cursor.getString(cursor.getColumnIndexOrThrow
                    (KEY_USERNAME));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME));
            String password = cursor.getString(cursor.getColumnIndexOrThrow
                    (KEY_PASSWORD));
            String packname = cursor.getString(cursor.getColumnIndexOrThrow
                    (KEY_URL));
            savedPasswords.add(new SavedPassword(_id, mast_email, name, username, packname, password));
        }
        return  savedPasswords;
    }

    public int updatePassword(SavedPassword pm) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, pm.getId());
        values.put(KEY_URL, pm.getUrl());
        values.put(KEY_PASSWORD, pm.getPassword());

        // updating row
        return db.update(
                TABLE_PASSWORDS,
                values,
                KEY_ID + " = ?",
                new String[] { String.valueOf(pm.getId()) });
    }

    // Deleting single password
    public void deletePassword(SavedPassword pm) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PASSWORDS, KEY_ID + " = ?",
                new String[] { String.valueOf(pm.getId()) });
    }


    // Getting password Count
    public int getPasswordsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PASSWORDS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        // return count
        return cursor.getCount();
    }

}
