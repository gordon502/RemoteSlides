package org.example.database;

import org.example.model.NewsTicker;
import org.example.model.Properties;
import org.example.model.Slide;

import java.util.List;

public interface DBHandler {
    List<Slide> readSlides();
    List<NewsTicker> readNewsTickers();
    Properties readProperties();
}
