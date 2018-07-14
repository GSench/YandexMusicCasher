package ru.yandexmusiccasher.presentation.view.service;

import android.content.Intent;

import ru.yandexmusiccasher.presentation.utils.IntentService;

/**
 * Created by GSench on 01.06.2016.
 */
public class YandexDownloadService extends IntentService {

    public static final String MUSIC_CASH = "music_cash";

    String baseUrl;

    public YandexDownloadService() {
        super(null);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        baseUrl = intent.getStringExtra(Intent.EXTRA_TEXT);
    }


}
