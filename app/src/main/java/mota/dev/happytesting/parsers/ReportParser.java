package mota.dev.happytesting.parsers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mota.dev.happytesting.models.Report;

/**
 * Created by Slaush on 13/06/2017.
 */

public class ReportParser
{
    private static ReportParser instance;
    private ReportParser(){}

    public static ReportParser getInstance()
    {
        if(instance == null)
            instance = new ReportParser();
        return instance;
    }



    public List<Report> generateReportList(JSONObject obj) throws JSONException
    {
        return generateReportList(obj, "reports");
    }

    public List<Report> generateReportList(JSONObject jsonObject, String arrayName) throws JSONException {
        JSONArray array = jsonObject.optJSONArray(arrayName);

        if (array.length() > 0)
        {
            List<Report> reports = new ArrayList<>();
            for (int i = 0; i<array.length(); i++)
                reports.add(generateReportFromJson(array.getJSONObject(i)));

            return reports;
        }

        return new ArrayList<>();
    }

    public Report generateReportFromJson(JSONObject jsonObject) throws JSONException
    {
        Report r = new Report();
        r.setName(jsonObject.optString("nombre"));
        r.setCreado(jsonObject.optString("createdAt"));
        r.setId(jsonObject.optInt("id"));
        try{
            r.setAppName(jsonObject.getJSONObject("App").optString("nombre"));
            r.setObservations(ObservationParser.getInstance().generateObservationList(jsonObject));
            //TODO agregar AUTOR !!
        }catch (Exception e)
        {

        }

        return r;
    }
}
