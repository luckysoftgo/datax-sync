package com.application.base.admin.kylin;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.application.base.admin.kylin.bean.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : 孤狼.
 * @NAME: CubeInfoProvider.
 * @DESC: cube 信息的提供.
 **/
@Component
public class CubeInfoProvider {
	/**
	 * cube 的信息.
	 */
	public ConcurrentHashMap<String,Object> cubesMap = new ConcurrentHashMap(32);

	/**
	 * 放入
	 */
	public void put(String key,Object value){
		cubesMap.put(key,value);
	}
	/**
	 * 取出
	 */
	public Object get(String key){
		return cubesMap.get(key);
	}

	/**
	 * json 信息
	 * @param json
	 * @return
	 */
	public ArrayList<CubeInfo> getCubesInfo(String json){
		ArrayList<CubeInfo> cubesList = new ArrayList<>();
		try {
			if (StringUtils.isBlank(json)){
				return cubesList;
			}
			//Json的解析类对象
			JsonParser parser = new JsonParser();
			//将JSON的String 转成一个JsonArray对象
			JsonArray jsonArray = parser.parse(json).getAsJsonArray();
			Gson gson = new Gson();
			//加强for循环遍历JsonArray
			for (JsonElement element : jsonArray) {
				//使用GSON，直接转成Bean对象
				CubeInfo info = gson.fromJson(element, CubeInfo.class);
				if (info!=null && info.getInput_records_count()>0 && info.getInput_records_size()>0){
					setSchem(info);
					cubesList.add(info);
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return cubesList;
	}
	
	/**
	 * 获得schem信息
	 * @param info
	 * @return
	 */
	private void setSchem(CubeInfo info){
		Map<String,String> schemMap = info.getSegments()[0].getDictionaries();
		for (Map.Entry<String,String> entry : schemMap.entrySet()) {
			// result="/dict/USE_AND_DROP.CREDIT_RISK_DATA/TERM/108d18c5-ed99-10e5-16d5-572827fc3e4d.dict"
			String tmpStr = entry.getValue();
			String[] values = tmpStr.split("/");
			String schem = values[2];
			String[] finals = schem.split("\\.");
			info.setTable_SCHEM(finals[0]);
			info.setTable_Name(finals[1]);
			break;
		}
	}

	/**
	 * json 信息
	 * @param json
	 * @return
	 */
	public List<CubeDescInfo> getCubeDescInfo(String json, CubeInfo info){
		ArrayList<CubeDescInfo> cubeDescList = new ArrayList<>();
		try {
			if (StringUtils.isBlank(json)){
				return cubeDescList;
			}
			//Json的解析类对象
			JsonParser parser = new JsonParser();
			//将JSON的String 转成一个JsonArray对象
			JsonArray jsonArray = parser.parse(json).getAsJsonArray();
			Gson gson = new Gson();
			//加强for循环遍历JsonArray
			for (JsonElement element : jsonArray) {
				//使用GSON，直接转成Bean对象
				CubeDescInfo descInfo = gson.fromJson(element, CubeDescInfo.class);
				descInfo.setProject(info.getProject());
				descInfo.setSchem_name(info.getTable_SCHEM());
				descInfo.setTable_name(info.getTable_Name());
				descInfo.setInput_records_count(info.getInput_records_count());
				getExecuteSql(descInfo);
				cubeDescList.add(descInfo);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return cubeDescList;
	}
	
	/**
	 * json 信息
	 * @param cubeDescInfo
	 * @return
	 */
	public void getExecuteSql(CubeDescInfo cubeDescInfo){
		String table_schem = cubeDescInfo.getSchem_name();
		String tableName = cubeDescInfo.getTable_name();
		String columns = getColumns(cubeDescInfo);
		cubeDescInfo.setColumns(columns);
		StringBuffer buffer = new StringBuffer("SELECT "+columns);
		List<Measures> measuress = cubeDescInfo.getMeasures();
		List<Measures> tmpMeasuress = new ArrayList<>();
		tmpMeasuress.addAll(measuress);
		for (Measures instance : tmpMeasuress) {
			if (instance.getFunction().getExpression().equalsIgnoreCase(KylinConstant.RAW)){
				measuress.remove(instance);
			}
		}
		if (measuress!=null && measuress.size()>1){
			buffer.append(" , ");
			for (int i = 0; i <measuress.size() ; i++) {
				Measures measures = measuress.get(i);
				Measures.Function instance = measures.getFunction();
				Measures.Parameter parameter = instance.getParameter();
				if (parameter.getType().equalsIgnoreCase(KylinConstant.MEASURES_CONST_TYPE)){
					buffer.append(instance.getExpression()+"("+parameter.getValue()+") AS "+ measures.getName());
				}else {
					if (instance.getExpression().equalsIgnoreCase(KylinConstant.RAW)){
						continue;
					}
					if (instance.getExpression().equalsIgnoreCase(KylinConstant.PERCENTILE_APPROX)){
						buffer.append(instance.getExpression()+"("+parameter.getValue().replace(tableName,"").replace(".","")+","+KylinConstant.PERCENTILE_APPROX_VALUE+") AS "+ measures.getName());
					}else{
						buffer.append(instance.getExpression()+"("+parameter.getValue().replace(tableName,"").replace(".","")+") AS "+ measures.getName());
					}
				}
				if (i!=measuress.size()-1){
					buffer.append(",");
				}
			}
		}
		if (table_schem==null){
			buffer.append(" FROM "+tableName);
		}else{
			buffer.append(" FROM "+table_schem+"."+tableName);
		}
		cubeDescInfo.setFromsql(buffer.toString());
		String groupBy = " GROUP BY "+columns;
		cubeDescInfo.setGroupby(groupBy);
		buffer.append(groupBy);
		cubeDescInfo.setKylinSql(buffer.toString());
		cubeDescInfo.setSql(getExecSql(table_schem,tableName,columns));
		cubeDescInfo.setAllColumns(getApiColumns(cubeDescInfo));
		//cubeDescInfo.setAllColumns(getAllColumns(cubeDescInfo));
		//whereIs(cubeDescInfo,groupBy);
	}

	private String getApiColumns(CubeDescInfo descInfo) {
		StringBuffer buffer = new StringBuffer("");
		try {
			ArrayList<HashMap<String,List<String>>> columns = (ArrayList<HashMap<String, List<String>>>) get(KylinConstant.COLUMN_ARRAY);
			if (columns!=null && columns.size()>0){
				for (HashMap<String,List<String>> map :columns) {
					for (Map.Entry<String,List<String>> entry : map.entrySet()) {
						if (entry.getKey().equalsIgnoreCase(descInfo.getName())){
							List<String> array = entry.getValue();
							for (int i = 0; i < array.size() ; i++) {
								buffer.append(array.get(i));
								if (i!=array.size()-1){
									buffer.append(",");
								}
							}
						}
					}
				}
			}
		}catch (Exception e){
		}
		return buffer.toString();
	}

	private String getExecSql(String table_schem, String tableName, String columns) {
		StringBuffer buffer = new StringBuffer("SELECT "+columns);
		if (table_schem==null){
			buffer.append(" FROM "+tableName);
		}else{
			buffer.append(" FROM "+table_schem+"."+tableName);
		}
		buffer.append(" limit 1");
		return buffer.toString();
	}

	/**
	 *
	 * @param cubeDescInfo
	 */
	private void whereIs(CubeDescInfo cubeDescInfo,String groupBy) {
		String sql = "SELECT PROCESSING_DTTM FROM "+cubeDescInfo.getSchem_name()+"."+cubeDescInfo.getTable_name()+groupBy+" ORDER BY PROCESSING_DTTM DESC LIMIT 1 ";
		cubeDescInfo.setWhereis(sql);
	}

	/**
	 * 获得列信息
	 * @return
	 */
	public String getColumns(CubeDescInfo cubeDescInfo){
		StringBuffer buffer = new StringBuffer();
		List<Dimensions> dimensions = cubeDescInfo.getDimensions();
		int count = dimensions.size();
		for (int i = 0; i <count ; i++) {
			Dimensions info = dimensions.get(i);
			buffer.append(info.getName());
			if (i!=count-1){
				buffer.append(",");
			}
		}
		return buffer.toString();
	}

	/**
	 * 获得所有的列信息
	 * @return
	 */
	public String getAllColumns(CubeDescInfo descInfo){
		ArrayList<CubeModelInfo> models = (ArrayList<CubeModelInfo>) get(KylinConstant.MODEL_ARRAY);
		if (models!=null && models.size()>0){
			int count = models.size();
			for (int i = 0; i <count ; i++) {
				CubeModelInfo info = models.get(i);
				if (descInfo.getModel_name().equalsIgnoreCase(info.getName())){
					return getModelColumns(info);
				}
			}
		}
		return "";
	}

	/**
	 * 获取列信息
	 * @param info
	 * @return
	 */
	private String getModelColumns(CubeModelInfo info) {
		List<String> metrics = info.getMetrics();
		List<Dimensions> dimensions = info.getDimensions();
		Set<String> columns = new HashSet<>();
		for (String instance : metrics) {
			String[] array = instance.split("\\"+KylinConstant.POINT);
			columns.add(array[1]);
		}
		for (Dimensions dimens :dimensions ) {
			 List<String> list = dimens.getColumns();
			for (String column :list ) {
				columns.add(column);
			}
		}
		StringBuffer buffer = new StringBuffer();
		int index = 0,count = columns.size();
		for (String column :columns ) {
			buffer.append(column);
			if (index!=count-1){
				buffer.append(",");
			}
			index++;
		}
		return buffer.toString();
	}
}
