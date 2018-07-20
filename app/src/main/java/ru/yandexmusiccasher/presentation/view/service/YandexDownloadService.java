package ru.yandexmusiccasher.presentation.view.service;

import android.content.Intent;

import ru.yandexmusiccasher.R;
import ru.yandexmusiccasher.domain.utils.HttpParams;
import ru.yandexmusiccasher.presentation.AndroidInterface;
import ru.yandexmusiccasher.presentation.presenter.UrlReceiverPresenter;
import ru.yandexmusiccasher.presentation.utils.IntentService;
import ru.yandexmusiccasher.presentation.utils.ToastService;
import ru.yandexmusiccasher.presentation.view.DownloadServiceView;
import ru.yandexmusiccasher.presentation.view.activity.YandexVerifyActivity;

/**
 * Created by GSench on 01.06.2016.
 */
public class YandexDownloadService extends IntentService implements DownloadServiceView {

    public static final String MUSIC_CASH = "music_cash";

    String baseUrl;

    private UrlReceiverPresenter presenter;

    public YandexDownloadService() {
        super(null);
        presenter = new UrlReceiverPresenter(new AndroidInterface(this));
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        baseUrl = intent.getStringExtra(Intent.EXTRA_TEXT);
        presenter.urlReceived(baseUrl);
    }


    @Override
    public void openYandexCaptcha(HttpParams httpParams) {
        breakQueue();
        startActivity(new Intent(this, YandexVerifyActivity.class).putExtra(Intent.EXTRA_TEXT, baseUrl).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
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
}
