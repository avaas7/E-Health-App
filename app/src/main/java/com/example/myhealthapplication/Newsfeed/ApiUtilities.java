package com.example.myhealthapplication.Newsfeed;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtilities {

    private static Retrofit retrofit = null;

    public static com.example.myhealthapplication.Newsfeed.ApiInterface getApiInterface()
    {
        if(retrofit==null)
        {
            retrofit = new Retrofit.Builder().baseUrl(com.example.myhealthapplication.Newsfeed.ApiInterface.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit.create(com.example.myhealthapplication.Newsfeed.ApiInterface.class);
    }

}
