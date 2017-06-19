package mota.dev.happytesting.ViewModel.items;

import android.content.Context;

import java.util.Observable;

import mota.dev.happytesting.models.Observation;

/**
 * Created by Slaush on 18/06/2017.
 */

public class ItemObservationViewModel extends Observable
{
    private Context context;
    private Observation observation;

    public ItemObservationViewModel(Observation o, Context context)
    {
        this.context = context;
        this.observation = o;
    }

    public void setObservation(Observation o)
    {
        this.observation = o;
    }
}
