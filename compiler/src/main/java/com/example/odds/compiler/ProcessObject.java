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

    ProcessObject(Element element) {
        ParameterSpec android = ParameterSpec.builder(TypeName.get(element.asType()), element.getSimpleName().toString())
                .addModifiers(Modifier.FINAL)
                .build();
        parameterSpecList.add(android);
    }

    void addFile(Element element) {
        ParameterSpec android = ParameterSpec.builder(TypeName.get(element.asType()), element.getSimpleName().toString())
                .addModifiers(Modifier.FINAL)
                .build();
        parameterSpecList.add(android);
    }
}
