package com.example.concordiaguide.Services;
import com.example.concordiaguide.Services.*;

public class CommonServices {
    private static final String BASE_URL = "https://maps.googleapis.com/";

    public static GoogleApiService getGoogleApiService() {
        return RtfBuilder.builder().create(GoogleApiService.class);
        //helps to map the Json/Gson data to java object
    }
}
