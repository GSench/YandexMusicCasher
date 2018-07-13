package ru.yandexmusiccasher.domain.usecase;

import ru.yandexmusiccasher.presentation.presenter.UrlReceiverPresenter;

/**
 * Created by grish on 08.07.2018.
 */

public interface UrlReceivedUseCase {

    public void downloadTrackByUrl(String link, UrlReceiverPresenter presenter);
}
