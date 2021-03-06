package ru.yandexmusiccasher.presentation.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.apache.commons.io.FilenameUtils;

import java.io.ByteArrayInputStream;
import java.net.URL;

import ru.yandexmusiccasher.R;
import ru.yandexmusiccasher.domain.interactor.DownloadCompleteInteractor;
import ru.yandexmusiccasher.domain.services.HttpParams;
import ru.yandexmusiccasher.domain.services.Network;
import ru.yandexmusiccasher.domain.utils.Pair;
import ru.yandexmusiccasher.presentation.AndroidInterface;
import ru.yandexmusiccasher.presentation.view.service.YandexDownloadService;

/**
 * Created by GSench on 03.06.2016.
 */
public class YandexVerifyActivity extends AppCompatActivity {

    private String uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yandex_verify);
        uri = getIntent().getStringExtra(Intent.EXTRA_TEXT);

        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);

        final AndroidInterface system = new AndroidInterface(this);
        final Network network = new Network(system);

        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {

                WebResourceResponse response = null;
                String ext = FilenameUtils.getExtension(url);
                String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext.equals("") ? "html" : ext);
                try{
                    Pair<byte[], HttpParams> rawResponse = system.httpGet(new URL(url), network.getInitialHttpParams());
                    response = new WebResourceResponse(mime, "utf-8", new ByteArrayInputStream(rawResponse.f));
                } catch (Exception e){
                    e.printStackTrace();
                }
                return response==null ? super.shouldInterceptRequest(view, url) : response;
            }
        });
        webView.loadUrl(uri);
        Toast.makeText(this, getString(R.string.yandex_verify), Toast.LENGTH_LONG).show();
    }

    public void onEnterClick(View v){
        exit();
    }

    private void exit(){
        Intent intent = new Intent(YandexVerifyActivity.this, YandexDownloadService.class);
        intent.putExtra(Intent.EXTRA_TEXT, uri);
        intent.putExtra(YandexDownloadService.DOWNLOAD_PLAY_STRATEGY, getIntent().getIntExtra(YandexDownloadService.DOWNLOAD_PLAY_STRATEGY, DownloadCompleteInteractor.DOWNLOAD_PLAY));
        startService(intent);
        finish();
    }
}
