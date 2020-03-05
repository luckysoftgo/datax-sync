package com.application.base.admin.util;

import com.application.base.admin.config.ElasticConfig;
import com.application.base.admin.entity.ElasticInfo;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.health.ClusterHealthStatus;
import org.elasticsearch.cluster.health.ClusterIndexHealth;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author admin
 * @ClassName ElasticSearchUtil
 */
public class ElasticSearchUtil {

    static Logger logger = LoggerFactory.getLogger(ElasticSearchUtil.class.getName());

    /**
     * 测试.
     * @param args
     */
    public static void main(String[] args) {
        ElasticConfig config = new ElasticConfig();
        config.setEsCluster("elasticsearch");
        config.setServerIps("192.168.10.216:9300");
        ElasticInfo info = getElasticInfo(config);
        System.out.println(info.getEsClusterName());
    }

    /**
     * 获取配置信息
     * @param config
     * @return
     */
    public static ElasticInfo getElasticInfo(ElasticConfig config) {
        ElasticInfo info = new ElasticInfo();
        try {
            //创建客户端
            TransportClient client = initClient(config.getEsCluster(),config.getServerIps()) ;
            ClusterHealthResponse healths = client.admin().cluster().prepareHealth().get();
            String clusterName = healths.getClusterName();
            info.setEsClusterName(clusterName);
            //输出集群名
            int numberOfDataNodes = healths.getNumberOfDataNodes();
            //输出节点数量
            info.setNumberOfDataNodes(numberOfDataNodes);
            //输出每个索引信息
            List<ElasticInfo.EsItemInfo> elasticInfos = new ArrayList<>();
            for(ClusterIndexHealth health:healths.getIndices().values()) {
                String indexName = health.getIndex();
                if (indexName.startsWith(".mon") || indexName.startsWith(".kiban")){
                    continue;
                }
                ElasticInfo.EsItemInfo itemInfo = new ElasticInfo().new EsItemInfo();
                int numberOfShards = health.getNumberOfShards();
                int numberOfReplicas = health.getNumberOfReplicas();
                ClusterHealthStatus clusterHealthStatus = health.getStatus();
                itemInfo.setIndexName(indexName);
                itemInfo.setNumberOfShards(numberOfShards);
                itemInfo.setNumberOfReplicas(numberOfReplicas);
                itemInfo.setClusterHealthStatus(clusterHealthStatus.toString());
                elasticInfos.add(itemInfo);
            }
            info.setElasticInfos(elasticInfos);
            client.close();
        }catch (Exception e){
            logger.error("获取es的索引失败了,失败信息是:{}",e);
        }
        return info;
    }

    /**
     * 返回创建的客户端信息
     * @param clusterName
     * @param serverIps
     * @return
     */
    private static TransportClient initClient(String clusterName,String serverIps) {
        return initClient(clusterName,true,serverIps);
    }

    /**
     * 返回创建的客户端信息
     * @param clusterName
     * @param isAppend
     * @param serverIps
     * @return
     */
    private static TransportClient initClient(String clusterName,boolean isAppend,String serverIps) {
        Settings settings = Settings.builder()
                // 集群名
                .put("cluster.name", clusterName)
                // 自动把集群下的机器添加到列表中:true.是;false.否
                .put("client.transport.sniff", isAppend)
                // 忽略集群名字验证, 打开后集群名字不对也能连接上
                .put("client.transport.ignore_cluster_name", true)
                .build();
        try {
            TransportClient settingClient = new PreBuiltTransportClient(settings);
            //节点信息
            Map<String, Integer> nodeMap = parseNodeIps(serverIps);
            for (Map.Entry<String, Integer> entry : nodeMap.entrySet()) {
                try {
                    settingClient.addTransportAddress(new TransportAddress(InetAddress.getByName(entry.getKey()), entry.getValue()));
                } catch (UnknownHostException e) {
                    logger.error("添加索引IP,Port出现异常,异常信息是{}",e.getMessage());
                }
            }
            return settingClient;
        }catch (Exception e){
            logger.error("初始化对象实例失败:{}",e);
        }
        return null;
    }

    /**
     * 解析节点IP信息,多个节点用逗号隔开,IP和端口用冒号隔开
     * @return
     */
    private static Map<String, Integer> parseNodeIps(String serverIPs) {
        String[] nodeIpInfoArr = serverIPs.split(",");
        Map<String, Integer> resultMap = new HashMap<String, Integer>(nodeIpInfoArr.length);
        for (String ipInfo : nodeIpInfoArr) {
            String[] ipInfoArr = ipInfo.split(":");
            resultMap.put(ipInfoArr[0], Integer.parseInt(ipInfoArr[1]));
        }
        return resultMap;
    }
}
