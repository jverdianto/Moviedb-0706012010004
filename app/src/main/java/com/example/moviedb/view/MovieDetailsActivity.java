package com.example.moviedb.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.moviedb.R;
import com.example.moviedb.helper.Const;

public class MovieDetailsActivity extends AppCompatActivity {

    private TextView text_title, text_releasedate, text_overview;
    private ImageView img_poster;
    private String title, image_path, date, overview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        text_title = findViewById(R.id.text_title_moviedetails);
        text_releasedate = findViewById(R.id.text_releasedate_moviedetails);
        text_overview = findViewById(R.id.text_overview_moviedetails);
        img_poster = findViewById(R.id.img_poster_moviedetails);

        Intent intent = getIntent();

        title = intent.getExtras().getString("title");
        text_title.setText(title);

        image_path = intent.getExtras().getString("gambar");
        Glide.with(getBaseContext()).load(Const.IMG_URL + image_path).into(img_poster);

        date = intent.getExtras().getString("date");
        text_releasedate.setText(date);

        overview = intent.getExtras().getString("overview");
        text_overview.setText(overview);

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}