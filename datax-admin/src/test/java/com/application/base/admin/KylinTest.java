package com.application.base.admin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.application.base.admin.kylin.bean.CubeDescInfo;
import com.application.base.admin.kylin.bean.CubeModelInfo;
import com.application.base.admin.kylin.client.KylinRestApiClient;
import com.google.gson.Gson;
import com.application.base.admin.kylin.KylinException;
import com.application.base.admin.kylin.config.KylinTaskConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static java.awt.SystemColor.info;

/**
 * Created by bruce on 2020/2/19.
 */
public class KylinTest {
    public static void main(String[] args) throws Exception {
        String method = "GET";
        String param = "/cubes/kll_cube";
        String json = execute(param,method,null);
        System.out.println(json);
        System.out.println("=======================================");
        method = "GET";
        param = "/cube_desc/kll_cube";
        json = execute(param,method,null);
        //Json的解析类对象
        JsonParser parser = new JsonParser();
        //将JSON的String 转成一个JsonArray对象
        JsonArray jsonArray = parser.parse(json).getAsJsonArray();
        Gson gson = new Gson();
        for (JsonElement element : jsonArray) {
            //使用GSON，直接转成Bean对象
            CubeDescInfo descInfo = gson.fromJson(element, CubeDescInfo.class);
            System.out.println(descInfo);
        }
        method = "GET";
        param = "/model/Kll_Model";
        json = execute(param,method,null);
        CubeModelInfo info1 = gson.fromJson(json, CubeModelInfo.class);
        System.out.println(json);
    }

    private static String execute(String param,String method,String body){
        byte[] key = ("ADMIN:KYLIN").getBytes();
        String authToken = new sun.misc.BASE64Encoder().encode(key);
        StringBuffer buffer = new StringBuffer();
        try {
            URL url = new URL("http://192.168.10.185:7070/kylin/api" + param);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //毫秒
            connection.setConnectTimeout(60000);
            //毫秒
            connection.setReadTimeout(50000);
            connection.setRequestMethod(method);
            connection.setDoOutput(true);
            connection.setRequestProperty  ("Authorization", "Basic " + authToken);
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            if(body !=null){
                byte[] outputInBytes = body.getBytes("UTF-8");
                OutputStream os = connection.getOutputStream();
                os.write(outputInBytes);
                os.flush();
                os.close();
            }
            InputStream content = (InputStream)connection.getInputStream();
            BufferedReader reader = new BufferedReader (new InputStreamReader(content));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            content.close();
            reader.close();
            connection.disconnect();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

}
