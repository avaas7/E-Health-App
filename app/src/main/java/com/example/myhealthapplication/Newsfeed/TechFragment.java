package com.example.myhealthapplication.Newsfeed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myhealthapplication.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TechFragment extends Fragment {


    String apiKey = "be39e2e137c342af9305bf21683cf23d";
    ArrayList<com.example.myhealthapplication.Newsfeed.modelClass> modelClassArrayList;
    Adapter adapter;
    String country = "in";
    String category = "technology";
    private RecyclerView recyclerViewOfTech;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


    View view = inflater.inflate(R.layout.techfragment,null);

        recyclerViewOfTech = view.findViewById(R.id.vpRecyclerViewTech);

        modelClassArrayList = new ArrayList<>();
        recyclerViewOfTech.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new Adapter(getContext(),modelClassArrayList);
        recyclerViewOfTech.setAdapter(adapter);

        findNews();


        return view;
    }

    private void findNews()
    {
        ApiUtilities.getApiInterface().getCategoryNews(country,category,100,apiKey).enqueue(new Callback<mainNews>() {
            @Override
            public void onResponse(Call<mainNews> call, Response<mainNews> response) {

                if(response.isSuccessful())
                {
                    modelClassArrayList.addAll(response.body().getArticles());
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<mainNews> call, Throwable t) {

            }
        });
    }


}
