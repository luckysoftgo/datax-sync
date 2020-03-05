package com.application.base.core.handler.impl;

import com.application.base.core.handler.IJobHandler;
import com.application.base.core.biz.model.ReturnT;
import com.application.base.core.biz.model.TriggerParam;
import com.application.base.core.log.JobLogger;

/**
 * glue job handler
 * @author admin 2016-5-19 21:05:45
 */
public class GlueJobHandler extends IJobHandler {

	private long glueUpdatetime;
	private IJobHandler jobHandler;
	public GlueJobHandler(IJobHandler jobHandler, long glueUpdatetime) {
		this.jobHandler = jobHandler;
		this.glueUpdatetime = glueUpdatetime;
	}
	public long getGlueUpdatetime() {
		return glueUpdatetime;
	}

	@Override
	public ReturnT<String> execute(TriggerParam tgParam) throws Exception {
		JobLogger.log("----------- glue.version:"+ glueUpdatetime +" -----------");
		return jobHandler.execute(tgParam);
	}
}
