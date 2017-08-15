package mota.dev.happytesting.Views.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import mota.dev.happytesting.R;

/**
 * Created by Slaush on 23/06/2017.
 */

public class SimpleInputDialog
{
    private Context context;
    private OnGetText onGet;
    private String title;
    private String preText;

    public SimpleInputDialog(Context context,String title, OnGetText onGetText)
    {
        this.context = context;
        this.onGet = onGetText;
        this.title = title;
        this.preText = "";
        build();
    }

    public SimpleInputDialog(Context context,String title, String preText, OnGetText onGetText)
    {
        this.context = context;
        this.onGet = onGetText;
        this.title = title;
        this.preText = preText;
        build();
    }

    private void build()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);

        View viewInflated = LayoutInflater.from(context).inflate(R.layout.input_dialog, null);

        final EditText input = (EditText) viewInflated.findViewById(R.id.input);


        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onGet.get(input.getText().toString());
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        input.setText(preText);
        builder.show();
    }


    public interface OnGetText
    {
        void get(String text);
    }
}
