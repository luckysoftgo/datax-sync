package com.application.base.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.druid.util.JdbcConstants;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.application.base.core.util.Constant;
import com.application.base.admin.config.ElasticConfig;
import com.application.base.admin.config.PlatformConfig;
import com.application.base.admin.entity.ElasticInfo;
import com.application.base.admin.entity.JobJdbcDatasource;
import com.application.base.admin.kylin.CubeInfoProvider;
import com.application.base.admin.kylin.KylinConstant;
import com.application.base.admin.kylin.bean.CubeDescInfo;
import com.application.base.admin.kylin.bean.CubeInfo;
import com.application.base.admin.kylin.client.KylinRestApiClient;
import com.application.base.admin.service.IJobJdbcDatasourceService;
import com.application.base.admin.service.JdbcDatasourceQueryService;
import com.application.base.admin.tool.query.BaseQueryTool;
import com.application.base.admin.tool.query.QueryToolFactory;
import com.application.base.admin.util.ElasticSearchUtil;
import com.application.base.admin.util.RemoteApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

/**
 * TODO
 *
 * @author admin
 * @ClassName JdbcDatasourceQueryServiceImpl
 * @Version 1.0
 * @since 2019/7/31 20:51
 */
@Service
public class JdbcDatasourceQueryServiceImpl implements JdbcDatasourceQueryService {

    @Autowired
    private IJobJdbcDatasourceService jobJdbcDatasourceService;
    @Autowired
    private PlatformConfig platformConfig;
    @Autowired
    private RemoteApiUtil remoteApi;
    @Autowired
    private KylinRestApiClient restApiClient;
    @Autowired
    private CubeInfoProvider infoProvider;
    @Autowired
    private ElasticConfig esConfig;

    @Override
    public List<String> getTables(Long id) {
        List<String> tables = Lists.newArrayList();
        //获取数据源对象
        JobJdbcDatasource jdbcDatasource = getDataSource(id);
        if (JdbcConstants.KYLIN.equalsIgnoreCase(jdbcDatasource.getDatasource())){
            String json = restApiClient.listCubes(0,100,null,null);
            ArrayList<CubeInfo> cubeInfos = infoProvider.getCubesInfo(json);
            ArrayList<CubeInfo> finalCubes = infoProvider.getCubesInfo(json);
            ArrayList<HashMap<String,List<String>>> columns = new ArrayList<>();
            if (!cubeInfos.isEmpty()){
                for (CubeInfo info :cubeInfos ) {
                    //去掉此处的cube信息.
                    if (info.getTable_SCHEM().equalsIgnoreCase(KylinConstant.TEST) || info.getTable_SCHEM().equalsIgnoreCase(KylinConstant.DEFAULT)){
                        finalCubes.remove(info);
                    }
                    //kylin的处理.
                    tables.add(info.getName()+":"+info.getTable_SCHEM()+"."+info.getTable_Name());
                    try {
                        String result = restApiClient.getCubeSql(info.getName());
                        columns.add(getSqlColumns(result,info.getName()));
                    }catch (Exception e){
                    }
                }
                infoProvider.put(KylinConstant.COBE_ARRAY,finalCubes);
                infoProvider.put(KylinConstant.COLUMN_ARRAY,columns);
            }
            return tables;
        }else if (JdbcConstants.ELASTIC_SEARCH.equalsIgnoreCase(jdbcDatasource.getDatasource())||
                KylinConstant.ELASTICSEARCH.equalsIgnoreCase(jdbcDatasource.getDatasource())){
              ElasticInfo elasticInfo = ElasticSearchUtil.getElasticInfo(esConfig);
              if (elasticInfo!=null && elasticInfo.getElasticInfos().size()>0){
                  List<ElasticInfo.EsItemInfo> dataList = elasticInfo.getElasticInfos();
                  for (ElasticInfo.EsItemInfo itemInfo:dataList){
                      tables.add(itemInfo.getIndexName());
                  }
              }
              return tables;
        }else{
            //queryTool组装
            if (ObjectUtil.isNull(jdbcDatasource)) {
                return tables;
            }
            BaseQueryTool queryTool = QueryToolFactory.getByDbType(jdbcDatasource);
            return queryTool.getTableNames();
        }
    }

    private HashMap<String,List<String>> getSqlColumns(String json,String cubeName) {
        HashMap<String,List<String>> map = new HashMap<>();
        try {
            JsonParser parser = new JsonParser();
            JsonObject ret = parser.parse(json).getAsJsonObject();
            String sql = ret.get("sql").getAsString();
            sql = sql.replaceAll("`","").replaceAll("SELECT","").replaceAll("WHERE 1=1","");
            String[] arrary = sql.split("FROM");
            String[] instArr = arrary[0].split(",");
            List<String> columns = new ArrayList<>();
            for (String instance :instArr ) {
                String[] array = instance.split("as");
                columns.add(array[0].split("\\.")[1]);
            }
            map.put(cubeName,columns);
        }catch (Exception e){
        }
        return map;
    }

    @Override
    public String getKylinSql(Long id,String tableName) {
        //获取数据源对象
        JobJdbcDatasource jdbcDatasource = getDataSource(id);
        //queryTool组装
        if (ObjectUtil.isNull(jdbcDatasource)) {
            return null;
        }
        CubeDescInfo descInfo = getCubeDesc(jdbcDatasource.getDatasource(),tableName);
        if (descInfo!=null){
            return descInfo.getSql();
        }
        //返回:信息
        return "";
    }

    @Override
    public List<String> getColumns(Long id, String tableName) {
        //获取数据源对象
        JobJdbcDatasource jdbcDatasource = getDataSource(id);
        //queryTool组装
        if (ObjectUtil.isNull(jdbcDatasource)) {
            return Lists.newArrayList();
        }
        if (JdbcConstants.ELASTIC_SEARCH.equalsIgnoreCase(jdbcDatasource.getDatasource()) ||
                KylinConstant.ELASTICSEARCH.equalsIgnoreCase(jdbcDatasource.getDatasource())){
            return Lists.newArrayList();
        }
        BaseQueryTool queryTool = QueryToolFactory.getByDbType(jdbcDatasource);
        List<String> columns = null;
        if (JdbcConstants.KYLIN.equalsIgnoreCase(jdbcDatasource.getDatasource())){
            String[] array = tableName.split(Constant.SPLIT_SCOLON);
            CubeDescInfo descInfo = getCubeDesc(jdbcDatasource.getDatasource(),array[0]);
            columns = queryTool.getColumnNames(array[1], jdbcDatasource.getDatasource(),descInfo.getAllColumns());
        }else {
            columns = queryTool.getColumnNames(tableName, jdbcDatasource.getDatasource());
        }
        return columns;
    }

    @Override
    public List<String> getElasticColumns(Long id, String tableName) {
        //获取数据源对象
        JobJdbcDatasource jdbcDatasource = getDataSource(id);
        //queryTool组装
        if (ObjectUtil.isNull(jdbcDatasource)) {
            return Lists.newArrayList();
        }
        BaseQueryTool queryTool = QueryToolFactory.getByDbType(jdbcDatasource);
        List<String> columns = null;
        if (JdbcConstants.KYLIN.equalsIgnoreCase(jdbcDatasource.getDatasource())){
            String[] array = tableName.split(Constant.SPLIT_SCOLON);
            CubeDescInfo descInfo = getCubeDesc(jdbcDatasource.getDatasource(),array[0]);
            columns = queryTool.getEsColumnNames(array[1], jdbcDatasource.getDatasource(),descInfo.getAllColumns());
        }else {
            columns = queryTool.getEsColumnNames(tableName, jdbcDatasource.getDatasource());
        }
        return columns;
    }

    @Override
    public List<String> getColumnsByQuerySql(Long datasourceId, String querySql) {
        //获取数据源对象
        JobJdbcDatasource jdbcDatasource = getDataSource(datasourceId);
        //queryTool组装
        if (ObjectUtil.isNull(jdbcDatasource)) {
            return Lists.newArrayList();
        }
        BaseQueryTool queryTool = QueryToolFactory.getByDbType(jdbcDatasource);
        return queryTool.getColumnsByQuerySql(querySql);
    }

    /**
     * 获得配置信息.
     * @param dataSource
     * @param tableName
     * @return
     */
    private CubeDescInfo getCubeDesc(String dataSource,String tableName){
        CubeDescInfo descInfo = null;
        if (JdbcConstants.KYLIN.equalsIgnoreCase(dataSource)){
            ArrayList<CubeInfo> finalCubes = (ArrayList<CubeInfo>) infoProvider.get(KylinConstant.COBE_ARRAY);
            if (finalCubes!=null && finalCubes.size()>0){
                for (CubeInfo info : finalCubes){
                    if (info.getName().equalsIgnoreCase(tableName)){
                        String json = restApiClient.getCubeDesc(info.getName());
                        List<CubeDescInfo> descList = infoProvider.getCubeDescInfo(json,info);
                        if (descList!=null && descList.size()>0){
                            descInfo = descList.get(0);
                        }
                    }
                }
            }
        }
        return descInfo;
    }

    /**
     * 获得数据源
     * @return
     */
    private JobJdbcDatasource getDataSource(Serializable id){
        //获取数据源对象
        JobJdbcDatasource jdbcDatasource =null;
        if (platformConfig.isPswitch()){
            jdbcDatasource = remoteApi.getOneDataSource(id);
        }else{
            jdbcDatasource = jobJdbcDatasourceService.getById(id);
        }
        return jdbcDatasource;
    }
}
