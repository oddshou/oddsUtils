package com.example.odds.compiler;

import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

/**
 * 针对每一个使用注解的对象对应一个 {@link ProcessObject}对象
 * 包含一个当前元素(Activity),多个成员元素(每一个字段)
 */
class ProcessObject {
    List<ParameterSpec> parameterSpecList = new ArrayList<>();

    List<String> serializeList = new ArrayList<>();
    List<String> parcelableList = new ArrayList<>();

    ProcessObject(Element element, boolean serialize, boolean parcelable, int order) {
        addFile(element, serialize, parcelable, order);
    }

    void addFile(Element element, boolean serialize, boolean parcelable, int order) {
        ParameterSpec android = ParameterSpec.builder(TypeName.get(element.asType()), element.getSimpleName().toString())
                .addModifiers(Modifier.FINAL)
                .build();
        if (order >= 0 && order <= parameterSpecList.size()) {
            parameterSpecList.add(order, android);
        }else {
            parameterSpecList.add(android);
        }
        if (serialize) {
            serializeList.add(element.getSimpleName().toString());
        }
        if (parcelable) {
            parcelableList.add(element.getSimpleName().toString());
        }
    }
}
