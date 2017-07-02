package mota.dev.happytesting.Views.activities;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;

import java.util.List;
import java.util.Observable;

import mota.dev.happytesting.R;
import mota.dev.happytesting.ViewModel.GalleryViewModel;
import mota.dev.happytesting.Views.adapters.ImageAdapter;
import mota.dev.happytesting.Views.interfaces.Selectable;
import mota.dev.happytesting.databinding.ActivityGalleryBinding;
import mota.dev.happytesting.managers.PermissionManager;
import mota.dev.happytesting.models.Image;


/**
 * Created by Slaush on 07/05/2017.
 */

public class GalleryActivity extends BindeableActivity implements Selectable<Image>,PermissionManager.PermisionResult
{
    private GalleryViewModel viewModel;
    private ActivityGalleryBinding binding;
    private final static int IMAGE_PERMISSION = 5879;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setupPermission();
    }

    private void setupPermission()
    {
        PermissionManager.getInstance()
                            .requestPermission(GalleryActivity.this,
                                               Manifest.permission.READ_EXTERNAL_STORAGE,IMAGE_PERMISSION, this);
    }

    private void setupAdapter()
    {
        ImageAdapter adapter = new ImageAdapter();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        binding.mosaicoRecyclerView.setLayoutManager(gridLayoutManager);
        binding.mosaicoRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.getInstance().onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

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
        viewModel = new GalleryViewModel(this,this);
        binding.setViewModel(viewModel);
    }

    @Override
    public List<Image> getSelected()
    {
        ImageAdapter adapter = (ImageAdapter) binding.mosaicoRecyclerView.getAdapter();
        return adapter.getSelectedImages();
    }

    @Override
    public void onDenied() {
        finish();
    }

    @Override
    public void onGranted()
    {
        initDataBinding();
        setupObserver(viewModel);
        setupAdapter();
        viewModel.setParams(getIntent().getExtras());
    }
}
