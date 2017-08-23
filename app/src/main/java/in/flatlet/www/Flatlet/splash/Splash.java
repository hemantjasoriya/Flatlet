package in.flatlet.www.Flatlet.splash;

        import android.content.Intent;
        import android.os.Bundle;
        import android.os.Handler;
        import android.support.annotation.Nullable;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.widget.ImageView;

        import in.flatlet.www.Flatlet.Home.FirstActivity;
        import in.flatlet.www.Flatlet.R;


public class Splash extends AppCompatActivity {
    ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        imageView=(ImageView)findViewById(R.id.imageView);
        Log.i("MainActivity", "onCreate: image loaded");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.setImageResource(R.drawable.splash);
                Intent intent = new Intent(Splash.this,FirstActivity.class);
                intent.setFlags(1);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

}