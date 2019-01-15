package service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author jianyuan.wei@hand-china.com
 * @date 2018/10/28 13:40
 */
public interface DataStreamService {

    /**
     * Controller调用的接口
     * @param page
     * @param size
     * @param type
     * @return
     */
    JSONObject queryDataByType(int page, int size, String type);

    /**
     * 带有分页功能的数据获取
     * @param page
     * @param size
     * @param type
     * @return
     */
    List<Object> getDataStreamByType(Integer page, Integer size, String type);
}
