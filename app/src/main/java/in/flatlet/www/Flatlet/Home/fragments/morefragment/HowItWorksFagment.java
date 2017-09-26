package in.flatlet.www.Flatlet.Home.fragments.morefragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import in.flatlet.www.Flatlet.R;

/**
 * Created by Dragon on 30-07-2017.
 */

public class HowItWorksFagment extends Fragment {
    private ImageView i1, i2, i3, i4, i5, i6, i7, i8, i9, i10;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.howitworks_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        i1 = (ImageView) getActivity().findViewById(R.id.image1);
        i2 = (ImageView) getActivity().findViewById(R.id.image2);
        i3 = (ImageView) getActivity().findViewById(R.id.image3);
        i4 = (ImageView) getActivity().findViewById(R.id.image4);
        i5 = (ImageView) getActivity().findViewById(R.id.image5);
        i6 = (ImageView) getActivity().findViewById(R.id.image6);
        i7 = (ImageView) getActivity().findViewById(R.id.image7);
        i8 = (ImageView) getActivity().findViewById(R.id.image8);
        i9 = (ImageView) getActivity().findViewById(R.id.image9);
        i10 = (ImageView) getActivity().findViewById(R.id.image10);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoreFragment moreFragment = new MoreFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content, moreFragment);
                fragmentTransaction.commit();
            }
        });

        Picasso.with(getContext()).load("http://images.flatlet.in/tutorial/1.webp").into(i1);
        Picasso.with(getContext()).load("http://images.flatlet.in/tutorial/2.webp").into(i2);
        Picasso.with(getContext()).load("http://images.flatlet.in/tutorial/3.webp").into(i3);
        Picasso.with(getContext()).load("http://images.flatlet.in/tutorial/4.webp").into(i4);
        Picasso.with(getContext()).load("http://images.flatlet.in/tutorial/5.webp").into(i5);
        Picasso.with(getContext()).load("http://images.flatlet.in/tutorial/6.webp").into(i6);
        Picasso.with(getContext()).load("http://images.flatlet.in/tutorial/7.webp").into(i7);
        Picasso.with(getContext()).load("http://images.flatlet.in/tutorial/8.webp").into(i8);
        Picasso.with(getContext()).load("http://images.flatlet.in/tutorial/9.webp").into(i9);
        Picasso.with(getContext()).load("http://images.flatlet.in/tutorial/10.webp").into(i10);












    }
}
