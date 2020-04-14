package Helpers;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RtfBuilder {
    private static final String BASE_URL = "https://maps.googleapis.com/maps/";
    private static Retrofit rtf = null;

    public static Retrofit builder() {
        if (rtf == null) {
            rtf = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return rtf;
    }
}
