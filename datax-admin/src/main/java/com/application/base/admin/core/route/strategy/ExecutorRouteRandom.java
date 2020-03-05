package com.application.base.admin.core.route.strategy;

import com.application.base.core.biz.model.ReturnT;
import com.application.base.core.biz.model.TriggerParam;
import com.application.base.admin.core.route.ExecutorRouter;

import java.util.List;
import java.util.Random;

/**
 * Created by admin on 17/3/10.
 */
public class ExecutorRouteRandom extends ExecutorRouter {

    private static Random localRandom = new Random();

    @Override
    public ReturnT<String> route(TriggerParam triggerParam, List<String> addressList) {
        String address = addressList.get(localRandom.nextInt(addressList.size()));
        return new ReturnT<String>(address);
    }

}
