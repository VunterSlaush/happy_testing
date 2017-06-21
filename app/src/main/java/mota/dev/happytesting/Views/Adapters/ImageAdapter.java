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
import java.util.ArrayList;
import java.util.List;

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
    private List<ImageViewHolder> holders;
    public ImageAdapter()
    {
        super();
        holders = new ArrayList<>();
    }
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        ImageItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.image_item, parent, false);
        ImageViewHolder holder = new ImageViewHolder(binding);
        holders.add(holder);
        return holder;
    }


    public List<Image> getSelectedImages()
    {
        List<Image> selected = new ArrayList<>();
        for (int i = 0; i< holders.size(); i++)
        {
            if(holders.get(i).binding.getViewModel().checked.get())
                selected.add(list.get(i));
        }
        return selected;
    }
}
