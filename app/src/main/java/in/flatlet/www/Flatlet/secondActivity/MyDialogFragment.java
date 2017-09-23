package in.flatlet.www.Flatlet.secondActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import in.flatlet.www.Flatlet.R;

/**
 * Created by javax on 22-Sep-17.
 */

public class MyDialogFragment extends DialogFragment {
    private float hostel_rating_food,hostel_rating_accommodation,hostel_rating_study,hostel_rating_staff;
    private RatingBar ratingBarFoodFrag, ratingBarStaffFrag, ratingBarAccommodationFrag, ratingBarStudyEnvironmentFrag;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment, container, false);
        getDialog().setTitle("DialogFragment Tutorial");
        hostel_rating_food=getArguments().getFloat("hostel_rating_food");
        hostel_rating_accommodation=getArguments().getFloat("hostel_rating_accommodation");
        hostel_rating_study=getArguments().getFloat("hostel_rating_study");
        hostel_rating_staff=getArguments().getFloat("hostel_rating_staff");
        ratingBarFoodFrag = (RatingBar)view.findViewById(R.id.ratingBarFoodFrag);
        ratingBarStaffFrag = (RatingBar)view.findViewById(R.id.ratingBarStaffFrag);
        ratingBarAccommodationFrag = (RatingBar)view.findViewById(R.id.ratingBarAccommodationFrag);
        ratingBarStudyEnvironmentFrag = (RatingBar)view.findViewById(R.id.ratingBarStudyEnvironmentFrag);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ratingBarFoodFrag.setRating(hostel_rating_food);
        ratingBarAccommodationFrag.setRating(hostel_rating_accommodation);
        ratingBarStaffFrag.setRating(hostel_rating_staff);
        ratingBarStudyEnvironmentFrag.setRating(hostel_rating_study);
        ratingBarFoodFrag.setIsIndicator(true);
        ratingBarAccommodationFrag.setIsIndicator(true);
        ratingBarStaffFrag.setIsIndicator(true);
        ratingBarStudyEnvironmentFrag.setIsIndicator(true);



    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
