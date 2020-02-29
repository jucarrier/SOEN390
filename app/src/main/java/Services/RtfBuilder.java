package Services;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class RtfBuilder {
    private static Retrofit rtf= null;
    private static final String Base_URL="https://maps.googleapis.com/maps/";

    public static Retrofit builder(){
        if(rtf== null){
            rtf= new Retrofit.Builder().baseUrl(Base_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return rtf;
    }
}
