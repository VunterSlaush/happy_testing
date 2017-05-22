package mota.dev.happytesting;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import mota.dev.happytesting.managers.PermissionManager;
import mota.dev.happytesting.Views.GalleryActivity;
import mota.dev.happytesting.managers.RouterManager;
import mota.dev.happytesting.utils.CustomMultipartRequest;


public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_MULTIPLE = 147;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 7645;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionManager.getInstance().requestPermission(MainActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        REQUEST_READ_EXTERNAL_STORAGE,
                        new PermissionManager.PermisionResult() {
                            @Override
                            public void onDenied() {
                                System.exit(0);
                            }

                            @Override
                            public void onGranted() {
                                Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
                                startActivityForResult(intent, PICK_IMAGE_MULTIPLE);
                            }
                        });

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_MULTIPLE)
        {
            ArrayList<String> imagesPathList = new ArrayList<String>();
            String[] imagesPath = data.getStringExtra("data").split("\\|");
            ((TextView) findViewById(R.id.textView)).setText(data.getStringExtra("data"));
            sendImagesToServer(imagesPath);
        }
    }

    // ESTO ES PARA PROBAR !
    private void sendImagesToServer(String[] imagesPath)
    {
        CustomMultipartRequest multiPartRequest = new CustomMultipartRequest(Request.Method.POST,
                MainActivity.this, RouterManager.getInstance().getUrlBase()+"/reportes/create", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                Log.d("REQUEST_RESPONSE",response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.d("REQUEST_RESPONSE",error.toString());
            }
        });
        for (int i = 0; i<imagesPath.length; i++)
        {
            multiPartRequest.addFile("file"+i,imagesPath[i]);

        }
        multiPartRequest.addData("aplicacion","10");
        multiPartRequest.addJsonArray("images",generateImages(imagesPath));
        multiPartRequest.addJsonArray("observaciones",generateObservations(imagesPath));
        multiPartRequest.send();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        PermissionManager.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public JSONArray generateImages(String[] imagesPath)
    {
        JSONArray array = new JSONArray();
        try
        {
            for (int i = 0; i< imagesPath.length; i++)
            {
                JSONObject obj = new JSONObject();
                obj.put("name",new File(imagesPath[i]).getName());
                obj.put("observacion",i);
                array.put(obj);
            }
        }catch (Exception e)
        {
            System.out.println("EXCEPTION BRUJA");
        }
        return array;
    }


    public JSONArray generateObservations(String[] imagesPath)
    {
        JSONArray array = new JSONArray();
        try
        {
            for (int i = 0; i< imagesPath.length; i++)
            {
                JSONObject obj = new JSONObject();
                obj.put("texto","hola "+i);
                obj.put("correlativo",i);
                array.put(obj);
            }
        }catch (Exception e)
        {
            System.out.println("EXCEPTION BRUJA");
        }
        return array;
    }
}
