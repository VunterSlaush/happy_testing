package mota.dev.happytesting.parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mota.dev.happytesting.models.Image;

/**
 * Created by Slaush on 18/06/2017.
 */

public class ImageParser
{
    private static ImageParser instance;
    private ImageParser(){}

    public static ImageParser getInstance()
    {
        if(instance == null)
            instance = new ImageParser();
        return instance;
    }

    public List<Image> generateImageList(JSONObject jsonObject) throws JSONException {
        JSONArray array = jsonObject.optJSONArray("images");

        if (array.length() > 0)
        {
            List<Image> images = new ArrayList<>();
            for (int i = 0; i<array.length(); i++)
                images.add(generateImageFromJson(array.getJSONObject(i)));

            return images;
        }

        return new ArrayList<>();
    }

    private Image generateImageFromJson(JSONObject jsonObject) {
        Image r = new Image();
        r.setDir(jsonObject.optString("direccion"));
        r.setId(jsonObject.optInt("id"));
        return r;
    }
}
