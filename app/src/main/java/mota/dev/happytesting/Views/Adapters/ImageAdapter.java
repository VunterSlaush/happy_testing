package mota.dev.happytesting.Views.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import mota.dev.happytesting.R;
import mota.dev.happytesting.Views.adapters.viewholders.BaseViewHolder;
import mota.dev.happytesting.Views.adapters.viewholders.ImageViewHolder;
import mota.dev.happytesting.databinding.ImageItemBinding;
import mota.dev.happytesting.models.Image;

/**
 * Created by user on 15/05/2017.
 */

public class ImageAdapter extends BaseRecyclerAdapter<Image>
{
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        ImageItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.image_item, parent, false);
        return new ImageViewHolder(binding);
    }
}
