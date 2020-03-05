package com.application.base.admin.util;

import com.application.base.admin.config.PlatformConfig;
import com.application.base.admin.entity.JobJdbcDatasource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OpenApiUtil {

    /**
     * 数据源设置.
     */
    public static final ConcurrentHashMap<Long, JobJdbcDatasource>  remoteDataSourceMap = new ConcurrentHashMap<Long, JobJdbcDatasource>();

    @Autowired
    private PlatformConfig platformConfig;
    @Autowired
    private RestTemplate trestTemplate;
	
	private static RestTemplate restTemplate;
    private static String tokenUrl = null;

    @PostConstruct
    public void init() {
        tokenUrl = platformConfig.getTokenUrl();
        restTemplate = trestTemplate;
    }

    private static String microServiceToken = null;
    private static String getHeaderToken(boolean reGet) throws RestClientException {
        if(microServiceToken==null || reGet){
            Map<String,String> authRet = restTemplate.postForEntity(tokenUrl,null,Map.class).getBody();
            microServiceToken = "bearer "+authRet.get("access_token");
        }
        return microServiceToken;
    }
    public static String get(String url) throws RestClientException {
        String ret = get(url,true);
        System.out.println("######################open-api return: "+ret);
        return ret;
    }
    private static String get(String url,boolean isFirst) throws RestClientException {
        HttpEntity<String> entity = buildHttpEntity();
        try {
            return restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
        }catch (RestClientException e){
            return doIfTokenExpire(isFirst,url,e);
        }
    }

    private static HttpEntity<String> buildHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
        headers.set("Accept","application/json;charset=UTF-8");
        headers.set("Accept-Charset","UTF-8");
        String token = getHeaderToken(false);
        System.out.println("######################micro-token: "+token);
        headers.set("Authorization",token);
        return new HttpEntity<String>(null, headers);
    }

    private static String doIfTokenExpire(boolean isFirst,String url,RestClientException e) throws RestClientException {
        String errMsg = getErrMsg(e);
        if(isFirst && errMsg!=null && errMsg.contains("401 Unauthorized")){
            getHeaderToken(true);
            return get(url,false);
        }else{
            throw e;
        }
    }

    private static String getErrMsg(Exception e) {
        String errMsg = null;
        if(e.getCause()!=null) {
            errMsg = e.getCause().getMessage();
        }else{
            errMsg = e.getMessage();
        }
        return errMsg;
    }

}
