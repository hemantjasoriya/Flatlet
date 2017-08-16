package in.flatlet.www.Flatlet.recyclerView;

import android.provider.BaseColumns;

/**
 * Created by javax on 31-Jul-17.
 */

public final class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "favourite";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SECONDARY_ADDRESS = "address_secondary";
        public static final String COLUMN_NAME_RENT="rent";
        public static final String COLUMN_NAME_IMG_URL="imgUrl";
        public static final String COLUMN_NAME_RATING="rating";

    }
}

