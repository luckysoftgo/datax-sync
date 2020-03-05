package com.application.base.admin.core.kill;

import com.application.base.admin.core.trigger.JobTrigger;
import com.application.base.core.biz.model.ReturnT;
import com.application.base.core.biz.model.TriggerParam;
import com.application.base.core.enums.ExecutorBlockStrategyEnum;
import com.application.base.core.glue.GlueTypeEnum;

import java.util.Date;

/**
 * datax-job trigger
 * Created by admin on 2019/12/15.
 */
public class KillJob {

    /**
     *
     * @param logId
     * @param address
     * @param processId
     */
    public static ReturnT<String> trigger(long logId,Date triggerTime, String address,String processId) {
        ReturnT<String> triggerResult = null;
        TriggerParam triggerParam = new TriggerParam();
        triggerParam.setJobId(-1);
        triggerParam.setExecutorHandler("killJobHandler");
        triggerParam.setProcessId(processId);
        triggerParam.setLogId(logId);
        triggerParam.setGlueType(GlueTypeEnum.BEAN.getDesc());
        triggerParam.setExecutorBlockStrategy(ExecutorBlockStrategyEnum.SERIAL_EXECUTION.getTitle());
        triggerParam.setLogDateTime(triggerTime.getTime());
        if (address != null) {
            triggerResult = JobTrigger.runExecutor(triggerParam, address);
        } else {
            triggerResult = new ReturnT<>(ReturnT.FAIL_CODE, null);
        }
        return triggerResult;
    }

}
