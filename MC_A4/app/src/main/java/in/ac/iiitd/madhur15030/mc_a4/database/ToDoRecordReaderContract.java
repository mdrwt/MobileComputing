package in.ac.iiitd.madhur15030.mc_a4.database;

import android.provider.BaseColumns;

/**
 * Created by Madhur on 19/05/16.
 */
public final class ToDoRecordReaderContract {
    public ToDoRecordReaderContract() {
    }

    public static abstract class ToDoRecordEntry implements BaseColumns {
        public static final String TABLE_NAME = "todo";
        public static final String COLUMN_NAME_INDEX = "index";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DETAIL = "detail";

    }
}
