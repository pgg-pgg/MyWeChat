package com.pgg.mywechatem.Protocol;

import android.os.Message;

import com.pgg.mywechatem.Domian.FaceInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


/**
 * Created by PDD on 2017/12/1.
 */

public class UpdateImageHelper {

    public static Request UpdateImage(File file) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("deadline", String.valueOf(System.currentTimeMillis() / 1000 + 60))
                .addFormDataPart("aid", "1336663")
                .addFormDataPart("Token", "8771bfc294d04602145025ae4ff662d005d0e193:PfLRceYFkiV50_uLPiReCk8XqrU=:eyJkZWFkbGluZSI6MTUwNjMzMzU2NiwiYWN0aW9uIjoiZ2V0IiwidWlkIjoiNjAyNzA4IiwiYWlkIjoiMTMzNjY2MyIsImZyb20iOiJmaWxlIn0=")
                .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file)).build();
        Request request = new Request.Builder().
                url("http://up.imgapi.com/")
                .post(requestBody)
                .build();
        return request;
    }
    public static FaceInfo AnalysisJson(String json){
        FaceInfo faceInfo;
        try {
            JSONObject jo=new JSONObject(json);
            faceInfo=new FaceInfo();
            faceInfo.height=jo.getInt("height");
            faceInfo.width=jo.getInt("width");
            faceInfo.findurl=jo.getString("findurl");
            faceInfo.htmlurl=jo.getString("htmlurl");
            faceInfo.linkurl=jo.getString("linkurl");
            faceInfo.markdown=jo.getString("markdown");
            faceInfo.s_url=jo.getString("s_url");
            faceInfo.t_url=jo.getString("t_url");
            faceInfo.type=jo.getString("type");
            faceInfo.ubburl=jo.getString("ubburl");
            faceInfo.size=jo.getInt("size");
            return faceInfo;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}

