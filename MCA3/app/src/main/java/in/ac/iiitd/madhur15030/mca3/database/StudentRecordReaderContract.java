package in.ac.iiitd.madhur15030.mca3.database;

import android.provider.BaseColumns;

/**
 * Created by Madhur on 19/05/16.
 */
public final class StudentRecordReaderContract {
    public StudentRecordReaderContract() {
    }

    public static abstract class StudentRecordEntry implements BaseColumns {
        public static final String TABLE_NAME = "student";
        public static final String COLUMN_NAME_ROLL_NO = "rollno";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_CURR_SEM = "currentsemester";

    }
}
