package com.example.instagramclone;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Register your parse models
        ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("BQLpBWQYdcrt8Azi5iccWexa8viV73o0XB05Sur9")
                .clientKey("45M3zW8oQnW1Cce6EcfOa9c2qOId6Pfp3pJHMJnb")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
