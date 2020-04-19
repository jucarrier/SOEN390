package Helpers;

import Models.MyPlaces;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface GoogleApiService {
    @GET
    Call<MyPlaces> getNearByPlaces(@Url String url);
    //can send and update request  to the server with url inside when this interface is implemented
}
