package mota.dev.happytesting.parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mota.dev.happytesting.models.Observation;
import mota.dev.happytesting.models.Report;

/**
 * Created by Slaush on 18/06/2017.
 */

public class ObservationParser
{
    private static ObservationParser instance;
    private ObservationParser(){}

    public static ObservationParser getInstance()
    {
        if(instance == null)
            instance = new ObservationParser();
        return instance;
    }

    public List<Observation> generateObservationList(JSONObject json) throws JSONException
    {
        JSONArray array = json.optJSONArray("Observations");

        if (array.length() > 0)
        {
            List<Observation> observations = new ArrayList<>();
            for (int i = 0; i<array.length(); i++)
                observations.add(generateObservationFromJson(array.getJSONObject(i)));

            return observations;
        }

        return new ArrayList<>();
    }

    private Observation generateObservationFromJson(JSONObject jsonObject) {
        Observation r = new Observation();
        r.setText(jsonObject.optString("texto"));
        r.setId(jsonObject.optInt("id"));
        try{
            r.setImages(ImageParser.getInstance().generateImageList(jsonObject));
        }catch (Exception e)
        {

        }

        return r;
    }
}
