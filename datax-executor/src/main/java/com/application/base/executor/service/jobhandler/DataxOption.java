package com.application.base.executor.service.jobhandler;

/**
 * DataX启动参数
 *
 * @author admin 2019-12-15
 */
public class DataxOption {

    public static final String SPLIT_SPACE = " ";

    public static final String TRANSFORM_SPLIT_SPACE = "\" \"";

    public static final String TRANSFORM_QUOTES = "\"";

    public static final String JVM_CM = "-j";

    public static final String PARAMS_CM = "-p";
	
	public static final String PARAMS_CM_V_PT = "-Dpartition=%s";
	
	public static final String DEFAULT_JSON = "jsons";
	
	public static final String DEFAULT_DATAX_PY = "bin/datax.py";
	
}
