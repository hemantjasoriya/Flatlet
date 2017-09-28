package in.flatlet.www.Flatlet.welcome;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import in.flatlet.www.Flatlet.Home.fragments.profilefragment.CreateProfileFragment;
import in.flatlet.www.Flatlet.R;

public class LoginActivity extends AppCompatActivity {
    Fragment fragment;
    FragmentTransaction fragmentTransaction;

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton("no", null)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory(Intent.CATEGORY_HOME);
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                        finish();
                    }
                }).create().show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        int flag=getIntent().getFlags();
        if (flag==0){
        fragment = new in.flatlet.www.Flatlet.Home.fragments.profilefragment.LoginFragment();
       }
        else {
            fragment=new CreateProfileFragment();

        }
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.login_relative, fragment, "fragmentHome");
        fragmentTransaction.commit();
    }
}
