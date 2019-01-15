package util;

import annotation.Index;
import annotation.Type;
import exception.HapException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * 该工具类主要用于对一个class进行操作,提供了类属性转数组/map,类描述转数组/map
 * 以及类的Class对象和指定的type进行map映射
 * @author jianyuan.wei@hand-china.com
 * @date 2018/10/22 19:21
 */
public class ArrayClassUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArrayClassUtil.class);

    /**
     * 设置扫描的包路径
     */
    private static String PACKAGE_PATH;

    public void setPackagePath(String packagePath) {
        PACKAGE_PATH = packagePath;
    }


    /**
     * 将一个类的属性名转成Map
     * @param cls
     * @return
     */
    public static Map<Integer,String> classPropertyAsMap(Class<?> cls) {
        Map<Integer,String> map = new TreeMap<>();
        Field[] fields = cls.getDeclaredFields();
        for(Field field : fields) {
            Index index = field.getAnnotation(Index.class);
            if(index == null) {
                continue;
            }
            int i = index.index();
            map.put(i,field.getName());
        }
        return map;
    }

    /**
     * 将字段描述转Map
     * @param cls
     * @return
     */
    public static Map<Integer,String> fieldDescAsMap(Class<?> cls) {
        Map<Integer,String> map = new TreeMap<>();
        Field[] fields = cls.getDeclaredFields();
        for(Field field : fields) {
            Index index = field.getAnnotation(Index.class);
            if(index == null) {
                continue;
            }
            int i = index.index();
            String desc = index.name();
            if(!StringUtils.isEmpty(desc)){
                map.put(i,desc);
            }
        }
        return map;
    }


    /**
     * 类型和class进行映射
     * @param
     * @return
     */
    public static Map<String,Class> ClassAndTypeAsMap(String packagePath) {
        Set<String> classNameSet = PackageUtil.scannerClassFile(packagePath);
        Map<String,Class> map = new HashMap<>();
        for(String className : classNameSet) {
            try {
                Class cls = Class.forName(className);
                Type type = (Type) cls.getAnnotation(Type.class);
                if(type != null) {
                    map.put(type.name(),cls);
                }
            } catch (ClassNotFoundException e) {
                LOGGER.error("系统内部错误:{}",e.getMessage());
                throw new HapException("类加载失败");
            }
        }

        return map;
    }


    /**
     * 将一个类的属性名转成数组
     * @param cls
     * @return
     */
    public static String[] classPropertyAsArrayValue(Class<?> cls) {
        Map<Integer,String> maps = classPropertyAsMap(cls);
        TreeMap<Integer,String> treeMap = new TreeMap(maps);
        int maxKey = treeMap.lastKey();
        String[] propertyNameArray = new String[maxKey + 1];
        for(Map.Entry<Integer,String> entry : maps.entrySet()) {
            int i = entry.getKey();
            String name = entry.getValue();
            propertyNameArray[i] = name;
        }
        return propertyNameArray;
    }

    /**
     * 将一个类的属性描述转成数组
     * @param cls
     * @return
     */
    public static String[] fieldDescAsArrayValue(Class<?> cls) {
        Map<Integer,String> maps = fieldDescAsMap(cls);
        TreeMap<Integer,String> treeMap = new TreeMap(maps);
        int maxKey = treeMap.lastKey();
        String[] propertyNameArray = new String[maxKey + 1];
        for(Map.Entry<Integer,String> entry : maps.entrySet()) {
            int i = entry.getKey();
            String name = entry.getValue();
            propertyNameArray[i] = name;
        }
        return propertyNameArray;
    }

    /**
     * 通过type获取指定Class对象
     * @param type
     * @return
     */
    public static Class<?> getClassByType(String type) {
        Map<String, Class> classMap = ClassAndTypeAsMap(PACKAGE_PATH);
        Class cls = classMap.get(type);
        return cls;
    }

}
