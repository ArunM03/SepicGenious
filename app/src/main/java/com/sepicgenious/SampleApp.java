package com.sepicgenious;

import android.app.Application;

//import com.downloader.PRDownloader;
//import com.downloader.PRDownloaderConfig;

public class SampleApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
//                .setDatabaseEnabled(true)
//                .setReadTimeout(30_000)
//                .setConnectTimeout(30_000)
//                .build();
//        PRDownloader.initialize(this, config);
    }

}