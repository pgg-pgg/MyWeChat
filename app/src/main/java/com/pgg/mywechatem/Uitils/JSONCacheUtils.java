package com.pgg.mywechatem.Uitils;

import com.alibaba.fastjson.util.IOUtils;
import com.pgg.mywechatem.Global.BaseApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by PDD on 2017/12/6.
 */

public class JSONCacheUtils {

    //写缓存
    public static void setCache(String result){
        //url作为文件名，Json作为文件内容
        File FileDir= BaseApplication.getContext().getCacheDir();
        FileWriter writer=null;
        long deadline=System.currentTimeMillis()+30*60*1000;
        File file_cache=new File(FileDir,"app"+"?"+ Constants.SEARCH_MOMENTS);
        try {
            writer = new FileWriter(file_cache);
            writer.write(deadline+"\n");
            writer.write(result);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOUtils.close(writer);
        }
    }
    //读缓存
    public static String getCache(){
        File FileDir= BaseApplication.getContext().getCacheDir();
        BufferedReader bf=null;
        File file_cache=new File(FileDir,"app" + "?" + Constants.SEARCH_MOMENTS);
        if (file_cache.exists()){
            try {
                bf =new BufferedReader(new FileReader(file_cache));
                StringBuffer sb=new StringBuffer();
                long deadline=Long.parseLong(bf.readLine());
                String line=null;
                if (deadline>System.currentTimeMillis()){
                    while((line=bf.readLine())!=null){
                        sb.append(line);
                    }
                    return sb.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                IOUtils.close(bf);
            }
        }
        return null;
    }
}
