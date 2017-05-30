package mota.dev.happytesting.ViewModel;

import android.content.Context;
import android.databinding.ObservableField;
import android.view.View;
import android.widget.Toast;

import java.util.Observable;

import mota.dev.happytesting.useCases.CreateApp;

/**
 * Created by Slaush on 28/05/2017.
 */

public class CreateAppViewModel extends Observable
{
    private Context context;
    public ObservableField<String> name;
    private CreateApp useCase;
    public CreateAppViewModel(Context context)
    {
        this.context = context;
        name = new ObservableField<>("");
        useCase = new CreateApp(context);
    }

    public void create(View view)
    {
        useCase.createApp(name.get());
    }
}
