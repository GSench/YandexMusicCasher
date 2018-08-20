package ru.yandexmusiccasher.presentation.view.service;

import android.content.Intent;

import ru.yandexmusiccasher.R;
import ru.yandexmusiccasher.domain.interactor.DownloadCompleteInteractor;
import ru.yandexmusiccasher.domain.model.MusicFileCash;
import ru.yandexmusiccasher.domain.services.HttpParams;
import ru.yandexmusiccasher.presentation.AndroidInterface;
import ru.yandexmusiccasher.presentation.model.AMSOperations;
import ru.yandexmusiccasher.presentation.model.AMusicFileCash;
import ru.yandexmusiccasher.presentation.presenter.UrlReceiverPresenterImpl;
import ru.yandexmusiccasher.presentation.utils.IntentService;
import ru.yandexmusiccasher.presentation.utils.ToastService;
import ru.yandexmusiccasher.presentation.view.DownloadServiceView;
import ru.yandexmusiccasher.presentation.view.activity.YandexVerifyActivity;

/**
 * Created by GSench on 01.06.2016.
 */
public class YandexDownloadService extends IntentService implements DownloadServiceView {

    public static final String DOWNLOAD_PLAY_STRATEGY = "strategy";

    String baseUrl;
    int strategy;

    private UrlReceiverPresenterImpl presenter;

    public YandexDownloadService() {
        super(null);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(presenter==null){
            presenter = new UrlReceiverPresenterImpl(new AndroidInterface(this), new AMSOperations(this));
            presenter.setView(this);
        }
        baseUrl = intent.getStringExtra(Intent.EXTRA_TEXT);
        strategy = intent.getIntExtra(DOWNLOAD_PLAY_STRATEGY, DownloadCompleteInteractor.DOWNLOAD_PLAY);
        presenter.urlReceived(baseUrl, strategy);
    }


    @Override
    public void openYandexCaptcha(HttpParams httpParams) {
        breakQueue();
        startActivity(new Intent(this, YandexVerifyActivity.class)
                .putExtra(Intent.EXTRA_TEXT, baseUrl)
                .putExtra(DOWNLOAD_PLAY_STRATEGY, strategy)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Override
    public void showInternetError() {
        ToastService.show(getString(R.string.internet_error), this);
    }

    @Override
    public void showParseError() {
        ToastService.show(getString(R.string.parse_error), this);
    }

    @Override
    public void showIndefinitePathError() {
        ToastService.show(getString(R.string.indef_path), this);
    }

    @Override
    public void continueWithoutDownloading(MusicFileCash musicFileCash) {
        AMusicFileCash aMusicFileCash = (AMusicFileCash) musicFileCash;
        Intent downloadCompleteIntent = new Intent(this, DownloadCompleteService.class)
                .putExtra(DownloadCompleteService.MUSIC_FILE_EXTRA, aMusicFileCash.getFullPath());
        startService(downloadCompleteIntent);
    }

}
