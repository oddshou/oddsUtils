package com.example.odds.compiler;

import com.example.odds.annotations.InitFile;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * public static Intent prepareIntent(Context context, String url）
 */
@AutoService(Processor.class)
public class InitFileProcessor extends AbstractProcessor {

    //Element 所在元素，ProcessObject 处理对象
    private Map<Element, ProcessObject> processObjectList;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        processObjectList = new HashMap<>();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> set = new HashSet<>();
        set.add(InitFile.class.getCanonicalName());
        return set;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //获取指定类型注解集合
        Set<? extends Element> initFileAnnotations = roundEnv.getElementsAnnotatedWith(InitFile.class);
        //需要根据所在类型单独生成一个方法，并把他们作为该方法参数
        for (Element element : initFileAnnotations) {
            boolean parcelable = element.getAnnotation(InitFile.class).Parcelable();
            boolean serializable = element.getAnnotation(InitFile.class).Serializable();
            //得到所在类名
            Element clazz = element.getEnclosingElement();

            //返回元素的超类，包括接口，也会显示在返回列表的最后
//            processingEnv.getTypeUtils().directSupertypes(element.asType());
            if (processObjectList.containsKey(clazz)) {
                processObjectList.get(clazz).addFile(element, serializable, parcelable);
            } else {
                processObjectList.put(clazz, new ProcessObject(element, serializable, parcelable));
            }
        }
        createJava();
        return true;
    }

    private MethodSpec createPreIntent(Element currentElement, ProcessObject processObject) {
        //statementList
        CodeBlock.Builder parm = CodeBlock.builder();
        for (ParameterSpec item :
                processObject.parameterSpecList) {
            if (processObject.serializeList.contains(item.name)) {
                parm.add("intent.putExtra($S, (java.io.Serializable)$L);\n", item.name, item.name);
            } else if (processObject.parcelableList.contains(item.name)) {
                parm.add("intent.putParcelableArrayListExtra($S, $L);\n", item.name, item.name);
            }else {
                parm.add("intent.putExtra($S, $L);\n", item.name, item.name);
            }
        }

        ClassName contextClass = ClassName.get("android.content", "Context");
        ClassName intentClass = ClassName.get("android.content", "Intent");
        MethodSpec methodSpec = MethodSpec.methodBuilder("preIntent_" + currentElement.getSimpleName())
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(intentClass)
                .addParameter(contextClass, "context")    //额外添加context参数作为第一个必传参数
                .addParameters(processObject.parameterSpecList)
                .addStatement("$T intent = new Intent(context, $T.class)", intentClass, TypeName.get(currentElement.asType()))
                .addStatement(parm.build())
                .addStatement("return intent")//addStatement 末尾会加上分号和换行
                .build();
        return methodSpec;
    }

    private MethodSpec createOnSave(Element currentElement, ProcessObject processObject) {
        //statementList
        CodeBlock.Builder parm = CodeBlock.builder();
        for (ParameterSpec item :
                processObject.parameterSpecList) {
            if (processObject.serializeList.contains(item.name)) {
                parm.add("intent.putExtra($S, (java.io.Serializable)(activity.$L));\n", item.name, item.name);
            } else if (processObject.parcelableList.contains(item.name)) {
                parm.add("intent.putParcelableArrayListExtra($S, activity.$L);\n", item.name, item.name);
            }else {
                parm.add("intent.putExtra($S, activity.$L);\n", item.name, item.name);
            }
        }

        ClassName outState = ClassName.get("android.os", "Bundle");
        ClassName intentClass = ClassName.get("android.content", "Intent");
        MethodSpec methodSpec = MethodSpec.methodBuilder("onSave_" + currentElement.getSimpleName())
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(outState, "outState")    //Bundle outState
                .addParameter(TypeName.get(currentElement.asType()), "activity") //xxActivity activity
                .addStatement("Intent intent = new Intent()")
                .addStatement(parm.build())
                .addStatement("outState.putAll(intent.getExtras())")
                .build();
        return methodSpec;
    }


    private MethodSpec createOnCreateSave(Element currentElement, List<ParameterSpec> parmList) {
        //statementList
        CodeBlock.Builder parm = CodeBlock.builder();
        for (ParameterSpec item :
                parmList) {
            parm.add("Object value_$L = bundle.get($S);\n", item.name, item.name);
            parm.add("if (value_$L != null) {\n" +
                            "activityJava.$L = ($T) value_$L;\n" +
                            "}\n",
                    item.name, item.name, item.type, item.name);
        }

        ClassName contextClass = ClassName.get("android.content", "Context");
        ClassName intentClass = ClassName.get("android.content", "Intent");
        ClassName bundle = ClassName.get("android.os", "Bundle");
        MethodSpec methodSpec = MethodSpec.methodBuilder("onCreate_" + currentElement.getSimpleName())
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(bundle, "saveInstance")
                .addParameter(TypeName.get(currentElement.asType()), "activityJava") //xxActivity activity
                .addStatement("Bundle bundle = saveInstance != null ? saveInstance : activityJava.getIntent().getExtras()")
                .addStatement(parm.build())
                .build();
        return methodSpec;
    }

    private void createJava() {
        if (processObjectList.size() <= 0) {
            return;
        }

        TypeSpec.Builder builder = TypeSpec.classBuilder("PreIntent")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        //生成preIntent 方法
        for (Map.Entry<Element, ProcessObject> entry :
                processObjectList.entrySet()) {
            builder.addMethod(createPreIntent(entry.getKey(), entry.getValue()));
            //每生成一个方法，还需要生成一个对应 onSaveInstance 方法，和一个 oncreate 方法
            builder.addMethod(createOnSave(entry.getKey(), entry.getValue()));
            builder.addMethod(createOnCreateSave(entry.getKey(), entry.getValue().parameterSpecList));
        }

        JavaFile javaFile = JavaFile.builder("com.odds.annotation.processor", builder.build())
                .build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
