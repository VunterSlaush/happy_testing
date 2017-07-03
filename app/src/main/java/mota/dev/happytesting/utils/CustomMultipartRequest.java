package mota.dev.happytesting.utils;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import mota.dev.happytesting.MyApplication;

/**
 * Created by Slaush on 07/05/2017.
 */


public class CustomMultipartRequest extends Request<JSONObject> {


  private final Response.Listener<JSONObject> mListener;
  private final Map<String, File> mFilePartData;
  private final Map<String, String> mStringPart;
  private final Map<String, String> mHeaderPart;
  private final Map<String, String> mJsonPart;

  private MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create();
  private HttpEntity mHttpEntity;




  public CustomMultipartRequest(int method, String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
    super(method, url, errorListener);
    mListener = listener;
    this.mFilePartData = new HashMap<>();
    this.mStringPart = new HashMap<>();
    this.mHeaderPart = new HashMap<>();
    this.mJsonPart = new HashMap<>();
  }


  public static String getMimeType(Context context, String url) {
    Uri uri = Uri.fromFile(new File(url));
    String mimeType = null;
    if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
      ContentResolver cr = context.getContentResolver();
      mimeType = cr.getType(uri);
    } else {
      String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
        .toString());
      mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
        fileExtension.toLowerCase());
    }
    return mimeType;


  }

  private void buildMultipartFileEntity() {
    for (Map.Entry<String, File> entry : mFilePartData.entrySet()) {
      try {

        String key = entry.getKey();
        File file = entry.getValue();
        String mimeType = getMimeType(MyApplication.getInstance(), file.toString());
        mEntityBuilder.addBinaryBody(key, file, ContentType.create(mimeType), file.getName());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void buildMultipartTextEntity() {
    for (Map.Entry<String, String> entry : mStringPart.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      if (key != null && value != null)
        mEntityBuilder.addTextBody(key, value);
    }
  }

  private void buildMultipartJsonEntity()
  {
    for (Map.Entry<String, String> entry : mJsonPart.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      if (key != null && value != null)
        mEntityBuilder.addBinaryBody(key, value.getBytes());
    }
  }

  @Override
  public Map<String, String> getHeaders() throws AuthFailureError {
    return mHeaderPart;
  }


  @Override
  public String getBodyContentType() {
    return mHttpEntity.getContentType().getValue();
  }

  @Override
  public byte[] getBody() throws AuthFailureError
  {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try
    {
      mHttpEntity.writeTo(bos);
    } catch (IOException e)
    {
      VolleyLog.e("IOException writing to ByteArrayOutputStream");
    }
    return bos.toByteArray();
  }

  @Override
  protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
    try
    {
      String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
      return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
    } catch (UnsupportedEncodingException e) {
      return Response.error(new ParseError(e));
    } catch (JSONException je) {
      return Response.error(new ParseError(je));
    }
  }

  @Override
  protected void deliverResponse(JSONObject response)
  {
    mListener.onResponse(response);
  }


  public void addFiles(Map<String,String> files)
  {
    for (Map.Entry<String, String> e : files.entrySet())
    {
      addFile(e.getKey(),e.getValue());
    }
  }

  public void addDataMap(Map<String,String> files)
  {
    for (Map.Entry<String, String> e : files.entrySet())
    {
      addData(e.getKey(),e.getValue());
    }
  }

  public void addJsons(Map<String,JSONObject> files)
  {
    if(files != null)
    {
      for (Map.Entry<String, JSONObject> e : files.entrySet())
      {
        addJson(e.getKey(),e.getValue());
      }
    }
  }

  public void addJsonsArray(Map<String, JSONArray> files)
  {
    for (Map.Entry<String, JSONArray> e : files.entrySet())
    {
      addJsonArray(e.getKey(),e.getValue());
    }
  }

  public void addFile(String fileKey, String uri)
  {
    this.mFilePartData.put(fileKey,new File(uri));
  }

  public void addData(String key, String value)
  {
    this.mStringPart.put(key,value);
  }

  public void addHeader(String key, String value)
  {
    this.mHeaderPart.put(key,value);
  }

  public void addJson(String key, JSONObject value)
  {
    this.mJsonPart.put(key,value.toString());
  }

  public void addJsonArray(String key, JSONArray value)
  {
    this.mJsonPart.put(key,value.toString());
  }

  public void build()
  {
    mEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
    buildMultipartFileEntity();
    buildMultipartTextEntity();
    buildMultipartJsonEntity();
    mHttpEntity = mEntityBuilder.build();

  }
}
