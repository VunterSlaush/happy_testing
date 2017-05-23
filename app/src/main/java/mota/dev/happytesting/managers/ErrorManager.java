package mota.dev.happytesting.managers;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;

/**
 * Created by user on 23/05/2017.
 */

public class ErrorManager
{
    private static ErrorManager instance;

    private ErrorManager()
    {

    }

    public static ErrorManager getInstance()
    {
        if(instance == null)
            instance = new ErrorManager();
        return instance;
    }

    public Throwable handleLoginError(Throwable e)
    {
        if(e instanceof NoConnectionError)
        {
            return new Throwable("Error de Conexion");
        }
        else if(e instanceof AuthFailureError)
        {
            return new Throwable("Credenciales Invalidas");
        }
        else
        {
            return new Throwable("Error de Conexion");
        }
    }

}
