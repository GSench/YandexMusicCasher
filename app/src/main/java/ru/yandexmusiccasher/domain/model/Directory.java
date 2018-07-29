package ru.yandexmusiccasher.domain.model;

import java.io.IOException;

/**
 * Created by grish on 29.07.2018.
 */

public interface Directory {

    public boolean available();
    public MFile createFile(String filename) throws IOException;
    public String toString();

}
