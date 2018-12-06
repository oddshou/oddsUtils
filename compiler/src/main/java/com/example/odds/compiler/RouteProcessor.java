package com.example.odds.compiler;

import com.example.odds.annotations.InitFile;
import com.example.odds.annotations.Route;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 *    public static Intent prepareIntent(Context context, String url）
 */
@AutoService(Processor.class)
public class RouteProcessor extends AbstractProcessor {

    private Messager mMessager;
    private List<MethodSpec> methodSpecList;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mMessager = processingEnv.getMessager();
        methodSpecList = new ArrayList<>();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> set = new HashSet<>();
        set.add(Route.class.getCanonicalName());
        set.add(InitFile.class.getCanonicalName());
        return set;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {


        //获取指定类型注解集合
        Set<? extends Element> initFileAnnotations = roundEnv.getElementsAnnotatedWith(InitFile.class);
        //需要根据所在类型单独生成一个方法，并把他们作为该方法参数
        for (Element element: initFileAnnotations) {
            //如果标注的对象不是FIELD则报错,这个错误其实不会发生因为InitFile的Target只声明为ElementType.FIELD了
            if (element.getKind()!= ElementKind.FIELD) {
                mMessager.printMessage(Diagnostic.Kind.ERROR, "is not a FIELD", element);
            }
            //返回元素的超类，接口类型，如果有将显示在列表的最后
            processingEnv.getTypeUtils().directSupertypes(element.asType());
            //返回此元素直接包含的元素
//            List<? extends Element> list = element.getEnclosedElements();
            //返回元素最内层的元素
            element.getEnclosingElement();

            //得到所在类名
            Element clazz = element.getEnclosingElement();
            //获取类全名
            String elemClass = element.asType().toString();
//            createStaticMethod(element.getSimpleName(), );
            mMessager.printMessage(Diagnostic.Kind.NOTE, element.toString());

        }

        createMain();
        return false;
    }

    private void createStaticMethod(String simpleName, List<ParameterSpec> parmList) {
        //statementList
        CodeBlock.Builder parm = CodeBlock.builder();
        for (ParameterSpec item :
                parmList) {
            parm.add("intent.putExtra($L, $L)", item.name, item.name);
        }

        ClassName context = ClassName.get("android.content.Context", "context");
        MethodSpec methodSpec = MethodSpec.methodBuilder("preIntent_" + simpleName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(ClassName.get("android.content", "Intent"))
                .addParameter(context.getClass(), "context")    //额外添加context参数作为第一个必传参数
                .addParameters(parmList)
                .addStatement("android.content.Intent intent = new Intent(context, $s.class)", simpleName)
                .addStatement(parm.toString())
                .build();

        methodSpecList.add(methodSpec);

    }

    private void createMain() {
        TypeSpec.Builder builder = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        for (MethodSpec item :
                methodSpecList) {
            builder.addMethod(item);
        }


        JavaFile javaFile = JavaFile.builder("com.example.helloworld", builder.build())
                .build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
