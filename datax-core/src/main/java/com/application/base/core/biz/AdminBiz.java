package com.application.base.core.biz;

import com.application.base.core.biz.model.HandleCallbackParam;
import com.application.base.core.biz.model.HandleProcessCallbackParam;
import com.application.base.core.biz.model.RegistryParam;
import com.application.base.core.biz.model.ReturnT;

import java.util.List;

/**
 * @author admin 2017-07-27 21:52:49
 */
public interface AdminBiz {


    // ---------------------- callback ----------------------

    /**
     * callback
     *
     * @param callbackParamList
     * @return
     */
    public ReturnT<String> callback(List<HandleCallbackParam> callbackParamList);

    /**
     * processCallback
     * @param processCallbackParamList
     * @return
     */
    public ReturnT<String> processCallback(List<HandleProcessCallbackParam> processCallbackParamList);

    // ---------------------- registry ----------------------

    /**
     * registry
     *
     * @param registryParam
     * @return
     */
    public ReturnT<String> registry(RegistryParam registryParam);

    /**
     * registry remove
     *
     * @param registryParam
     * @return
     */
    public ReturnT<String> registryRemove(RegistryParam registryParam);

}
