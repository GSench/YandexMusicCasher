package ru.yandexmusiccasher.domain.presenters;

import ru.yandexmusiccasher.domain.utils.HttpParams;

/**
 * Created by grish on 11.08.2018.
 */

public interface UrlReceiverPresenter {

    public void onUndefinedPath();
    public void onYandexCaptcha(HttpParams httpParams);
    public void onInternetError();
    public void onParseError();

}
