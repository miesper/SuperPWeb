package com.miguelesper.superpweb;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar
        setContentView(R.layout.activity_main);


        setContentView(R.layout.activity_main);
        String caminho = "https://superpweb.sistemasuperp.com.br/";
        WebView webView = findViewById(R.id.webview);
        ProgressBar progressBar = findViewById(R.id.progressBarEmpresa);
        progressBar.setVisibility(View.VISIBLE);


        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        webView.setWebViewClient(new WebViewClient()
        {
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                // all links  with in ur site will be open inside the webview
                //links that start ur domain example(http://www.example.com/)
                if (url != null && url.startsWith(caminho)){
                    return false;
                }
                // all links that points outside the site will be open in a normal android browser
                else
                {
                    view.getContext().startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                }
            }
        });

        webView.setWebViewClient(new WebViewClient()
                                 {
                                     @Override
                                     public void onPageFinished(WebView view, String url)
                                     {
                                         super.onPageFinished(view, url);
                                         progressBar.setVisibility(View.INVISIBLE);
                                     }
                                 }
        );

        webView.setDownloadListener((url, userAgent, contentDisposition, mimeType, contentLength) -> {

            DownloadManager.Request request = new DownloadManager.Request(
                    Uri.parse(url));
            request.setMimeType(mimeType);
            String cookies = CookieManager.getInstance().getCookie(url);
            request.addRequestHeader("cookie", cookies);
            request.addRequestHeader("User-Agent", userAgent);
            request.setDescription("Downloading file...");
            request.setTitle(URLUtil.guessFileName(url, contentDisposition,
                    mimeType));
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(
                            url, contentDisposition, mimeType));
            DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            dm.enqueue(request);
            Toast.makeText(getApplicationContext(), "Downloading File",
                    Toast.LENGTH_LONG).show();

        });

        webView.loadUrl(caminho);
    }
}