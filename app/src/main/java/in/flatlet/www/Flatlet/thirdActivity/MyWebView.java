package in.flatlet.www.Flatlet.thirdActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import in.flatlet.www.Flatlet.R;


public class MyWebView extends Fragment {
    private String TAG ="MyWebViewFragment";
    private WebView webView;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_webview_third, container, false);
         webView = (WebView) view.findViewById(R.id.webView);
        progressBar = (ProgressBar) view.findViewById(R.id.progres_bar);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String hostel_title = getActivity().getIntent().getStringExtra("hostel_title");
        Log.i(TAG, "onActivityCreated hostel_title received is :"+hostel_title);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("http://images.flatlet.in/images/"+hostel_title+"/_html5/MRF.html");
        progressBar.setVisibility(ProgressBar.GONE);
        webView.setWebViewClient(new WebViewClient());

    }
}