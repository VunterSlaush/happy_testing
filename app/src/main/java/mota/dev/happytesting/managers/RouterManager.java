package mota.dev.happytesting.managers;


import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONObject;

import java.net.URISyntaxException;

import mota.dev.happytesting.Consts;
import mota.dev.happytesting.MyApplication;
import mota.dev.happytesting.utils.PreferencesHelper;

/**
 * Created by Slaush on 15/05/2017.
 */

public class RouterManager
{
    private static final int MAX_TRIES = 5;
    private static RouterManager instance;
    private static String TAG = "ROUTER";

    private static final int MAX_IP_ON_FOUR_NUMBER = 20;
    private static final int TIMEOUT = 5;
    private static final int [] IP_FIRST_NUMBER = {192,10};
    private static final int [] IP_SECOND_NUMBER = {168,0};
    private static final int [] IP_THIRD_NUMBER = {0,1,2};
    private static final int PORT = 1234;

    private String urlBase;
    private boolean connected;
    private boolean confirmed;
    private int triesToConnect;
    private IO.Options opts;

    private RouterManager()
    {
        confirmed = false;
        triesToConnect = 0;
        connected = false;
        generateSocketOptions();
    }

    public static RouterManager getInstance()
    {
        if(instance == null)
            instance = new RouterManager();
        return instance;
    }

    public void findServerUrl()
    {
        String urlOnPref = PreferencesHelper.readString(MyApplication.getInstance(),Consts.URL_BASE_PREF,"");
        if(urlOnPref.isEmpty())
        {
            Log.d(TAG,"INIT TO FIND URL:"+System.currentTimeMillis());
            parallelSearch(IP_FIRST_NUMBER[0],IP_SECOND_NUMBER[0],IP_THIRD_NUMBER[0]);
            parallelSearch(IP_FIRST_NUMBER[0],IP_SECOND_NUMBER[0],IP_THIRD_NUMBER[1]);
            parallelSearch(IP_FIRST_NUMBER[0],IP_SECOND_NUMBER[0],IP_THIRD_NUMBER[2]);
            parallelSearch(IP_FIRST_NUMBER[1],IP_SECOND_NUMBER[1],IP_THIRD_NUMBER[0]);
        }
        else 
            tryToAutoConnect(urlOnPref);
    }

    private void tryToAutoConnect(String urlOnPref)
    {
        Log.d(TAG,"Trying To AutoConnect:"+System.currentTimeMillis()+" To:"+urlOnPref);
        tryToConnectSocket(urlOnPref);
        PreferencesHelper.deleteKey(MyApplication.getInstance(),Consts.URL_BASE_PREF);
        runTimeOutThread();
    }

    private void parallelSearch(final int first, final int second, final int third)
    {
        Log.d(TAG,"Running Parallel Search on:"+first+"."+second+"."+third);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                for (int i = 0; i< MAX_IP_ON_FOUR_NUMBER * 2; i++)
                {
                    if (connected)
                        return;
                    else if(i < MAX_IP_ON_FOUR_NUMBER)
                        tryToConnectSocket(makeUrl(first,second,third,i));
                    else
                        tryToConnectSocket(makeUrl(first,second,third,80 + i));

                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    private void tryToConnectSocket(String url)
    {
        try
        {
            Socket socket = IO.socket(url,opts);
            socket.on(Socket.EVENT_CONNECT,onConnect);
            socket.on("happy testing", onReceive);
            socket.connect();
        } catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
    }

    private void generateSocketOptions()
    {
        opts = new IO.Options();
        opts.forceNew = false;
        opts.reconnection = false;
        opts.reconnectionAttempts = 0;

    }

    private String makeUrl(int first, int second, int third, int four)
    {
        return "http://"+first+"."+second+"."+third+"."+four+":"+PORT;
    }


    private Emitter.Listener onReceive = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            Log.d(TAG,"Confirmado:"+System.currentTimeMillis());
            JSONObject obj = (JSONObject) args[0];
            confirmed = true;
            urlBase = obj.optString("url");
            Log.d(TAG,"Conectado a :"+urlBase);
            PreferencesHelper.writeString(MyApplication.getInstance(), Consts.URL_BASE_PREF, urlBase);
        }
    };

    private Emitter.Listener onConnect = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            Log.d(TAG,"Conectado!");
            connected = true;
            runTimeOutThread();
        }
    };

    private void runTimeOutThread()
    {
        Thread timeout = new Thread(new Runnable() {
            @Override
            public void run()
            {
                int seconds = 0;
                while(seconds <= TIMEOUT)
                {
                    if(System.currentTimeMillis() % 1000 == 0)
                        seconds++;

                    if(confirmed)
                        return;
                }

                if(!confirmed && triesToConnect < MAX_TRIES)
                {
                    triesToConnect++;
                    connected = false;
                    Log.d(TAG,"FINALIZO EL TIMEOUT:"+System.currentTimeMillis());
                    findServerUrl();
                }
            }
        });
        timeout.start();
    }

    public String getUrlBase()
    {
        return urlBase;
    }

    public boolean isConnected()
    {
        return connected && confirmed;
    }
}
