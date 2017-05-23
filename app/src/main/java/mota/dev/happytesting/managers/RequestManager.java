package mota.dev.happytesting.managers;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import mota.dev.happytesting.Models.User;
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
        JSONObject obj = null;
        try {
            obj = generateLoginJson(username,password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request(Request.Method.POST,urlBase + Urls.URL_LOGIN,obj);

    }

    private JSONObject generateLoginJson(String username, String password) throws JSONException
    {
        JSONObject obj = new JSONObject();
        obj.put("username",username);
        obj.put("password",password);
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

}
