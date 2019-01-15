package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author jianyuan.wei@hand-china.com
 * @date 2018/10/22 19:26
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Index {

    /**
     * 在数组的存放位置
     * 或 Map的key值
     *
     * @return
     */
    int index() default 0;

    /**
     * 描述
     * @return
     */
    String name() default "";


}
