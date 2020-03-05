package com.application.base.admin.tool.datax.reader;

import java.util.Map;

/**
 * mysql reader 构建类
 *
 * @author admin
 * @ClassName MysqlReader
 * @Version 1.0
 */
public class KylinReader extends BaseReaderPlugin implements DataxReaderInterface {

    @Override
    public String getName() {
        return "rdbmsreader";
    }

    @Override
    public Map<String, Object> sample() {
        return null;
    }
}
