package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author jianyuan.wei@hand-china.com
 * @date 2018/10/24 13:11
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Type {

    /**
     * 用于指定类型
     * @return
     */
    String name();
}
