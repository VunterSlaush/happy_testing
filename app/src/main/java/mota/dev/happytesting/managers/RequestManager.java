package mota.dev.happytesting.managers;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import mota.dev.happytesting.Consts;
import mota.dev.happytesting.MyApplication;
import mota.dev.happytesting.Urls;
import mota.dev.happytesting.utils.CustomMultipartRequest;
import mota.dev.happytesting.utils.RxRequestAdapter;
import mota.dev.happytesting.utils.SingletonRequester;

import static mota.dev.happytesting.Urls.URL_APP;
import static mota.dev.happytesting.Urls.URL_REPORTS;

/**
 * Created by Slaush on 22/05/2017.
 */

public class RequestManager
{
    private static RequestManager instance;
    private String urlBase;
    private RequestManager()
    {
        urlBase = RouterManager.getInstance().getUrlBase();
    }

    public static RequestManager getInstance()
    {
        if(instance == null)
            instance = new RequestManager();
        return  instance;
    }

    public Observable<JSONObject> login(JSONObject obj)
    {
        return request(Request.Method.POST,urlBase + Urls.URL_LOGIN,obj);
    }


    //TODO mover esto de aqui, no es responsabilidad del RequestManager!
    private JSONObject generateJSONCreateApp(String app_name, int[] ids)
    {
        JSONObject obj = new JSONObject();
        JSONArray array = new JSONArray();
        try {
            obj.put(Consts.NAME,app_name);
            obj.put(Consts.OWNER, UserManager.getInstance().getUserId());
            for (int i = 0; i< ids.length; i++)
                array.put(ids[i]);
            obj.put("users",array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }


    private Observable<JSONObject> request(int method, String url, JSONObject obj)
    {
        try
        {
            RxRequestAdapter<JSONObject> adapter = new RxRequestAdapter<>();
            JsonObjectRequest request = new JsonObjectRequest(method, url, obj, adapter, adapter);
            SingletonRequester.getInstance(MyApplication.getInstance()).addToRequestQueue(request);
            return adapter.getObservable();
        } catch (Exception e)
        {
            e.printStackTrace();

            return new Observable<JSONObject>() {
                @Override
                protected void subscribeActual(Observer<? super JSONObject> observer) {
                    observer.onError(new Throwable("Undefined Error!"));
                }
            };
        }
    }

    public Observable<JSONObject> multipartRequest(int method, String url, Map<String, String> data,
                                                    Map<String,String> files, Map<String,JSONArray> arrays,
                                                    Map<String,JSONObject> jsons)
    {
        try
        {
            RxRequestAdapter<JSONObject> adapter = new RxRequestAdapter<>();
            CustomMultipartRequest request = new CustomMultipartRequest(method, url, adapter, adapter);
            request.addDataMap(data);
            request.addFiles(files);
            request.addJsonsArray(arrays);
            request.addJsons(jsons);
            request.build();
            SingletonRequester.getInstance(MyApplication.getInstance()).addToRequestQueue(request);
            return adapter.getObservable();
        } catch (Exception e)
        {
            e.printStackTrace();

            return new Observable<JSONObject>() {
                @Override
                protected void subscribeActual(Observer<? super JSONObject> observer) {
                    observer.onError(new Throwable("Undefined Error!"));
                }
            };
        }
    }

    public Observable<JSONObject> createApp(String name, int [] ids)
    {
        JSONObject o = generateJSONCreateApp(name, ids);
        return request(Request.Method.POST,urlBase + Urls.URL_CREATE_APP,o);
    }

    public Observable<JSONObject> deleteApp(JSONObject id)
    {
        return request(Request.Method.POST,urlBase + Urls.URL_DELETE_APP, id);
    }

    public Observable<JSONObject> getApps()
    {
        return request(Request.Method.GET, urlBase + Urls.URL_APPS, null);
    }

    public Observable<JSONObject> getUsers()
    {
        return request(Request.Method.GET,urlBase + Urls.URL_USERS, null);
    }

    public Observable<JSONObject> getReports()
    {
        return request(Request.Method.GET,urlBase + URL_REPORTS, null);
    }

    public Observable<JSONObject> getApp(int id)
    {
        return request(Request.Method.GET, urlBase + Urls.URL_APP + "/" + id,null);
    }

    public Observable<JSONObject> getReport(int id)
    {
        return request(Request.Method.GET,urlBase + Urls.URL_REPORT + "/"+id,null);
    }

    public Observable<JSONObject> updateUser(JSONObject user)
    {
        return request(Request.Method.POST,urlBase + Urls.URL_UPDATE_USER ,user);
    }

    public Observable<JSONObject> updateApp(JSONObject app) {
        return request(Request.Method.POST,urlBase + Urls.URL_UPDATE_APP, app);
    }

    public Observable<JSONObject> deleteReport(JSONObject report)
    {
        return request(Request.Method.POST, urlBase + Urls.URL_DELETE_REPORT, report);
    }

    public Observable<JSONObject> sendReport(Map<String, String> data,
                                             Map<String,String> files, Map<String,JSONArray> arrays,
                                             Map<String,JSONObject> jsons)
    {
        return multipartRequest(Request.Method.POST, urlBase + Urls.URL_CREATE_REPORT, data,files, arrays, jsons);
    }
}
