package com.izone.onethestorycgvalarm.component;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "theater")
public class TheaterProperty {

    private List<Map<String, String>> cgv = new ArrayList<>();

    public List<Map<String, String>> getCgv() {
        return cgv;
    }
}
