package com.application.base.admin;

import com.google.gson.Gson;
import com.application.base.admin.entity.ElasticInfo;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.health.ClusterHealthStatus;
import org.elasticsearch.cluster.health.ClusterIndexHealth;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @desc es 测试
 * @author 孤狼
 */
public class EsTest {
	
	static String clusterName = "my-application";
	//index
	static String dbName = "bruce1";
	//type
	static String tableName = "brucetest1" ;
	//ips
	static String serverIPs = "192.168.2.169:9300";

	/**
	 * test 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		//test1();
		//test2();
		testInfo();
	}
	
	public static void testInfo() {
		ElasticInfo info = new ElasticInfo();
		try {
			//指定集群
			Settings settings = Settings.builder().put("cluster.name","elasticsearch").build();
			//创建客户端
			TransportClient client = new PreBuiltTransportClient(settings)
					.addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.10.216"),9300));
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
			e.printStackTrace();
		}
		System.out.println(new Gson().toJson(info));
	}
}
