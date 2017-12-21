package com.example.haider.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.http.SslError;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

private Context mContext;
private Button mBtnGoogle;
private WebView mWebview;

private WebView mWebviewPopTwo;
private FrameLayout mContainer;
private Toast mToast;
private long mLastBackPressTime = 0;
private Bundle savedWebviewInstanceState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        mBtnGoogle = (Button) findViewById(R.id.button);
        mWebview = (WebView) findViewById(R.id.webview);
        mContainer = (FrameLayout) findViewById(R.id.webview_frame);
        WebSettings webSettings = mWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        mWebview.setWebViewClient(new UriWebViewClient());
        mWebview.setWebChromeClient(new UriChromeClient());
        mWebview.getSettings().setSavePassword(true);






        //mWebview.loadUrl(target_url);

        mContext=this.getApplicationContext();



    }



    public void onBtnClick(View view) {

        // Go to Google
        mWebview.loadUrl("http://www.google.com");
    }


    private class UriWebViewClient extends WebViewClient {

        /*
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String host = Uri.parse(url).getHost();
            //Log.d("shouldOverrideUrlLoading", url);
            if (host.equals(target_url_prefix))
            {
                // This is my web site, so do not override; let my WebView load
                // the page
                if(mWebviewPop!=null)
                {
                    mWebviewPop.setVisibility(View.GONE);
                    mContainer.removeView(mWebviewPop);
                    mWebviewPop=null;
                }
                return false;
            }

            if(host.equals("m.facebook.com"))
            {
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch
            // another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }

        */



        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {
            Log.d("onReceivedSslError", "onReceivedSslError");
            //super.onReceivedSslError(view, handler, error);
        }
    }

    class UriChromeClient extends WebChromeClient {

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog,
                                      boolean isUserGesture, Message resultMsg) {
            mWebviewPopTwo = new WebView(mContext);
            mWebviewPopTwo.setVerticalScrollBarEnabled(false);
            mWebviewPopTwo.setHorizontalScrollBarEnabled(false);
            mWebviewPopTwo.setWebViewClient(new UriWebViewClient());
            mWebviewPopTwo.setWebChromeClient(new UriChromeClient());
            mWebviewPopTwo.getSettings().setJavaScriptEnabled(true);
            mWebviewPopTwo.getSettings().setSavePassword(false);
            mWebviewPopTwo.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mWebview.setVisibility(View.GONE);
            mBtnGoogle.setVisibility(View.GONE);
            mContainer.addView(mWebviewPopTwo);

            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(mWebviewPopTwo);
            resultMsg.sendToTarget();

            return true;
        }

        @Override
        public void onCloseWindow(WebView window) {

            Toast.makeText(mContext,"onCloseWindow called",Toast.LENGTH_SHORT).show();
            mWebviewPopTwo.destroy();
            mBtnGoogle.setVisibility(View.VISIBLE);
            mWebview.setVisibility(View.VISIBLE);

            Log.d("onCloseWindow", "called");
        }

    }


}
