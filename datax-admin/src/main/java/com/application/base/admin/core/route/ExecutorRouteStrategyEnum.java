package com.application.base.admin.core.route;

import com.application.base.admin.core.route.strategy.ExecutorRouteBusyover;
import com.application.base.admin.core.route.strategy.ExecutorRouteConsistentHash;
import com.application.base.admin.core.route.strategy.ExecutorRouteFailover;
import com.application.base.admin.core.route.strategy.ExecutorRouteFirst;
import com.application.base.admin.core.route.strategy.ExecutorRouteLFU;
import com.application.base.admin.core.route.strategy.ExecutorRouteLRU;
import com.application.base.admin.core.route.strategy.ExecutorRouteLast;
import com.application.base.admin.core.route.strategy.ExecutorRouteRandom;
import com.application.base.admin.core.route.strategy.ExecutorRouteRound;
import com.application.base.admin.core.util.I18nUtil;

/**
 * Created by admin on 17/3/10.
 */
public enum ExecutorRouteStrategyEnum {

    FIRST(I18nUtil.getString("jobconf_route_first"), new ExecutorRouteFirst()),
    LAST(I18nUtil.getString("jobconf_route_last"), new ExecutorRouteLast()),
    ROUND(I18nUtil.getString("jobconf_route_round"), new ExecutorRouteRound()),
    RANDOM(I18nUtil.getString("jobconf_route_random"), new ExecutorRouteRandom()),
    CONSISTENT_HASH(I18nUtil.getString("jobconf_route_consistenthash"), new ExecutorRouteConsistentHash()),
    LEAST_FREQUENTLY_USED(I18nUtil.getString("jobconf_route_lfu"), new ExecutorRouteLFU()),
    LEAST_RECENTLY_USED(I18nUtil.getString("jobconf_route_lru"), new ExecutorRouteLRU()),
    FAILOVER(I18nUtil.getString("jobconf_route_failover"), new ExecutorRouteFailover()),
    BUSYOVER(I18nUtil.getString("jobconf_route_busyover"), new ExecutorRouteBusyover()),
    SHARDING_BROADCAST(I18nUtil.getString("jobconf_route_shard"), null);

    ExecutorRouteStrategyEnum(String title, ExecutorRouter router) {
        this.title = title;
        this.router = router;
    }

    private String title;
    private ExecutorRouter router;

    public String getTitle() {
        return title;
    }
    public ExecutorRouter getRouter() {
        return router;
    }

    public static ExecutorRouteStrategyEnum match(String name, ExecutorRouteStrategyEnum defaultItem){
        if (name != null) {
            for (ExecutorRouteStrategyEnum item: ExecutorRouteStrategyEnum.values()) {
                if (item.name().equalsIgnoreCase(name)) {
                    return item;
                }
            }
        }
        return defaultItem;
    }

}
