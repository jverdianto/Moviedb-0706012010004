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
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviedb.R;
import com.example.moviedb.helper.Const;
import com.example.moviedb.model.NowPlaying;
import com.example.moviedb.view.fragments.NowPlayingFragment;
import com.example.moviedb.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.Observable;

public class NowPlayingAdapter extends RecyclerView.Adapter<NowPlayingAdapter.CardViewViewHolder>{

    private Context context;
    private ArrayList<NowPlaying.ResultsDTO> listNowPlaying;
    private MovieViewModel view_model;
    private int page = 1;

    public ArrayList<NowPlaying.ResultsDTO> getListNowPlaying(){return listNowPlaying;}
    public void setListNowPlaying(ArrayList<NowPlaying.ResultsDTO> listNowPlaying){
        this.listNowPlaying = listNowPlaying;
    }
    public NowPlayingAdapter(Context context){
        this.context = context;
    }

    public void addNowPlaying() {
        page++;
        view_model = new ViewModelProvider((ViewModelStoreOwner) context).get(MovieViewModel.class);
        view_model.getNowPlaying(String.valueOf(page));
        view_model.getResultGetNowPlaying().observe((LifecycleOwner) context, showNowPlaying);
    }

    private Observer<NowPlaying> showNowPlaying = new Observer<NowPlaying>() {
        @Override
        public void onChanged(NowPlaying nowPlaying) {
            for(int i = 0; i < 20; i++){
                listNowPlaying.add(nowPlaying.getResults().get(i));
                notifyDataSetChanged();
            }
        }
    };

    @Override
    public NowPlayingAdapter.CardViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_now_playing, parent, false);
        return new NowPlayingAdapter.CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NowPlayingAdapter.CardViewViewHolder holder, int position) {
        final NowPlaying.ResultsDTO results = getListNowPlaying().get(position);
        holder.lbl_title.setText(results.getTitle());
        holder.lbl_overview.setText(results.getOverview());
        holder.lbl_releasedate.setText(results.getRelease_date());
        Glide.with(context).load(Const.IMG_URL + results.getPoster_path()).into(holder.img_poster);
//        holder.cv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, MovieDetailsActivity.class);
//                intent.putExtra("movie_id", results.getId());
//                intent.putExtra("title", results.getTitle());
//                intent.putExtra("gambar", results.getPoster_path());
//                intent.putExtra("overview", results.getOverview());
//                intent.putExtra("date", results.getRelease_date());
//                context.startActivity(intent);
//
//                Bundle bundle = new Bundle();
//                bundle.putString("movieId", ""+results.getId());
//                Navigation.findNavController(view).navigate(R.id.action_nowPlayingFragment_to_movieDetailsFragment, bundle);
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return getListNowPlaying().size();
    }

    public class CardViewViewHolder extends RecyclerView.ViewHolder {
        ImageView img_poster;
        TextView lbl_title, lbl_overview, lbl_releasedate;
        CardView cv;

        public CardViewViewHolder(@NonNull View itemView) {
            super(itemView);
            img_poster = itemView.findViewById(R.id.img_poster_card_nowplaying);
            lbl_title = itemView.findViewById(R.id.lbl_title_card_nowplaying);
            lbl_overview = itemView.findViewById(R.id.lbl_overview_card_nowplaying);
            lbl_releasedate = itemView.findViewById(R.id.lbl_releasedate_card_nowplaying);
            cv = itemView.findViewById(R.id.cv_card_nowplaying);
        }
    }
}
