package in.flatlet.www.Flatlet.reviewhostel;

import android.provider.BaseColumns;

/**
 * Created by javax on 31-Jul-17.
 */

final class FeedReaderContractReviewHostel {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FeedReaderContractReviewHostel() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }
}

