package mota.dev.happytesting.ViewModel.items;

import android.content.Context;

import java.util.Observable;

import mota.dev.happytesting.models.Image;

/**
 * Created by Slaush on 18/06/2017.
 */

public class ItemImageViewModel extends Observable
{
    private Context context;
    private Image image;

    public ItemImageViewModel(Image image, Context context)
    {
        this.image = image;
        this.context = context;
    }
}
