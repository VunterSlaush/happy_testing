package mota.dev.happytesting.Views.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import java.util.Observable;

import mota.dev.happytesting.R;
import mota.dev.happytesting.ViewModel.GalleryViewModel;
import mota.dev.happytesting.Views.adapters.ImageAdapter;
import mota.dev.happytesting.databinding.ActivityGalleryBinding;


/**
 * Created by Slaush on 07/05/2017.
 */

public class GalleryActivity extends BindeableActivity
{
    private GalleryViewModel viewModel;
   // private GalleryActivityBinding binding;
    ActivityGalleryBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initDataBinding();
        setupObserver(viewModel);
        setupAdapter();
    }

    private void setupAdapter()
    {
        ImageAdapter adapter = new ImageAdapter();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        binding.mosaicoRecyclerView.setLayoutManager(gridLayoutManager);
        binding.mosaicoRecyclerView.setAdapter(adapter);
    }

    /*public void select()
    {
        boolean [] imagesSelected = imageAdapter.getImagesSelected();
        final int len = imagesSelected.length;
        int cnt = 0;
        String selectImages = "";
        for (int i = 0; i < len; i++) {
            if (imagesSelected[i]) {
                cnt++;
                selectImages = selectImages + strUrls[i] + "|";
            }
        }
        if (cnt == 0)
        {
            Toast.makeText(getApplicationContext(), "Please select at least one image", Toast.LENGTH_LONG).show();
        }
        else
        {

            Log.d("SelectedImages", selectImages);
            Intent i = new Intent();
            i.putExtra("data", selectImages);
            setResult(Activity.RESULT_OK, i);
            finish();
        }
    }*/


    @Override
    public void update(Observable observable, Object o)
    {
        if (observable instanceof GalleryViewModel)
        {
            ImageAdapter adapter = (ImageAdapter) binding.mosaicoRecyclerView.getAdapter();
            adapter.setList(viewModel.getImages());
            binding.mosaicoRecyclerView.getItemAnimator().endAnimations();
        }
    }

    @Override
    public void initDataBinding()
    {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gallery);
        viewModel = new GalleryViewModel(this);
        binding.setViewModel(viewModel);
    }
}
