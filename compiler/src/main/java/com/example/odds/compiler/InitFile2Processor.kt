package com.example.odds.compiler

import com.example.odds.annotations.InitFile
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import java.io.IOException

import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

/**
 * public static Intent prepareIntent(Context context, String url）
 */
@AutoService(Processor::class)
class InitFile2Processor : AbstractProcessor() {

    //Element 所在元素，ProcessObject 处理对象
    private var processObjectList: MutableMap<Element, ProcessObject>? = null

    @Synchronized
    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        processObjectList = HashMap()
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        val set = HashSet<String>()
        set.add(InitFile::class.java.name)
        return set
    }

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        //获取指定类型注解集合
        val initFileAnnotations = roundEnv.getElementsAnnotatedWith(InitFile::class.java)
        //需要根据所在类型单独生成一个方法，并把他们作为该方法参数
        initFileAnnotations.forEach { element ->
            val parcelable = element.getAnnotation(InitFile::class.java).Parcelable
            val serializable = element.getAnnotation(InitFile::class.java).Serializable
            val order = element.getAnnotation(InitFile::class.java).order
            //得到所在类名
            val clazz = element.enclosingElement

            //返回元素的超类，包括接口，也会显示在返回列表的最后
            //            processingEnv.getTypeUtils().directSupertypes(element.asType());
            if (processObjectList!!.containsKey(clazz)) {
                processObjectList!![clazz]?.addFile(element, serializable, parcelable, order)
            } else {
                processObjectList!![clazz] = ProcessObject(element, serializable, parcelable, order)
            }
        }
        createJava()
        return true
    }

    private fun createPreIntent(currentElement: Element, processObject: ProcessObject): FunSpec {
        //statementList
        val parm = CodeBlock.builder()
        processObject.parameterSpecList.forEach { item ->
            when {
                /*(java.io.Serializable)*/
                processObject.serializeList.contains(item.name) -> parm.add("intent.putExtra(%S, %L as java.io.Serializable)\n", item.name, item.name)
                processObject.parcelableList.contains(item.name) -> parm.add("intent.putParcelableArrayListExtra(%S, %L)\n", item.name, item.name)
                else -> parm.add("intent.putExtra(%S, %L);\n", item.name, item.name)
            }
        }

        val contextClass = ClassName("android.content", "Context")
        val intentClass = ClassName("android.content", "Intent")
        return FunSpec.builder("preIntent_" + currentElement.simpleName)
                .addModifiers(KModifier.PUBLIC/*, KModifier.STATIC*/)
                .returns(intentClass)
                .addParameter("context", contextClass)    //额外添加context参数作为第一个必传参数
                .addParameters(processObject.parameterSpecList)
                .addStatement("val intent = Intent(context, %T::class.java)", currentElement.asType().asTypeName())
                .addStatement("%L", parm.build())
                .addStatement("return intent")//addStatement 末尾会加上分号和换行
                .build()
    }

    private fun createOnSave(currentElement: Element, processObject: ProcessObject): FunSpec {
        //statementList
        val parm = CodeBlock.builder()
        processObject.parameterSpecList.forEach { item ->
            if (processObject.serializeList.contains(item.name)) {
                parm.add("intent.putExtra(%S, activity.%L  as java.io.Serializable)\n", item.name, item.name)
            } else if (processObject.parcelableList.contains(item.name)) {
                parm.add("intent.putParcelableArrayListExtra(%S, activity.%L)\n", item.name, item.name)
            } else {
                parm.add("intent.putExtra(%S, activity.%L)\n", item.name, item.name)
            }
        }

        val outState = ClassName("android.os", "Bundle")
        return FunSpec.builder("onSave_" + currentElement.simpleName)
                .addModifiers(KModifier.PUBLIC)
                .returns(Void.TYPE)
                .addParameter("outState", outState)    //Bundle outState
                .addParameter("activity", currentElement.asType().asTypeName()) //xxActivity activity
                .addStatement("val intent = Intent()")
                .addStatement("%L", parm.build())
                .addStatement("outState.putAll(intent.getExtras())")
                .build()
    }


    private fun createOnCreateSave(currentElement: Element, parmList: List<ParameterSpec>): FunSpec {
        //statementList
        val parm = CodeBlock.builder()
        for (item in parmList) {
            parm.add("val value_%L = bundle?.get(%S)\n", item.name, item.name)
            parm.add("if (value_%L != null) {\n" +
                    "activityJava.%L = value_%L as %T\n" +
                    "}\n",
                    item.name, item.name, item.name, item.type)
        }


        val bundle = ClassName("android.os", "Bundle")
        return FunSpec.builder("onCreate_" + currentElement.simpleName)
                .addModifiers(KModifier.PUBLIC)
                .addParameter("saveInstance", bundle.copy(nullable = true))
                .addParameter("activityJava", currentElement.asType().asTypeName()) //xxActivity activity
                .addStatement("val bundle = saveInstance ?: activityJava.intent.extras\n")
                .addStatement("%L", parm.build())
                .build()
    }

    private fun createJava() {
        if (processObjectList!!.isEmpty()) {
            return
        }

        val fileName = "PreIntent"
        val builder = TypeSpec.classBuilder(fileName)



//        val builder = TypeSpec.classBuilder("PreIntent")
//                .addModifiers(KModifier.OPEN)

        //生成preIntent 方法
        processObjectList!!.forEach { (key, value) ->
            builder.addFunction(createPreIntent(key, value))
            //每生成一个方法，还需要生成一个对应 onSaveInstance 方法，和一个 oncreate 方法
            builder.addFunction(createOnSave(key, value))
            builder.addFunction(createOnCreateSave(key, value.parameterSpecList))
        }

//        val javaFile = JavaFile.builder("com.odds.annotation.processor", builder.build())
//                .build()
//        try {
//            javaFile.writeTo(processingEnv.filer)
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }

        val file = FileSpec.builder("com.odds.annotation.processor", fileName)
                .addType(builder.build())
                .build()

        //val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
        try {
            file.writeTo(processingEnv.filer)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}
