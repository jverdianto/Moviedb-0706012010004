package com.example.moviedb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.moviedb.R;
import com.example.moviedb.helper.Const;
import com.example.moviedb.model.Movies;
import com.example.moviedb.model.UpComing;

import java.util.ArrayList;
import java.util.List;

public class CompaniesAdapter extends RecyclerView.Adapter<CompaniesAdapter.CardViewViewHolder>{
    @NonNull
    private Context context;
    private List<Movies.ProductionCompaniesDTO> listCompanies;

    private List<Movies.ProductionCompaniesDTO> getListCompanies(){
        return listCompanies;
    }

    public void setListCompanies(List<Movies.ProductionCompaniesDTO> listCompanies){
        this.listCompanies = listCompanies;
    }

    public CompaniesAdapter(Context context){
        this.context = context;
    }

    @Override
    public CompaniesAdapter.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_companies, parent, false);
        return new CompaniesAdapter.CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewViewHolder holder, int position) {
        final Movies.ProductionCompaniesDTO results = getListCompanies().get(position);
        if(results.getLogo_path() != null){
            Glide.with(context).load(Const.IMG_URL + results.getLogo_path()).into(holder.img_logo);
        }else{
            Glide.with(context)
                    .load("https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/1024px-No_image_available.svg.png")
                    .into(holder.img_logo);
        }
    }

    @Override
    public int getItemCount() {
        return getListCompanies().size();
    }

    public class CardViewViewHolder extends RecyclerView.ViewHolder {
        ImageView img_logo;

        public CardViewViewHolder(@NonNull View itemView) {
            super(itemView);
            img_logo = itemView.findViewById(R.id.img_logo_card_companies);
        }
    }
}
