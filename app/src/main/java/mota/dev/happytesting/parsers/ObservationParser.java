package mota.dev.happytesting.parsers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mota.dev.happytesting.models.Observation;
import mota.dev.happytesting.models.Report;
import mota.dev.happytesting.utils.Functions;

/**
 * Created by Slaush on 18/06/2017.
 */

public class ObservationParser {
    private static ObservationParser instance;

    private ObservationParser() {
    }

    public static ObservationParser getInstance() {
        if (instance == null)
            instance = new ObservationParser();
        return instance;
    }


    // TODO SALVAR ESTA LISTA EN ALGUN LUGAR !
    public List<Observation> generateObservationList(JSONObject json) throws JSONException {
        //Log.d("MOTA--->","OBSERVATIONS LIST:"+json.toString());
        JSONArray array = json.optJSONArray("Observations");

        if (array.length() > 0) {
            List<Observation> observations = new ArrayList<>();
            for (int i = 0; i < array.length(); i++)
                observations.add(generateObservationFromJson(array.getJSONObject(i)));

            return observations;
        }

        return new ArrayList<>();
    }

    public Observation generateObservationFromJson(JSONObject jsonObject) {
        Log.d("MOTAE--->","OBSERVATION JSON:"+jsonObject);
        Observation r = new Observation();
        r.setText(jsonObject.optString("texto"));
        r.setId(jsonObject.optInt("id"));
        r.setLocalId(Functions.generateRandomId());
        Log.d("MOTA--->", "Generate Observation!");
        try {
            r.setImages(ImageParser.getInstance().generateImageList(jsonObject));
        } catch (Exception e) {

        }

        return r;
    }

    public JSONArray generateObservationArray(List<Observation> observations)
    {
        JSONArray array = new JSONArray();
        for (int i = 0; i < observations.size(); i++) {
            JSONObject obj = new JSONObject();
            try
            {
                obj.put("texto", observations.get(i).getText());
                obj.put("correlativo", i);
            } catch (Exception e) {
            }
            array.put(obj);
        }
        return array;
    }

    public JSONObject generateJSONObservationToSend(String text, Report report)
    {
        JSONObject obj = new JSONObject();
        try
        {
            obj.put("reporte", report.getId());
            obj.put("texto",text);
        }catch (Exception e){}
        return obj;
    }
}
