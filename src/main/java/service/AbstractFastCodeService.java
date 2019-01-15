package service;


import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 该类主要用于映射快码数据(由于本公司自身框架存在快码)
 * @author jianyuan.wei@hand-china.com
 * @date 2018/10/22 17:43
 */
public abstract class AbstractFastCodeService {

    /**
     * 快码常量
     */
    protected enum FastCodeEnum {
        FY_PROHIBITION_REASON("FY.PROHIBITION_REASON","禁配原因","proReason"),
        NZ_BREED_STATE("NZ.BREED_STATE","繁殖状态","state");
        private String fastCodeKey;
        private String cnDesc;
        private String enDesc;

        FastCodeEnum(String fastCodeKey, String cnDesc,String enDesc) {
            this.fastCodeKey = fastCodeKey;
            this.cnDesc = cnDesc;
            this.enDesc = enDesc;
        }

        public static final FastCodeEnum getEnumObject(String desc,Boolean isEn) {
            isEn = isEn == null ? true : isEn;
            if(isEn) {
                for(FastCodeEnum tn : values()) {
                    if(tn.getEnDesc().equals(desc)) {
                        return tn;
                    }
                }
            } else {
                for(FastCodeEnum tn : values()) {
                    if(tn.getCnDesc().equals(desc)) {
                        return tn;
                    }
                }
            }
            return null;
        }

        public String getFastCodeKey() {
            return fastCodeKey;
        }

        public String getCnDesc() {
            return cnDesc;
        }

        public String getEnDesc() {
            return enDesc;
        }
    }



    /**
     * 返回一个类型所对应的快码
     * @param keyDesc
     * @return
     */
    protected List<JSONObject> getFastCodeByType(String[] keyDesc) {
        List<JSONObject> fastCodeList = new ArrayList<>();
        for (String fastCodeEnDesc : keyDesc) {
            FastCodeEnum fastCodeEnum = FastCodeEnum.getEnumObject(fastCodeEnDesc, true);
            if( fastCodeEnum != null) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name",fastCodeEnDesc);
                jsonObject.put("code",fastCodeEnum.getFastCodeKey());
                fastCodeList.add(jsonObject);
            }
        }
        return fastCodeList;
    }

}
