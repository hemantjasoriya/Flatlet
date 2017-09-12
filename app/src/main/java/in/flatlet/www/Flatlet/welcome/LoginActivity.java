package in.flatlet.www.Flatlet.welcome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import in.flatlet.www.Flatlet.Home.fragments.profilefragment.LoginFragment;
import in.flatlet.www.Flatlet.R;

public class LoginActivity extends AppCompatActivity {
    Fragment fragment;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fragment = new LoginFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.login_relative, fragment, "fragmentHome");
        fragmentTransaction.commit();
    }
}
