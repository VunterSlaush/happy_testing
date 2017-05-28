package mota.dev.happytesting.ViewModel;

import android.content.Context;
import android.databinding.ObservableField;
import android.view.View;
import android.widget.Toast;

import java.util.Observable;

/**
 * Created by Slaush on 28/05/2017.
 */

public class CreateAppViewModel extends Observable
{
    private Context context;
    public ObservableField<String> name;
    public CreateAppViewModel(Context context)
    {
        this.context = context;
        name = new ObservableField<>();
    }

    public void create(View view)
    {
        Toast.makeText(context,"HOLA"+name.get(),Toast.LENGTH_LONG).show();
    }
}
