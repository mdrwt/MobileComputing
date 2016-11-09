package in.ac.iiitd.madhur15030.mc_a4.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;

import in.ac.iiitd.madhur15030.mc_a4.R;
import in.ac.iiitd.madhur15030.mc_a4.ToDo;


public class DBHelper {
    private static DBHelper ourInstance = new DBHelper();

    public static DBHelper getInstance() {
        return ourInstance;
    }

    private DBHelper() {
    }

    public long insertToDoRecord(String title, String detail, Context context) {
        ReaderDBHelper readerDBHelper = new ReaderDBHelper(context);

        SQLiteDatabase db=null;
        try {
            db = readerDBHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        ContentValues values = new ContentValues();

        values.put(ToDoRecordReaderContract.ToDoRecordEntry.COLUMN_NAME_TITLE, title);
        values.put(ToDoRecordReaderContract.ToDoRecordEntry.COLUMN_NAME_DETAIL, detail);

        long newRowId;
        newRowId = db.insertWithOnConflict(
                ToDoRecordReaderContract.ToDoRecordEntry.TABLE_NAME,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);
        if(-1 == newRowId) throw new RuntimeException(context.getString(R.string.Exception_error_updating_record));
        return newRowId;

    }

    public ToDo getToDoRecord(String title, Context context) {
        ReaderDBHelper readerDBHelper = new ReaderDBHelper(context);

        SQLiteDatabase db=null;
        try {
            db = readerDBHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        String[] projection = {
                ToDoRecordReaderContract.ToDoRecordEntry._ID,
                ToDoRecordReaderContract.ToDoRecordEntry.COLUMN_NAME_TITLE,
                ToDoRecordReaderContract.ToDoRecordEntry.COLUMN_NAME_DETAIL
        };
        String where = ToDoRecordReaderContract.ToDoRecordEntry.COLUMN_NAME_TITLE+"=\""+title+"\"";

        Cursor cursor=null;
        try {
            cursor = db.query(
                    ToDoRecordReaderContract.ToDoRecordEntry.TABLE_NAME,
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
        String mTitle = cursor.getString(cursor.getColumnIndexOrThrow
                (ToDoRecordReaderContract.ToDoRecordEntry.COLUMN_NAME_TITLE));
        String detail = cursor.getString(cursor.getColumnIndexOrThrow
                (ToDoRecordReaderContract.ToDoRecordEntry.COLUMN_NAME_DETAIL));
        return new ToDo(mTitle, detail);
    }

    public boolean deleteToDoRecord(String title, Context context) {
        ReaderDBHelper readerDBHelper = new ReaderDBHelper(context);

        SQLiteDatabase db=null;
        try {
            db = readerDBHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        String where = ToDoRecordReaderContract.ToDoRecordEntry.COLUMN_NAME_TITLE + " LIKE ?";
        String args[] = {title};
        try {
            db.delete(ToDoRecordReaderContract.ToDoRecordEntry.TABLE_NAME, where, args);
            return true;
        } catch (SQLiteException e) {
            return false;
        }

    }

    public ArrayList<ToDo> getAllToDoRecord(Context context) {
        ReaderDBHelper readerDBHelper = new ReaderDBHelper(context);

        SQLiteDatabase db=null;
        try {
            db = readerDBHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        String[] projection = {
                ToDoRecordReaderContract.ToDoRecordEntry._ID,
                ToDoRecordReaderContract.ToDoRecordEntry.COLUMN_NAME_TITLE,
                ToDoRecordReaderContract.ToDoRecordEntry.COLUMN_NAME_DETAIL,
        };

        Cursor cursor=null;
        try {
            cursor = db.query(
                    ToDoRecordReaderContract.ToDoRecordEntry.TABLE_NAME,
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

        ArrayList<ToDo> toDos=new ArrayList<>();
        while(cursor.moveToNext()) {

            String title = cursor.getString(cursor.getColumnIndexOrThrow
                    (ToDoRecordReaderContract.ToDoRecordEntry.COLUMN_NAME_TITLE));
            String detail = cursor.getString(cursor.getColumnIndexOrThrow
                    (ToDoRecordReaderContract.ToDoRecordEntry.COLUMN_NAME_DETAIL));
            toDos.add(new ToDo(title, detail));
        }

        return toDos;
    }
    public ToDo getToDoRecordWithIndex(int index, Context context) {
        ReaderDBHelper readerDBHelper = new ReaderDBHelper(context);

        SQLiteDatabase db=null;
        try {
            db = readerDBHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        String[] projection = {
                ToDoRecordReaderContract.ToDoRecordEntry._ID,
                ToDoRecordReaderContract.ToDoRecordEntry.COLUMN_NAME_TITLE,
                ToDoRecordReaderContract.ToDoRecordEntry.COLUMN_NAME_DETAIL,
        };

        Cursor cursor=null;
        try {
            cursor = db.query(
                    ToDoRecordReaderContract.ToDoRecordEntry.TABLE_NAME,
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


        int count =0;
        ToDo toDo=null;
        while(cursor.moveToNext() && (count++)<=index) {

            String title = cursor.getString(cursor.getColumnIndexOrThrow
                    (ToDoRecordReaderContract.ToDoRecordEntry.COLUMN_NAME_TITLE));
            String detail = cursor.getString(cursor.getColumnIndexOrThrow
                    (ToDoRecordReaderContract.ToDoRecordEntry.COLUMN_NAME_DETAIL));
            toDo = new ToDo(title, detail);
        }

        return toDo;
    }
}
