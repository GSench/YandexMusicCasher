package ru.yandexmusiccasher;

import org.junit.Test;

import ru.yandexmusiccasher.domain.interactor.DownloadCompleteInteractor;
import ru.yandexmusiccasher.domain.model.MusicInfo;
import ru.yandexmusiccasher.domain.services.HttpHeadersManager;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        String oldName = "Artist - Title"+ MusicInfo.setStrategy(DownloadCompleteInteractor.DOWNLOAD)+"album111track111.mp3";
        String newName = "Artist - Title"+ MusicInfo.setStrategy(DownloadCompleteInteractor.PLAY)+"album111track111.mp3";
        assertEquals(newName, MusicInfo.updateStrategy(oldName, DownloadCompleteInteractor.PLAY));
        oldName = "Artist - Title"+"album111track111.mp3";
        newName = "Artist - Title"+"album111track111"+MusicInfo.setStrategy(DownloadCompleteInteractor.PLAY)+".mp3";
        assertEquals(newName, MusicInfo.updateStrategy(oldName, DownloadCompleteInteractor.PLAY));
    }

    @Test
    public void updateCookies(){
        String old = "yandexuid=3332477751536511126; path=/; domain=.yandex.ru; expires=Sat, 09 Sep 2028 16:38:46 GMT; max-age=315360000";
        String newC = "i=MevIJhD6cFYEjodOo76P8D3Z+rfvj7ZrOvK6/nOWoKK4pv9mgSGp/tOC9mq+GvsvDkdM72MxhC9VJNgdeKL1zupT30k=; Expires=Wed, 06-Sep-2028 16:38:56 GMT; Domain=.yandex.ru; Path=/; Secure; HttpOnly";
        System.out.println(HttpHeadersManager.updateCookies(old, newC));
    }
}