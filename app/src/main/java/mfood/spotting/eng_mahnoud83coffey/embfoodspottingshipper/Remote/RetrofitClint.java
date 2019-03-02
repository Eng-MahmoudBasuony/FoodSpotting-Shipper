package mfood.spotting.eng_mahnoud83coffey.embfoodspottingshipper.Remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClint
{


    private  static Retrofit retrofit=null;


    //Get message Notification
    public static Retrofit getClint(String baseURL)
    {

        if (retrofit ==null)
        {
            retrofit=new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

}
