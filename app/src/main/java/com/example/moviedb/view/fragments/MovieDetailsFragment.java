package com.example.moviedb.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.moviedb.R;
import com.example.moviedb.adapter.CompaniesAdapter;
import com.example.moviedb.helper.Const;
import com.example.moviedb.helper.ItemClickSupport;
import com.example.moviedb.model.Movies;
import com.example.moviedb.viewmodel.MovieViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MovieDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieDetailsFragment newInstance(String param1, String param2) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private TextView lbl_movie_title, lbl_movie_date, lbl_movie_overview, lbl_movie_genre, lbl_movie_popularity, lbl_movie_tagline,
            lbl_movie_avgvote, lbl_movie_vote;
    private ImageView img_poster, img_backdrop;
    private String movie_id;
    private MovieViewModel view_model;
    private List<Movies.GenresDTO> genre;
    private List<Movies.ProductionCompaniesDTO> companies;
    private RecyclerView rv_companies;
    private RatingBar rb_movie;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);

        lbl_movie_title = view.findViewById(R.id.lbl_title_movie_details_fragment);
        lbl_movie_date = view.findViewById(R.id.lbl_releasedate_movie_details_fragment);
        lbl_movie_overview = view.findViewById(R.id.lbl_overview_movie_details_fragment);
        lbl_movie_genre = view.findViewById(R.id.lbl_genre_movie_details_fragment);
        lbl_movie_popularity = view.findViewById(R.id.lbl_popularity_movie_details_fragment);
        lbl_movie_avgvote = view.findViewById(R.id.lbl_avgvote_movie_details_fragment);
        lbl_movie_vote = view.findViewById(R.id.lbl_vote_movie_details_fragment);
        lbl_movie_tagline = view.findViewById(R.id.lbl_tagline_movie_details_fragment);

        rv_companies = view.findViewById(R.id.rv_companies_movie_details_fragment);

        rb_movie = view.findViewById(R.id.ratingBar);

        img_poster = view.findViewById(R.id.img_poster_movie_details_fragment);
        img_backdrop = view.findViewById(R.id.img_backdrop_movie_details_fragment);

        movie_id = getArguments().getString("movieID");

        view_model = new ViewModelProvider(getActivity()).get(MovieViewModel.class);
        view_model.getMovieById(movie_id);
        view_model.getResultGetMovieById().observe(getViewLifecycleOwner(), showResultMovie);

        return view;
    }

    private Observer<Movies> showResultMovie = new Observer<Movies>() {
        @Override
        public void onChanged(Movies movies) {

            rv_companies.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            CompaniesAdapter adapter = new CompaniesAdapter(getActivity());
            adapter.setListCompanies(movies.getProduction_companies());
            rv_companies.setAdapter(adapter);

            ItemClickSupport.addTo(rv_companies).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    Toast.makeText(getActivity(), movies.getProduction_companies().get(position).getName(),
                            Toast.LENGTH_SHORT).show();
                }
            });

            ItemClickSupport.addTo(rv_companies).setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                    return false;
                }
            });

            lbl_movie_title.setText(movies.getTitle());

            Glide.with(getContext()).load(Const.IMG_URL + String.valueOf(movies.getPoster_path())).into(img_poster);

            Glide.with(getContext()).load(Const.IMG_URL + String.valueOf(movies.getBackdrop_path())).into(img_backdrop);

            genre = movies.getGenres();
            for (int i = 0; i<genre.size(); i++){
                String lblgenre = String.valueOf(lbl_movie_genre.getText());
                if (lblgenre.equals("Genre")){
                    lbl_movie_genre.setText(genre.get(i).getName());
                }else{
                    lbl_movie_genre.setText(lblgenre + ",  " + genre.get(i).getName());
                }
            }

            lbl_movie_tagline.setText(movies.getTagline());

            lbl_movie_overview.setText(movies.getOverview());

            lbl_movie_popularity.setText(String.valueOf(movies.getPopularity()));

            lbl_movie_date.setText(movies.getRelease_date());

            lbl_movie_vote.setText(String.valueOf(movies.getVote_count()) + " people voted");

            lbl_movie_avgvote.setText(String.valueOf(movies.getVote_average()/2));

            rb_movie.setRating((float) movies.getVote_average()/2);

        }
    };


}