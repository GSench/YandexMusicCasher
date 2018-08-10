package ru.yandexmusiccasher.domain.usecase;

import ru.yandexmusiccasher.domain.model.MusicFileCash;
import ru.yandexmusiccasher.presentation.presenter.DownloadCompletePresenter;

/**
 * Created by Григорий Сенченок on 21.07.2018.
 */

public interface DownloadCompleteUseCase {
    public void fileDownloaded(MusicFileCash fileCash, DownloadCompletePresenter presenter);
}
