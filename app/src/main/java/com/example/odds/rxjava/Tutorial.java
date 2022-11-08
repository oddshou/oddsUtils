package com.example.odds.rxjava;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.functions.Action;

/**
 * ClassName:Toturial
 * Author:oddshou
 * Description: Rxjava 官方教程demo
 * 2022/11/1 1:57 PM
 * Wiki: https://github.com/ReactiveX/RxJava/wiki/How-To-Use-RxJava
 */
public class Tutorial {

    public static void main(String[] args) {
        hello("world", "heihei", "123");
    }


    /**
     * invoke: hello("world", "heihei", "123");
     * output: Hello world!
     *         Hello heihei!
     *         Hello 123!
     *
     */
    public static void hello(String... args) {
//        Flowable.fromArray(args).subscribe(s -> System.out.println("Hello " + s + "!"));
        Flowable.fromArray(args).subscribe(System.out::println, throwable -> {}, () -> System.out.println("complete"));
    }
}
