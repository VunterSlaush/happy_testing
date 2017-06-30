package mota.dev.happytesting.parsers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mota.dev.happytesting.Consts;
import mota.dev.happytesting.models.App;
import mota.dev.happytesting.utils.Functions;

/**
 * Created by Slaush on 13/06/2017.
 */

public class AppParser
{
    private static AppParser instance;
    private AppParser(){}

    public static AppParser getInstance()
    {
        if(instance == null)
            instance = new AppParser();
        return instance;
    }

    public App jsonToApp(JSONObject jsonObject) throws Exception
    {
        return jsonToApp(jsonObject,"res");
    }


    public App jsonToApp(JSONObject jsonObject, String objName) throws Exception
    {
        if(jsonObject.has("error"))
            throw new Exception();

        JSONObject appJson = jsonObject.getJSONObject(objName);
        App app = new App();
        app.setId(appJson.optInt("id"));
        app.setName(appJson.optString("nombre"));

        try {
            app.setReports(ReportParser.getInstance().generateReportList(appJson,"Reports"));
            app.setModificar(UserParser.getInstance().generateUsersList(appJson, "canEditMe"));
            app.setApp_owner(UserParser.getInstance().generateUserFromJson(appJson.optJSONObject("User")));
        }catch (Exception e)
        {

        }

        return app;
    }


    public List<App> getAppsOfJson(JSONObject jsonObject) throws Exception
    {
        if(jsonObject.has("error"))
            throw new Exception();
        JSONArray array = jsonObject.getJSONArray("apps");
        List<App> apps = new ArrayList<>();
        for (int i = 0; i< array.length(); i++)
        {
            App app = new App();
            app.setName(array.getJSONObject(i).optString(Consts.NAME));
            app.setId(array.getJSONObject(i).optInt("id"));
            apps.add(app);
        }
        return apps;
    }

    public JSONObject appToJson(App app)
    {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id",app.getId());
            obj.put("name",app.getName());
            obj.put("users",extractUserIdsToJSONArray(app));
        }catch (Exception e )
        {

        }
        return obj;
    }

    public JSONArray extractUserIdsToJSONArray(App app)
    {
        JSONArray array = new JSONArray();
        int [] ids = Functions.convertToUsersIdsArray(app.getModificar());
        for (int i = 0; i< ids.length; i++)
            array.put(ids[i]);
        return array;
    }

}
