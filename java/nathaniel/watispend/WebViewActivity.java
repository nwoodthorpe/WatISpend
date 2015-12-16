package nathaniel.watispend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class WebViewActivity extends AppCompatActivity {
    String EXTRA_title = "";
    String EXTRA_domain = "";

    private void setTitleNDomain(){
        WebView webview = (WebView) findViewById(R.id.webView);
        TextView title = (TextView) findViewById(R.id.title);

        webview.setWebViewClient(new WebViewClient());
        webview.setVerticalScrollBarEnabled(true);
        webview.setHorizontalScrollBarEnabled(true);
        webview.getSettings().setBuiltInZoomControls(true);

        webview.loadUrl(EXTRA_domain);
        title.setText(EXTRA_title);
    }

    private void setExtraParams(){
        Intent intent = getIntent();
        EXTRA_title = intent.getStringExtra("title");
        EXTRA_domain = intent.getStringExtra("domain");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        setExtraParams();
        setTitleNDomain();
    }
}
