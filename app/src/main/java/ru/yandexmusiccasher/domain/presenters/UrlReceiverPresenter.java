package ru.yandexmusiccasher.domain.presenters;

import ru.yandexmusiccasher.domain.model.MusicFileCash;
import ru.yandexmusiccasher.domain.services.HttpParams;

/**
 * Created by grish on 11.08.2018.
 */

public interface UrlReceiverPresenter {

    public void onUndefinedPath();
    public void onYandexCaptcha(HttpParams httpParams);
    public void onInternetError();
    public void onParseError();
    public void onMusicAlreadyInCash(MusicFileCash musicFileCash);
}
