package in.flatlet.www.Flatlet.recyclerView.ViewsAccountingSQLiteHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


class FeedReaderViewsAccountingDbHelper extends SQLiteOpenHelper {
    private static final String TAG = "ViewsAccountingDBHelper";
    // If you change the database schema, you must increment the database version.

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS" + FeedReaderViewsAccounting.FeedEntryViewsAccounting.TABLE_NAME +
                    " (" +
                    FeedReaderViewsAccounting.FeedEntryViewsAccounting._ID + " INTEGER PRIMARY KEY,"
                    + FeedReaderViewsAccounting.FeedEntryViewsAccounting.COLUMN_NAME_TITLE + "TEXT," +
                    FeedReaderViewsAccounting.FeedEntryViewsAccounting.COLUMN_NAME_VIEWS + "INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderViewsAccounting.FeedEntryViewsAccounting.TABLE_NAME;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FeedReader.db";


    public FeedReaderViewsAccountingDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "onCreate: of feed ");
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
