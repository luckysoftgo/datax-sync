package com.application.base.admin.util;

import java.util.ArrayList;

import java.util.List;

/**
 * @Author: admin
 * @Data: 2019/12/30 16:19
 * @Description: mysql、oracle数据库类型和hive类型做适配，先实现功能，后期在考虑优化
 */
public class MysqlAndOracleTypeToHive {
    public static List<String> changeType(List<String> type) {
        List<String> hiveType = new ArrayList<>();
//        System.out.println(type.get(0));
        for (int i = 0; i < type.size(); i++) {
//            System.out.println(type.get(i));
            if ("CHAR".equalsIgnoreCase(type.get(i)) || "VARCHAR".equalsIgnoreCase(type.get(i)) || "TINYBLOB".equalsIgnoreCase(type.get(i))
                    || "TINYTEXT".equalsIgnoreCase(type.get(i)) || "BLOB".equalsIgnoreCase(type.get(i)) || "TEXT".equalsIgnoreCase("type.get(i)")
                    || "MEDIUMBLOB".equalsIgnoreCase(type.get(i)) || "MEDIUMTEXT".equalsIgnoreCase(type.get(i)) || "LONGBLOB".equalsIgnoreCase(type.get(i))
                    || "LONGTEXT".equalsIgnoreCase(type.get(i)) || "VARCHAR2".equalsIgnoreCase(type.get(i)) || "NCHAR".equalsIgnoreCase(type.get(i)) || "NVARCHAR".equalsIgnoreCase(type.get(i)) || "NVARCHAR2".equalsIgnoreCase(type.get(i))
                    || "RAW".equalsIgnoreCase(type.get(i)) || "LONG RAW".equalsIgnoreCase(type.get(i)) || "CLOB".equalsIgnoreCase("type.get(i)")
                    || "NCLOB".equalsIgnoreCase(type.get(i)) || "BFILE".equalsIgnoreCase(type.get(i))) {
                hiveType.add(i, "string");
            } else if ("TINYINT".equalsIgnoreCase(type.get(i)) || "SMALLINT".equalsIgnoreCase(type.get(i)) || "INT".equalsIgnoreCase(type.get(i)) || "BIGINT".equalsIgnoreCase(type.get(i))
                    || "FLOAT".equalsIgnoreCase(type.get(i)) || "DOUBLE".equalsIgnoreCase(type.get(i)) || "DECIMAL".equalsIgnoreCase(type.get(i)) || "DATE".equalsIgnoreCase(type.get(i)) || "TIMESTAMP".equalsIgnoreCase(type.get(i))) {
                hiveType.add(i, type.get(i));
            } else if ("LONG".equalsIgnoreCase(type.get(i))) {
                hiveType.add(i, "bigint");
            } else if ("DATETIME".equalsIgnoreCase(type.get(i))) {
                hiveType.add(i, "timestamp");
            } else if ("NUMBER".equalsIgnoreCase(type.get(i))) {
                hiveType.add(i, "double");
            } else {
                hiveType.add(i, "string");
            }
        }
        return hiveType;
    }
}
