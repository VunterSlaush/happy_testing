package mota.dev.happytesting.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import mota.dev.happytesting.R;

/**
 * Created by Slaush on 24/07/2017.
 */

public class Pnotify
{
    public static final int INFO = 0;
    public static final int WARNING = 1;
    public static final int ERROR = 2;

    public static Toast makeText(Context context, String text, int duration, int type)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.pnotify_layout,null);
        switch (type)
        {
            case INFO:
                layout.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                break;
            case WARNING:
                layout.setBackgroundColor(context.getResources().getColor(R.color.warning));
                break;
            case ERROR:
                layout.setBackgroundColor(context.getResources().getColor(R.color.error));
                break;
            default:
                layout.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                break;
        }

        TextView textView = (TextView) layout.findViewById(R.id.text);
        textView.setText(text);

        Toast toast = new Toast(context.getApplicationContext());
        toast.setGravity(Gravity.TOP | Gravity.RIGHT, 30, 30);
        toast.setDuration(duration);
        toast.setView(layout);
        return toast;
    }
}
