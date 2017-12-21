package com.example.haider.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.http.SslError;
import android.os.Build;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
private AlertDialog builder;
private FrameLayout mContainer;
private Toast mToast;
private long mLastBackPressTime = 0;
private Bundle savedWebviewInstanceState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mBtnGoogle = (Button) findViewById(R.id.button);
        mWebview = (WebView) findViewById(R.id.webview);

        WebSettings webSettings = mWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setJavaScriptEnabled(true);
        //webSettings.setAllowContentAccess(true);
        //webSettings.setAllowFileAccess(true);
        //webSettings.setDatabaseEnabled(true);

        mWebview.getSettings().setSavePassword(true);
        mWebview.getSettings().setSaveFormData(true);
        mWebview.setWebViewClient(new UriWebViewClient());
        mWebview.setWebChromeClient(new UriChromeClient());
        mWebview.getSettings().setSavePassword(true);


        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(mWebview,true);
        }





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
            mWebviewPopTwo.getSettings().setSavePassword(true);
            mWebviewPopTwo.getSettings().setSaveFormData(true);
            //mWebviewPopTwo.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            // create an AlertDialog.Builder
            //the below did not give me .dismiss() method . See : https://stackoverflow.com/questions/14853325/how-to-dismiss-alertdialog-in-android

//            AlertDialog.Builder builder;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
//            } else {
//                builder = new AlertDialog.Builder(MainActivity.this);
//            }

            // set the WebView as the AlertDialog.Builder’s view

            builder = new AlertDialog.Builder(MainActivity.this).create();


            builder.setTitle("");
            builder.setView(mWebviewPopTwo);

            builder.setButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    mWebviewPopTwo.destroy();
                    dialog.dismiss();


                }
            });



            builder.show();





            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cookieManager.setAcceptThirdPartyCookies(mWebviewPopTwo,true);
            }



            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(mWebviewPopTwo);
            resultMsg.sendToTarget();

            return true;
        }


        @Override
        public void onCloseWindow(WebView window) {

            //Toast.makeText(mContext,"onCloseWindow called",Toast.LENGTH_SHORT).show();


            try {
                mWebviewPopTwo.destroy();
            } catch (Exception e) {

            }

            try {
                builder.dismiss();

            } catch (Exception e) {

            }


        }

    }


}
