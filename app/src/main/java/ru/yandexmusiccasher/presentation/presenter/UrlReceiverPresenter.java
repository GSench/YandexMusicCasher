package ru.yandexmusiccasher.presentation.presenter;

import android.content.Context;

import ru.yandexmusiccasher.domain.usecase.UrlReceivedUseCase;

/**
 * Created by grish on 08.07.2018.
 */

public class UrlReceiverPresenter {

    private UrlReceivedUseCase useCase;
    private Context context;

    public UrlReceiverPresenter(UrlReceivedUseCase useCase){
        this.useCase=useCase;
    }

    public void start(){

    }

    public void onYandexCaptcha() {

    }

    public void onInternetError() {

    }

    public void onParseError() {

    }

    public void onUndefinedPath() {

    }
}
