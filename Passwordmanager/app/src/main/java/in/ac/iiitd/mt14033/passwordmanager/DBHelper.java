package in.ac.iiitd.mt14033.passwordmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jarvisx on 10/2/2016.
 */

public class DBHelper extends SQLiteOpenHelper {


    static final String TAG = "MyAccessibilityService";
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 11;

    // Database Name
    private static final String DATABASE_NAME = "PasswordManager.sqlitedb";

    // Passwords table name
    private static final String TABLE_PASSWORDS = "passwords";

    // Passwords Table Columns names
    protected static final String KEY_ID = "_id";
    protected static final String KEY_USERID = "userid";
    protected static final String KEY_URL = "url";
    protected static final String KEY_PASSWORD = "password";
    protected static final String KEY_SAVE_ALLOWED = "saveallowed";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PASSWORD_TABLE = "CREATE TABLE " + TABLE_PASSWORDS + "( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_USERID + " TEXT," + KEY_URL + " TEXT," + KEY_PASSWORD + " TEXT,"+KEY_SAVE_ALLOWED+" INTEGER)";
        db.execSQL(CREATE_PASSWORD_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PASSWORDS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new password
    boolean addPassword(PasswordManager pm) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERID, pm.getUserId());
        values.put(KEY_URL, pm.getUrl());
        values.put(KEY_PASSWORD, pm.getPassword());
        values.put(KEY_SAVE_ALLOWED, 1); // if adding password assuming that wants to save password

        // Inserting Row
        long ret = db.insert(TABLE_PASSWORDS, null, values);
        db.close(); // Closing database connection
        return (ret!=-1);
    }

    // Getting single password
    PasswordManager getPassword(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PASSWORDS, new String[] { KEY_ID,
                        KEY_USERID, KEY_URL, KEY_PASSWORD, KEY_SAVE_ALLOWED }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        PasswordManager pm = new PasswordManager(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));

        Log.d(TAG,"UserId1: "+pm.getUserId());
        Log.d(TAG,"Url1: "+pm.getUrl());
        Log.d(TAG,"Password1: "+pm.getPassword());
        return pm;
    }

    PasswordManager getPasswordForUsernameAndPackage(String username, String packagename) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = new String[] { KEY_ID, KEY_USERID, KEY_URL, KEY_PASSWORD, KEY_SAVE_ALLOWED};
        String where = KEY_USERID+"=\""+username+"\" AND "
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
            return null;
        PasswordManager pm = new PasswordManager(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));

        return pm;
    }

    int isPasswordSaveAllowed(String username, String packagename) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = new String[] { KEY_ID, KEY_USERID, KEY_URL, KEY_PASSWORD, KEY_SAVE_ALLOWED};
        String where = KEY_USERID+"=\""+username+"\" AND "
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

    // Getting All passwords
    public Cursor getAllPasswords() {
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_PASSWORDS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // return password list cursor
        return cursor;
    }

    // Updating single password
    public int updatePassword(PasswordManager pm) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERID, pm.getUserId());
        values.put(KEY_URL, pm.getUrl());
        values.put(KEY_PASSWORD, pm.getPassword());

        // updating row
        return db.update(TABLE_PASSWORDS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(pm.getID()) });
    }

    // Deleting single password
    public void deletePassword(PasswordManager pm) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PASSWORDS, KEY_ID + " = ?",
                new String[] { String.valueOf(pm.getID()) });
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
