package com.pgg.mywechatem.Protocol;

import com.alibaba.fastjson.JSON;
import com.pgg.mywechatem.Domian.User;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by PDD on 2017/11/29.
 */

public class HttpHelper {
    public static final OkHttpClient client=new OkHttpClient();
    public static String getJson(RequestBody body,String url){
        final Request request=new Request.Builder().url(url).post(body).build();
        Response response=null;
        try {
            response=client.newCall(request).execute();
            if (response.isSuccessful()){
                return response.body().string();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public static int ParseJsonCode(String json){
        if (json!=null){
            try {
                JSONObject jsonObject=new JSONObject(json);
                if (jsonObject.has("code")){
                    int code=jsonObject.getInt("code");
                    return code;
                }else {
                    return -100;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return -100;
    }

    public static String ParseJsonMessage(String json){
        try {
            JSONObject jsonObject=new JSONObject(json);
            if (jsonObject.has("message")){
                return jsonObject.getString("message");
            }else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static User ParseJsonUser(String json){
        try {
            com.alibaba.fastjson.JSONObject jsonObject=JSON.parseObject(json);
            com.alibaba.fastjson.JSONObject data=jsonObject.getJSONObject("data");
            User user=JSON.parseObject(data.toJSONString(),User.class);
            return user;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
