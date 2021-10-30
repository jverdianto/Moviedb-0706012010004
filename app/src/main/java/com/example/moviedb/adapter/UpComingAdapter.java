package com.example.moviedb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviedb.R;
import com.example.moviedb.helper.Const;
import com.example.moviedb.model.NowPlaying;
import com.example.moviedb.model.UpComing;
import com.example.moviedb.viewmodel.MovieViewModel;

import java.util.ArrayList;

public class UpComingAdapter extends RecyclerView.Adapter<UpComingAdapter.CardViewViewHolder>{
    private Context context;
    private ArrayList<UpComing.ResultsDTO> listUpComing;
    private MovieViewModel view_model;
    private int page = 1;

    public ArrayList<UpComing.ResultsDTO> getListUpComing(){return listUpComing;}
    public void setListUpComing(ArrayList<UpComing.ResultsDTO> listUpComing){
        this.listUpComing = listUpComing;
    }
    public UpComingAdapter(Context context){
        this.context = context;
    }

    public void addUpComing() {
        page++;
        view_model = new ViewModelProvider((ViewModelStoreOwner) context).get(MovieViewModel.class);
        view_model.getUpComing(String.valueOf(page));
        view_model.getResultGetUpComing().observe((LifecycleOwner) context, showUpComing);
    }

    private Observer<UpComing> showUpComing = new Observer<UpComing>() {
        @Override
        public void onChanged(UpComing upComing) {
            for(int i = 0; i < 20; i++){
                listUpComing.add(upComing.getResults().get(i));
                notifyDataSetChanged();
            }
        }
    };

    @Override
    public UpComingAdapter.CardViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_up_coming, parent, false);
        return new UpComingAdapter.CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UpComingAdapter.CardViewViewHolder holder, int position) {
        final UpComing.ResultsDTO results = getListUpComing().get(position);
        holder.lbl_title.setText(results.getTitle());
        holder.lbl_overview.setText(results.getOverview());
        holder.lbl_releasedate.setText(results.getRelease_date());
        Glide.with(context).load(Const.IMG_URL + results.getPoster_path()).into(holder.img_poster);
    }

    @Override
    public int getItemCount() {
        return getListUpComing().size();
    }

    public class CardViewViewHolder extends RecyclerView.ViewHolder {
        ImageView img_poster;
        TextView lbl_title, lbl_overview, lbl_releasedate;
        CardView cv;

        public CardViewViewHolder(@NonNull View itemView) {
            super(itemView);
            img_poster = itemView.findViewById(R.id.img_poster_card_up_coming);
            lbl_title = itemView.findViewById(R.id.lbl_title_card_up_coming);
            lbl_overview = itemView.findViewById(R.id.lbl_overview_card_up_coming);
            lbl_releasedate = itemView.findViewById(R.id.lbl_releasedate_card_up_coming);
            cv = itemView.findViewById(R.id.cv_card_up_coming);
        }
    }
}
