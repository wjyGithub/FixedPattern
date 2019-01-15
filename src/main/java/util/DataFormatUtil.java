package util;

import com.alibaba.fastjson.JSONObject;
import exception.HapException;
import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 *该类主要用于构造返回的JSONObject数据格式
 * @author jianyuan.wei@hand-china.com
 * @date 2018/9/25 10:10
 */
public class DataFormatUtil {

    private static final String PRE_DATA = "data_";

    private DataFormatUtil(){}

    /**
     * {
     *     "title":"项目",
     *     "dataIndex":"data_0",
     *     "key":"data_0"
     * }
     * 构建单个表头的jsonObject对象
     * @param title 表头名
     * @param key key值
     * @return
     */
    public static JSONObject buildTableHeadJsonObject(String title, String key) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title",title);
        jsonObject.put("dataIndex",key);
        jsonObject.put("key",key);
        return jsonObject;
    }

    /**
     * [
     *  {
     *     "title":"项目",
     *     "dataIndex":"name",
     *     "key":"name"
     * },
     * {
     *     "title":"均值",
     *     "dataIndex":"avg",
     *     "key":"avg"
     * },
     * {
     *     "title":"合计",
     *     "dataIndex":"sum",
     *     "key":"sum"
     * },
     * //数据部分。------
     * {
     *     "title":"2018-01-02",
     *     "dataIndex":"data_0",
     *     "key":"data_0"
     * }
     * ......
     *
     * ]
     * 构建带有附加信息的单行表头信息(项目名,均值,合计)
     * @param desc 为null时，表示不存在该行
     * @param data
     * @param avg 为null时，表示不存在该行
     * @param sum 为null时，表示不存在该行
     * @return
     */
    public static List<JSONObject> buildSimpleTableHeadJSONObjectWithAttach(String desc,
                                                                            List<? extends String> data,
                                                                            String avg,
                                                                            String sum) {

        List<JSONObject>  jsonObjectList = new ArrayList<>();
        if(!StringUtils.isEmpty(desc)) {
            JSONObject nameJSONObject =  buildTableHeadJsonObject(desc,"name");
            jsonObjectList.add(nameJSONObject);
        }
        if(!StringUtils.isEmpty(avg)) {
            JSONObject avgJSONObject = buildTableHeadJsonObject(avg,"avg");
            jsonObjectList.add(avgJSONObject);
        }
        if(!StringUtils.isEmpty(sum)) {
            JSONObject sumJSONObject = buildTableHeadJsonObject(sum,"sum");
            jsonObjectList.add(sumJSONObject);
        }
        List<JSONObject> dataJSONObjectList = buildSimpleTableHeadJsonObject(data);
        jsonObjectList.addAll(dataJSONObjectList);
        return jsonObjectList;
    }
    /**
     * [
     * {
     *     "title":"2018-01-02",
     *     "dataIndex":"data_0",
     *     "key":"data_0"
     * },
     *  {
     *     "title":"2018-01-03",
     *     "dataIndex":"data_1",
     *     "key":"data_1"
     * },
     * ......
     * ]
     * 构建整行的表头信息
     * @param data 表头数据
     * @return
     */
    public static List<JSONObject> buildSimpleTableHeadJsonObject(List<? extends String> data) {
        List<JSONObject> jsonObjectList = new ArrayList<>();
        for(int i=0; i<data.size(); i++) {
            JSONObject columnJSONObject = buildTableHeadJsonObject((String)data.get(i),PRE_DATA+i);
            jsonObjectList.add(columnJSONObject);
        }
        return jsonObjectList;
    }

    /**
     * [{
     *     "title":"牛号",
     *     "dataIndex":"cowNum",
     *     "key":"cowNum"
     * }, {
     *     "title":"牛舍",
     *     "dataIndex":"houseName",
     *     "key":"houseName"
     * }
     * ]
     * 构建带有具体key的表头信息
     * @param data
     * @param key
     * @return
     */
    public static List<JSONObject> buildSimpleTableHeadWithKey(List<String> data, List<String> key) {
        if(data == null || key == null || data.size() != key.size()) {
            throw new HapException("构建表头参数不正确");
        }
        List<JSONObject> jsonObjects = new ArrayList<>();
        for(int i=0; i < data.size(); i++) {
           JSONObject jsonObject = buildTableHeadJsonObject(data.get(i),key.get(i));
           jsonObjects.add(jsonObject);
        }
        return jsonObjects;
    }

    /**
     * {
     *     "name":"泌乳牛"
     *     "avg":平均值
     *     "sum":总和
     *     "data_0":1
     *     "data_1":2
     *     ....
     * }
     * 用于构建第一列带有项目名称的单行数据
     * @param desc 项目名
     * @param data  数据格式
     * @return
     */
    public static JSONObject buildSimpleJsonObjectWithAttach(String desc, List<?> data,
                                                             Double avg, Long sum) {

        JSONObject jsonObject = new JSONObject(treeMapWithCustomSort(null,true));
        jsonObject.put("name",desc);
        //如果值为null,不显示该行
        jsonObject.put("avg",avg);
        jsonObject.put("sum",sum);
        buildSimpleJsonObject(jsonObject,data);
        return jsonObject;
    }

    /**
     * {
     *     "data_0":1,
     *     "data_1":2,
     *     ......
     * }
     * 用于构建带有单行数据
     * @param jsonObject
     * @param data 构建数据
     * @return
     */
    public static JSONObject buildSimpleJsonObject(JSONObject jsonObject, List<?> data) {
        if(jsonObject != null) {
            for(int i=0; i<data.size(); i++) {
                jsonObject.put(PRE_DATA+i,data.get(i));
            }
            return jsonObject;
        }else {
            JSONObject jsonObject1 = new JSONObject();
            for(int i=0; i<data.size(); i++) {
                jsonObject1.put(PRE_DATA+i,data.get(i));
            }
            return jsonObject1;
        }
    }

    /**
     *
     * rules:[1,2,4,5,6,8,0]
     * 自定义排序规则是指在给定的规则数组中,Map的key值是按照给定数组的顺序进行排序的
     * @param rules 指定的规则数组
     * @param sort 当规则数组未指定时,使用默认的排序规则,sort指出升序还是降序
     *             true :升序  false:降序
     * @return
     */
    private static TreeMap treeMapWithCustomSort(List<?> rules,Boolean sort) {
        if(rules==null || rules.isEmpty()) {
            return new TreeMap(((o1, o2) -> sort ? 1 : -1));
        }else {
            //自定义规则
            return new TreeMap((o1, o2) -> rules.indexOf(o1) - rules.indexOf(o2));
        }
    }

}
