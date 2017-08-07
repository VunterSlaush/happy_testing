package mota.dev.happytesting.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
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

    public static void showAskDialog(Context context, String title, DialogInterface.OnClickListener okListener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(title)
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
        StringTokenizer token = new StringTokenizer(data, "|");
        while (token.hasMoreElements())
        {
            String d = token.nextToken();
            Log.d("MOTARRAY","Dir:"+d);
            images.add(new Image(d));
        }

        return images;
    }


    public static JSONArray concatJSONArray(JSONArray... arrs)
            throws JSONException {
        JSONArray result = new JSONArray();
        for (JSONArray arr : arrs) {
            for (int i = 0; i < arr.length(); i++) {
                result.put(arr.get(i));
            }
        }
        return result;
    }

    public static String generateRandomId()
    {
        return UUID.randomUUID().toString();
    }


    public static String formatDate(String dateStr)
    {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(dateStr);
            return new SimpleDateFormat("dd/MM/yyyy").format(date);
        } catch (ParseException e)
        {
            try {
                date = new SimpleDateFormat("dd/MM/yyyy-HH:mm").parse(dateStr);
                return new SimpleDateFormat("dd/MM/yyyy").format(date);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }

        }
        return "";
    }
}

