package com.application.base.admin.util;

import com.alibaba.fastjson.JSON;

/**
 *
 * @author admin
 * @ClassName JSONUtils
 * @Version 1.0
 */
public class JSONUtils {

    /**
     * 返回格式化的json
     *
     * @param object
     * @return
     */
    public static String formatJson(Object object) {
        return JSON.toJSONString(object, true);
    }
}
