package com.application.base.core.handler;

import com.google.common.collect.Maps;
import com.application.base.core.biz.model.ReturnT;
import com.application.base.core.biz.model.TriggerParam;

import java.util.concurrent.ConcurrentMap;

/**
 * job handler
 *
 * @author admin 2015-12-19 19:06:38
 */
public abstract class IJobHandler {


	/** success */
	public static final ReturnT<String> SUCCESS = new ReturnT<String>(200, null);
	/** fail */
	public static final ReturnT<String> FAIL = new ReturnT<String>(500, null);
	/** fail timeout */
	public static final ReturnT<String> FAIL_TIMEOUT = new ReturnT<String>(502, null);

	public static final ConcurrentMap<String, String> jobTmpFiles = Maps.newConcurrentMap();

	/**
	 * execute handler, invoked when executor receives a scheduling request
	 *
	 * @param tgParam
	 * @return
	 * @throws Exception
	 */
	public abstract ReturnT<String> execute(TriggerParam tgParam) throws Exception;

	/**
	 * init handler, invoked when JobThread init
	 */
	public void init() {
		// do something
	}


	/**
	 * destroy handler, invoked when JobThread destroy
	 */
	public void destroy() {
		// do something
	}


}
