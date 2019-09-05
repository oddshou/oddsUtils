package com.example.odds.compiler

import com.squareup.kotlinpoet.*
import com.sun.tools.javac.code.Type


import javax.lang.model.element.Element
//import kotlin.reflect.jvm.internal.impl.builtins.jvm.JavaToKotlinClassMap

/**
 * 针对每一个使用注解的对象对应一个 [ProcessObject]对象
 * 包含一个当前元素(Activity),多个成员元素(每一个字段)
 */
internal class ProcessObject(element: Element, serialize: Boolean, parcelable: Boolean, order: Int) {
    var parameterSpecList: MutableList<ParameterSpec> = ArrayList()

    var serializeList: MutableList<String> = ArrayList()
    var parcelableList: MutableList<String> = ArrayList()

    init {
        addFile(element, serialize, parcelable, order)
    }

    fun addFile(element: Element, serialize: Boolean, parcelable: Boolean, order: Int) {
        var typeName = element.asType().asTypeName()
        if (element.asType().toString() == String::class.java.name) {
            //java String 要特殊处理
            typeName = String::class.asTypeName()
        }
//        if(element.asType() is Type)
        if (element.asType().toString() == List::class.java.name) {
            //java List 要特殊处理
            typeName = List::class.asTypeName()
        }
        val android = ParameterSpec.builder(element.simpleName.toString(), typeName)
//                .addModifiers(KModifier.)
                .build()
        if (order >= 0 && order <= parameterSpecList.size) {
            parameterSpecList.add(order, android)
        } else {
            parameterSpecList.add(android)
        }
        if (serialize) {
            serializeList.add(element.simpleName.toString())
        }
        if (parcelable) {
            parcelableList.add(element.simpleName.toString())
        }
    }

//    private fun TypeName.javaToKotlinType(): TypeName = if (this is ParameterizedTypeName) {
//        (rawType.javaToKotlinType() as ClassName).parameterizedBy(
//                *typeArguments.map { it.javaToKotlinType() }.toTypedArray()
//        )
//    } else {
//        val className = JavaToKotlinClassMap.INSTANCE
//                .mapJavaToKotlin(FqName(toString()))?.asSingleFqName()?.asString()
//        if (className == null) this
//        else ClassName.bestGuess(className)
//    }

//    private fun Element.javaToKotlinType(): ClassName? {
//        val className = JavaToKotlinClassMap.INSTANCE.mapJavaToKotlin(FqName(this.asType().asTypeName().toString()))?.asSingleFqName()?.asString()
//        return if (className == null) {
//            null
//        } else {
//            ClassName.bestGuess(className)
//        }
//    }
}
