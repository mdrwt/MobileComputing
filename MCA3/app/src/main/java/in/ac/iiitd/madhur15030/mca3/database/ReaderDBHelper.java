package in.ac.iiitd.madhur15030.mca3.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Madhur on 19/05/16.
 */
public class ReaderDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "mcassignment3.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String BLOB_TYPE = " BLOB";
    private static final String COMMA_SEP = ",";
    private static final String PRIMARY_KEY = "PRIMARY KEY";
    private static final String UNIQUE = "UNIQUE";

    private static final String SQL_CREATE_ENTRIES_STUDENT_RECORD=
           "CREATE TABLE " + StudentRecordReaderContract.StudentRecordEntry.TABLE_NAME + " (" +
                   StudentRecordReaderContract.StudentRecordEntry._ID + " INTEGER ," +
                   StudentRecordReaderContract.StudentRecordEntry.COLUMN_NAME_ROLL_NO + TEXT_TYPE + COMMA_SEP +
                   StudentRecordReaderContract.StudentRecordEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                   StudentRecordReaderContract.StudentRecordEntry.COLUMN_NAME_CURR_SEM + TEXT_TYPE + COMMA_SEP +
                   PRIMARY_KEY+" ("+StudentRecordReaderContract.StudentRecordEntry.COLUMN_NAME_ROLL_NO+") ON CONFLICT REPLACE"+" )";

    private static final String SQL_DELETE_ENTRIES_STUDENT_RECORD =
            "DROP TABLE IF EXISTS " +
                    StudentRecordReaderContract.StudentRecordEntry.TABLE_NAME;

    public ReaderDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES_STUDENT_RECORD);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES_STUDENT_RECORD);
        onCreate(db);
    }
}
