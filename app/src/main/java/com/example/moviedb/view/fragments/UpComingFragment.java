package com.example.moviedb.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.example.moviedb.R;
import com.example.moviedb.adapter.NowPlayingAdapter;
import com.example.moviedb.adapter.UpComingAdapter;
import com.example.moviedb.helper.ItemClickSupport;
import com.example.moviedb.model.Movies;
import com.example.moviedb.model.NowPlaying;
import com.example.moviedb.model.UpComing;
import com.example.moviedb.view.activities.LoadingDialog;
import com.example.moviedb.viewmodel.MovieViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpComingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpComingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UpComingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpcomingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpComingFragment newInstance(String param1, String param2) {
        UpComingFragment fragment = new UpComingFragment();
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

    private RecyclerView rv_up_coming;
    private MovieViewModel view_model;
    private boolean isScrolling = false;
    private int currentItems, totalItems, scrollOutItems;
    private String page = "1";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming, container, false);

        rv_up_coming = view.findViewById(R.id.rv_up_coming_fragment);
        view_model = new ViewModelProvider(getActivity()).get(MovieViewModel.class);
        view_model.getUpComing(page);
        view_model.getResultGetUpComing().observe(getActivity(), showUpComing);

        return view;
    }

    private Observer<UpComing> showUpComing = new Observer<UpComing>() {

        @Override
        public void onChanged(UpComing upComing) {
            LinearLayoutManager manager;
            manager = new LinearLayoutManager(getActivity());
            rv_up_coming.setLayoutManager(manager);

            UpComingAdapter adapter = new UpComingAdapter(getActivity());
            adapter.setListUpComing(upComing.getResults());
            rv_up_coming.setAdapter(adapter);

            rv_up_coming.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                        isScrolling = true;
                    }
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    currentItems = manager.getChildCount();
                    totalItems = manager.getItemCount();
                    scrollOutItems = manager.findFirstVisibleItemPosition();

                    if(isScrolling && (currentItems + scrollOutItems == totalItems)){
                        isScrolling = false;

                        final LoadingDialog loadingDialog = new LoadingDialog(getActivity());
                        loadingDialog.startLoadingDialog();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                adapter.addUpComing();
                                adapter.notifyDataSetChanged();
                                loadingDialog.dismissDialog();
                            }
                        }, 250);
                    }

                }
            });

            ItemClickSupport.addTo(rv_up_coming).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                    final LoadingDialog loadingDialog = new LoadingDialog(getActivity());
                    loadingDialog.startLoadingDialog();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.dismissDialog();
                        }
                    }, 250);

                    Bundle bundle = new Bundle();
                    bundle.putString("movieID", ""+upComing.getResults().get(position).getId());
                    Navigation.findNavController(v).navigate(R.id.action_upComingFragment_to_movieDetailsFragment, bundle);
                }
            });

            ItemClickSupport.addTo(rv_up_coming).setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                    return false;
                }
            });

        }
    };

}