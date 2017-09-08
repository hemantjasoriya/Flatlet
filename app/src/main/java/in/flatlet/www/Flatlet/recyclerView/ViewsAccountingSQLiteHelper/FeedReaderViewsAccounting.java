package in.flatlet.www.Flatlet.recyclerView.ViewsAccountingSQLiteHelper;

import android.provider.BaseColumns;



class FeedReaderViewsAccounting {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FeedReaderViewsAccounting (){

    }

    //Inner class that defines the table contents//
    public static class FeedEntryViewsAccounting implements BaseColumns{

        public static final String TABLE_NAME = "ViewsAccounting";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_VIEWS = "views";
    }
}
