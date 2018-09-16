package ru.yandexmusiccasher.presentation.view;

import ru.yandexmusiccasher.domain.model.MusicFileCash;

/**
 * Created by grish on 20.07.2018.
 */

public interface DownloadServiceView {

    public void openYandexCaptcha();
    public void showInternetError();
    public void showParseError();
    public void showIndefinitePathError();
    public void continueWithoutDownloading(MusicFileCash musicFileCash);
}
