package dao;

import android.provider.BaseColumns;

/**
 * Created by liyang on 11/29/17.
 */
public final class DBContract {


        // To prevent someone from accidentally instantiating the contract class,
        // make the constructor private.
        private DBContract() {}

        /* Inner class that defines the table contents */
        public static class FileEntry implements BaseColumns {
            public static final String TABLE_NAME = "entry";
            public static final String COLUMN_NAME = "name";
            public static final String COLUMN_STATUS = "status";
        }


}
