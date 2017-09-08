package in.flatlet.www.Flatlet.reviewhostel;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by javax on 31-Jul-17.
 */

class FeedReaderDbHelperReviewHostel extends SQLiteOpenHelper {
    private static final String TAG ="FeedReaderDbHelperReviewHostel";
    // If you change the database schema, you must increment the database version.
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedReaderContractReviewHostel.FeedEntry.TABLE_NAME + " (" +
                    FeedReaderContractReviewHostel.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContractReviewHostel.FeedEntry.COLUMN_NAME_TITLE + " TEXT)";/*+
                    FeedReaderContractReviewHostel.FeedEntry.COLUMN_NAME_SUBTITLE + " TEXT)";*/

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContractReviewHostel.FeedEntry.TABLE_NAME;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FeedReader.db";

    public FeedReaderDbHelperReviewHostel(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i(TAG, "FeedReaderDbHelperReviewHostel: ");
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
