package com.example.notetaker_java.DB;

import static com.example.notetaker_java.MainActivity.ID;

public class AppDB {
    private int _id;
    protected String title;
    protected String content;
    protected String appended;

    AppDB(){
        _id = ID;
        ID += 1;

    }
}
