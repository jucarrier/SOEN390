package com.example.concordiaguide.Services;

import com.example.concordiaguide.Services.GoogleApiService;
import com.example.concordiaguide.Services.RtfBuilder;

public class Common {
    private static final String BASE_URL = "https://maps.googleapis.com/";

    public static GoogleApiService getGoogleApiService() {
        return RtfBuilder.builder().create(GoogleApiService.class);
    }
}