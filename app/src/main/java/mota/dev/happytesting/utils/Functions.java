package mota.dev.happytesting.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import mota.dev.happytesting.models.Image;
import mota.dev.happytesting.models.User;

/**
 * Created by Slaush on 23/06/2017.
 */

public class Functions
{

    public static void showConfirmDialog(Context context, DialogInterface.OnClickListener okListener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("¿Estas Seguro?")
                .setCancelable(false)
                .setPositiveButton("Si", okListener)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static int[] convertToUsersIdsArray(List<User> users)
    {
        int [] ids = new int [users.size()];
        for (int i = 0; i<users.size(); i++)
        {
            ids[i] = users.get(i).getId();
        }
        return ids;
    }

    public static List<Image> generateImageListFromString(String data) {
        List<Image> images = new ArrayList<>();
        String [] dirs = data.split("|");
        for (String dir : dirs)
            images.add(new Image(dir));
        return images;
    }

    public static String generateRandomId()
    {
        return UUID.randomUUID().toString();
    }
}

