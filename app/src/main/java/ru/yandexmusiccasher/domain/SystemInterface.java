package ru.yandexmusiccasher.domain;

import java.io.IOException;
import java.net.URL;

import ru.yandexmusiccasher.domain.services.HttpParams;
import ru.yandexmusiccasher.domain.utils.Pair;
import ru.yandexmusiccasher.domain.utils.function;


/**
 * Created by grish on 01.05.2017.
 */

public interface SystemInterface {

    public void doOnBackground(function<Void> background);
    public Pair<byte[], HttpParams> httpGet(URL url, HttpParams params) throws IOException;
    public String[] getSavedStringArray(String title, String[] def);
    public void saveStringArray(String title, String[] array);
    public String getSavedString(String title, String def);
    public void saveString(String title, String string);
    /**
    public int getSavedInt(String title, int def);
    public void saveInt(String title, int i);*/
    public void removeSaved(String str);
    public String md5(String s);

}
