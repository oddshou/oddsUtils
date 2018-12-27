package com.example.odds.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * source code https://github.com/oddshou/AndroidSomeTest<p/>
 * 用法：给Activity成员添加 @InitFile 会在PreIntent下生成3个以该Activity名称为后缀的对应方法。包名：odds.annotation.processor<P/>
 * 注意成员定义先后顺序决定preIntent方法参数顺序。参数默认值添加在成员定义处。<p/>
 * order 设置生成方法的参数顺序，从0开始 可以默认不填写。不填则按定义顺序排在末尾
 *
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.CLASS)
public @interface InitFile {
    boolean Serializable() default false;
    boolean Parcelable() default false;
    int order() default -1;
}
