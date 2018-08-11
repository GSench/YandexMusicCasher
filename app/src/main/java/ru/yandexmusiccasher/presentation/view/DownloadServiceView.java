package ru.yandexmusiccasher.presentation.view;

import ru.yandexmusiccasher.domain.services.HttpParams;

/**
 * Created by grish on 20.07.2018.
 */

public interface DownloadServiceView {

    public void openYandexCaptcha(HttpParams httpParams);
    public void showInternetError();
    public void showParseError();
    public void showIndefinitePathError();
}
