package in.flatlet.www.Flatlet.Home.fragments.homefragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import in.flatlet.www.Flatlet.R;


public class ExploreVirtualTour extends AppCompatActivity {
    private ProgressBar progressBar;
    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        String hostel_title = getIntent().getStringExtra("hostel_title");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explore_virtual_tour);
        Toolbar toolbar = findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.progresBar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("360° Virtual Tour");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        WebView.setWebContentsDebuggingEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Toast.makeText(getApplicationContext(), "Can't Load the live tour", Toast.LENGTH_SHORT).show();
            }
        });
        webView.loadUrl("http://images.flatlet.in/images/" + hostel_title + "/_html5/MRF.html");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
