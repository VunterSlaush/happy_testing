package mota.dev.happytesting.parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mota.dev.happytesting.Consts;
import mota.dev.happytesting.models.User;

/**
 * Created by Slaush on 13/06/2017.
 */

public class UserParser
{
    private static UserParser instance;
    private UserParser(){}

    public static UserParser getInstance()
    {
        if(instance == null)
            instance = new UserParser();
        return instance;
    }

    public List<User> generateUsersList(JSONObject jsonObject, String arrayName) throws JSONException {
        JSONArray array = jsonObject.optJSONArray(arrayName);
        if (array.length() > 0)
        {
            List<User> users = new ArrayList<>();
            for (int i = 0; i<array.length(); i++)
                users.add(generateUserFromJson(array.getJSONObject(i)));

            return users;
        }
        return new ArrayList<>();
    }

    public List<User> generateUsersList(JSONObject jsonObject) throws JSONException
    {
        return generateUsersList(jsonObject,"users");
    }


    public User generateUserFromJson(JSONObject jsonObject)
    {
        User user = new User();
        user.setId(jsonObject.optInt(Consts.USER_ID));
        user.setName(jsonObject.optString(Consts.NAME));
        user.setUsername(jsonObject.optString(Consts.USERNAME));
        return user;
    }
}
