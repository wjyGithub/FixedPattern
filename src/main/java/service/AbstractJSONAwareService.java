package service;

import com.alibaba.fastjson.JSONObject;
import exception.HapException;
import util.ArrayClassUtil;
import util.DataFormatUtil;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * JSON构造器
 * @author jianyuan.wei@hand-china.com
 * @date 2018/10/22 22:52
 */
public abstract class AbstractJSONAwareService extends AbstractFastCodeService implements DataStreamService {

    protected final List<String> STANDARD_TYPE_LIST;

    public AbstractJSONAwareService(List<String> typeList) {
        STANDARD_TYPE_LIST = Collections.unmodifiableList(typeList);
    }

    /**
     * 校验类型是否正确
     * @param type
     * @param message
     */
    private void checkType(String type,String message) {
        if(!STANDARD_TYPE_LIST.contains(type)) {
            throw new HapException(message);
        }
    }
    /**
     * 通过类型查询具体的数据
     * @param page 页数
     * @param size 页面大小
     * @param type 接口类型
     * @return
     */
    @Override
    public JSONObject queryDataByType(int page, int size, String type) {
        checkType(type,"类型指定错误");
        JSONObject cowWaringJson = new JSONObject();
        Class cls = ArrayClassUtil.getClassByType(type);
        String[] propertyNames = ArrayClassUtil.classPropertyAsArrayValue(cls);
        String[] propertyDescs = ArrayClassUtil.fieldDescAsArrayValue(cls);
        List<JSONObject> headerList = DataFormatUtil.buildSimpleTableHeadWithKey(Arrays.asList(propertyDescs),Arrays.asList(propertyNames));
        List<Object> dataList = getDataStreamByType(page,size,type);
        List<JSONObject> fastCodeList = getFastCodeByType(propertyNames);
        cowWaringJson.put("header",headerList);
        cowWaringJson.put("row",dataList);
        cowWaringJson.put("lookUpHeader",fastCodeList);
        return cowWaringJson;
    }


}
