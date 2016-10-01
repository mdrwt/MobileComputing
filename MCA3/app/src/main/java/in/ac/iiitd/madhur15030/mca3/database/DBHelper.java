package in.ac.iiitd.madhur15030.mca3.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;

import in.ac.iiitd.madhur15030.mca3.R;
import in.ac.iiitd.madhur15030.mca3.model.StudentRecord;


public class DBHelper {
    private static DBHelper ourInstance = new DBHelper();

    public static DBHelper getInstance() {
        return ourInstance;
    }

    private DBHelper() {
    }

    public long InsertStudentRecord(String rollno, String name, String currsem, Context context) {
        ReaderDBHelper readerDBHelper = new ReaderDBHelper(context);

        SQLiteDatabase db=null;
        try {
            db = readerDBHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        ContentValues values = new ContentValues();

        values.put(StudentRecordReaderContract.StudentRecordEntry.COLUMN_NAME_ROLL_NO, rollno);
        values.put(StudentRecordReaderContract.StudentRecordEntry.COLUMN_NAME_NAME, name);
        values.put(StudentRecordReaderContract.StudentRecordEntry.COLUMN_NAME_CURR_SEM, currsem);

        long newRowId;
        newRowId = db.insertWithOnConflict(
                StudentRecordReaderContract.StudentRecordEntry.TABLE_NAME,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);
        if(-1 == newRowId) throw new RuntimeException(context.getString(R.string.Exception_error_updating_record));
        return newRowId;

    }

    public StudentRecord getStudentRecord(String rollno, Context context) {
        ReaderDBHelper readerDBHelper = new ReaderDBHelper(context);

        SQLiteDatabase db=null;
        try {
            db = readerDBHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        String[] projection = {
                StudentRecordReaderContract.StudentRecordEntry._ID,
                StudentRecordReaderContract.StudentRecordEntry.COLUMN_NAME_ROLL_NO,
                StudentRecordReaderContract.StudentRecordEntry.COLUMN_NAME_NAME,
                StudentRecordReaderContract.StudentRecordEntry.COLUMN_NAME_CURR_SEM,
        };
        String where = StudentRecordReaderContract.StudentRecordEntry.COLUMN_NAME_ROLL_NO+"=\""+rollno+"\"";

        Cursor cursor=null;
        try {
            cursor = db.query(
                    StudentRecordReaderContract.StudentRecordEntry.TABLE_NAME,
                    projection,
                    where,
                    null,
                    null,
                    null,
                    null
            );
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        if(cursor==null)
            return null;

        cursor.moveToFirst();
        String rollnumber = cursor.getString(cursor.getColumnIndexOrThrow
                (StudentRecordReaderContract.StudentRecordEntry.COLUMN_NAME_ROLL_NO));
        String name = cursor.getString(cursor.getColumnIndexOrThrow
                (StudentRecordReaderContract.StudentRecordEntry.COLUMN_NAME_NAME));
        String currsem = cursor.getString(cursor.getColumnIndexOrThrow
                (StudentRecordReaderContract.StudentRecordEntry.COLUMN_NAME_CURR_SEM));
        return new StudentRecord(rollnumber, name, currsem);
    }

    public int deleteStudentRecord(String rollno, Context context) {
        ReaderDBHelper readerDBHelper = new ReaderDBHelper(context);

        SQLiteDatabase db=null;
        try {
            db = readerDBHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        String where = StudentRecordReaderContract.StudentRecordEntry.COLUMN_NAME_ROLL_NO + " LIKE ?";
//        String where = StudentRecordReaderContract.StudentRecordEntry.COLUMN_NAME_ROLL_NO+"=\""+rollno+"\"";
        String args[] = {rollno};
        try {
            int resid = db.delete(StudentRecordReaderContract.StudentRecordEntry.TABLE_NAME, where, args);
            return resid;
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public ArrayList<StudentRecord> getAllStudentRecord(Context context) {
        ReaderDBHelper readerDBHelper = new ReaderDBHelper(context);

        SQLiteDatabase db=null;
        try {
            db = readerDBHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        String[] projection = {
                StudentRecordReaderContract.StudentRecordEntry._ID,
                StudentRecordReaderContract.StudentRecordEntry.COLUMN_NAME_ROLL_NO,
                StudentRecordReaderContract.StudentRecordEntry.COLUMN_NAME_NAME,
                StudentRecordReaderContract.StudentRecordEntry.COLUMN_NAME_CURR_SEM,
        };
//        String where = SharedKeyReaderContract.SharedKeyEntry.COLUMN_NAME_USERNAME+"=\""+username+"\"";

        Cursor cursor=null;
        try {
            cursor = db.query(
                    StudentRecordReaderContract.StudentRecordEntry.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    null
            );
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        if(cursor==null)
            return null;

        ArrayList<StudentRecord> studentRecords=new ArrayList<>();
        while(cursor.moveToNext()) {

            String rollno = cursor.getString(cursor.getColumnIndexOrThrow
                    (StudentRecordReaderContract.StudentRecordEntry.COLUMN_NAME_ROLL_NO));
            String name = cursor.getString(cursor.getColumnIndexOrThrow
                    (StudentRecordReaderContract.StudentRecordEntry.COLUMN_NAME_NAME));
            String currsem = cursor.getString(cursor.getColumnIndexOrThrow
                    (StudentRecordReaderContract.StudentRecordEntry.COLUMN_NAME_CURR_SEM));
            studentRecords.add(new StudentRecord(rollno, name, currsem));
        }

        return studentRecords;
    }
}
