package mota.dev.happytesting.Views.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import mota.dev.happytesting.R;

public class FullImageActivity extends AppCompatActivity
{
    @BindView(R.id.image)
    public ImageView image;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        String imageUrl = getIntent().getStringExtra("image");
        unbinder = ButterKnife.bind(this);
        if(imageUrl.contains("http"))
            Glide.with(this).load(imageUrl).into(image);
        else
            Glide.with(this).load(new File(imageUrl)).into(image);
    }

    @OnClick(R.id.back_image_button)
    public void back(View view)
    {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
