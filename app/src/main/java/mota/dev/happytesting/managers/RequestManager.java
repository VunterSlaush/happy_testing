package mota.dev.happytesting.managers;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import mota.dev.happytesting.Consts;
import mota.dev.happytesting.MyApplication;
import mota.dev.happytesting.Urls;
import mota.dev.happytesting.utils.RxRequestAdapter;
import mota.dev.happytesting.utils.SingletonRequester;

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

    public Observable<JSONObject> login(String username, String password)
    {
        JSONObject obj = generateLoginJson(username,password);
        return request(Request.Method.POST,urlBase + Urls.URL_LOGIN,obj);
    }

    private JSONObject generateLoginJson(String username, String password)
    {
        JSONObject obj = new JSONObject();
        try {
            obj.put(Consts.USERNAME,username);
            obj.put(Consts.PASSWORD,password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }

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

    public Observable<JSONObject> createApp(String name, int [] ids)
    {
        JSONObject o = generateJSONCreateApp(name, ids);
        return request(Request.Method.POST,urlBase + Urls.URL_CREATE_APP,o);
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
        return request(Request.Method.GET,urlBase + Urls.URL_REPORTS, null);
    }
}
