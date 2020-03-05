package com.application.base.admin.tool.meta;

/**
 * kylin元数据信息
 *
 * @author admin
 * @ClassName KylinDatabaseMeta
 * @Version 2.0
 */
public class KylinDatabaseMeta extends BaseDatabaseMeta implements DatabaseInterface {

    private volatile static KylinDatabaseMeta single;

    public static KylinDatabaseMeta getInstance() {
        if (single == null) {
            synchronized (KylinDatabaseMeta.class) {
                if (single == null) {
                    single = new KylinDatabaseMeta();
                }
            }
        }
        return single;
    }

    @Override
    public String getSQLQueryTables(String... args) {
        return "show tables";
    }

}
