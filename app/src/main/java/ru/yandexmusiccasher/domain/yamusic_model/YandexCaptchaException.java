package ru.yandexmusiccasher.domain.yamusic_model;

/**
 * Created by grish on 08.07.2018.
 */

public class YandexCaptchaException extends Exception {

    public static void yandexCheck(String response) throws YandexCaptchaException {
        if(response.contains("https://music.yandex.ru/captcha/")) throw new YandexCaptchaException();
    }
}
