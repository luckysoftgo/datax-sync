package com.application.base.admin.core.route.strategy;

import com.application.base.core.biz.model.ReturnT;
import com.application.base.core.biz.model.TriggerParam;
import com.application.base.admin.core.route.ExecutorRouter;

import java.util.List;

/**
 * Created by admin on 17/3/10.
 */
public class ExecutorRouteFirst extends ExecutorRouter {

    @Override
    public ReturnT<String> route(TriggerParam triggerParam, List<String> addressList){
        return new ReturnT<String>(addressList.get(0));
    }

}
