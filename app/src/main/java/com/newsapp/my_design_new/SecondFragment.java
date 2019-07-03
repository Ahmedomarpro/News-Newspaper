package com.newsapp.my_design_new;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.newsapp.my_design_new.api.ApiClient;
import com.newsapp.my_design_new.api.ApiInterface;
import com.newsapp.my_design_new.models.Article;
import com.newsapp.my_design_new.models.News;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecondFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String API_KEY = "35b2f2b16e844957940d0890b0141ab3";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articles = new ArrayList<>();
    private Adapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String TAG = SecondFragment.class.getSimpleName();
    private TextView topHeadline;
    private RelativeLayout errorLayout;
    private ImageView errorImage;
    private TextView errorTitle, errorMessage;
    private Button btnRetry;

    String errorCode;
    private AdView mAdView;

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_second_fragment,container,false);

        topHeadline = view.findViewById(R.id.topheadelines);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager= new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        //layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        onLoadingSwipeRefresh("");

        errorLayout = view.findViewById(R.id.errorLayout);
        errorImage = view.findViewById(R.id.errorImage);
        errorTitle = view.findViewById(R.id.errorTitle);
        errorMessage = view.findViewById(R.id.errorMessage);
        btnRetry = view.findViewById(R.id.btnRetry);
        LoadJson(" ");



        setHasOptionsMenu(true);


        mAdView = view.findViewById(R.id.adView2);
        AdView adView = new AdView(getContext());

        adView.setAdSize(AdSize.BANNER);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        return view;
    }
    public void LoadJson(final String keyword) {
        errorLayout.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(true);
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        String country = Utils.getCountry();
        String language = Utils.getLanguage();

        Call<News> call;

        if(keyword.length() > 0){

            call = apiInterface.getNewsSearch(keyword, language, "publishedAt", API_KEY);
        }else {
         //  call = apiInterface.getNewsbysources("techcrunch", API_KEY);
                    call = apiInterface.getNewsbySports("eg","sports",API_KEY);


        }

        call.enqueue(new Callback<News>() {
            private Response<News> response;

            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                // this.response = response;
                //  assert response.body() != null;
                // assert response.body() != null;

                if (response.body() != null) {
                    if (response.isSuccessful() && response.body().getArticle() != null){

                        if (!articles.isEmpty()){
                            articles.clear();
                        }

                        articles = response.body().getArticle();
//                        Toast.makeText(MainActivity.this,articles.get(0).getAuthor(),Toast.LENGTH_SHORT).show();

                        adapter = new Adapter(articles,getActivity());
                        recyclerView.setAdapter(adapter);
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();

                        topHeadline.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setRefreshing(false);

                        initListener();

                    }else {
                        topHeadline.setVisibility(View.INVISIBLE);
                        swipeRefreshLayout.setRefreshing(false);

                        switch (response.code()) {
                            case 404:
                                errorCode = "404 not found";
                                break;
                            case 500:
                                errorCode = "500 server broken";
                                break;
                            default:
                                errorCode = "unknown error";
                                break;
                        }
                        showErrorMessage(
                                R.drawable.no_result,
                                "No Result",
                                "Please Try Again!\n"+
                                        errorCode);


                    }

                }


            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                topHeadline.setVisibility(View.INVISIBLE);

                //        Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();

                swipeRefreshLayout.setRefreshing(false);
                showErrorMessage(
                        R.drawable.no_result,
                        "Oops..",
                        "Network failure, Please Try Again\n"+ "" .toString());
            }
        });

    }
    private void initListener(){

        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ImageView imageView = view.findViewById(R.id.img);
                Intent intent = new Intent(getActivity(), NewsDetailActivitY.class);

                Article article = articles.get(position);
                intent.putExtra("url", article.getUrl());
                intent.putExtra("title", article.getTitle());
                intent.putExtra("img",  article.getUrlToImage());
                intent.putExtra("date",  article.getPublishedAt());
                intent.putExtra("source",  article.getSource().getName());
                intent.putExtra("author", article.getAuthor());

                Pair<View,String> pair = Pair.create((View)imageView, ViewCompat.getTransitionName(imageView));
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        pair
                );
                //---------------------------------------------------------------------------

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(intent,optionsCompat.toBundle());
                }else {
                    startActivity(intent);
                }
            }
        });

    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    //public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
    public void onCreateOptionsMenu(Menu menu , MenuInflater inflater) {
        inflater = Objects.requireNonNull(getActivity()).getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setQueryHint("Search Latest News...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (query.length() > 2){
                    onLoadingSwipeRefresh(query);
                }
                else {
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newtext) {
                
                onLoadingSwipeRefresh(newtext);

                return false;
            }
        });

        searchMenuItem.getIcon().setVisible(false, false);

        super.onCreateOptionsMenu(menu,inflater);
    }



    @Override
    public void onRefresh() {
        LoadJson("");


    }

    private void onLoadingSwipeRefresh(final String keyword) {

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                LoadJson(keyword);

            }
        });
    }

    private void showErrorMessage(int imageView, String title, String message){

        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
        }

        errorImage.setImageResource(imageView);
        errorTitle.setText(title);
        errorMessage.setText(message);
        recyclerView.setVisibility(View.GONE);
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoadingSwipeRefresh("");
                Toast.makeText(getActivity(), "Network failure, Please Try Again!!!", Toast.LENGTH_SHORT).show();

            }
        });

    }

}
