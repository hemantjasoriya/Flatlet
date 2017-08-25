package in.flatlet.www.Flatlet.recyclerView;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;




public class FeedReaderDbHelper extends SQLiteOpenHelper {
    public static final String TAG = "FeedReaderDbHelper";
    // If you change the database schema, you must increment the database version.
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME + " (" +
                    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " TEXT," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_SECONDARY_ADDRESS + " TEXT," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_RENT + " INTEGER," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_IMG_URL + " TEXT," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_RATING + " REAL)";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i(TAG, "FeedReaderDbHelper: ");
    }

    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "onCreate: of feed ");
        /*db.execSQL(SQL_CREATE_ENTRIES);*/

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void onCreateOriginal(SQLiteDatabase db) {
        Log.i(TAG, "onCreate: of feed ");
        db.execSQL(SQL_CREATE_ENTRIES);

    }
}
