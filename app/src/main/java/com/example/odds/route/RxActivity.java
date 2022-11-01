package com.example.odds.route;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.odds.R;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableEmitter;
import io.reactivex.rxjava3.core.FlowableOnSubscribe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.BiFunction;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class RxActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx);
        map();
    }

    private void rx1() {
        //创建发送者
        Observable<Apple> observable = Observable.create(new ObservableOnSubscribe<Apple>() {
            @Override
            public void subscribe(ObservableEmitter<Apple> emitter) throws Exception {
                emitter.onNext(new Apple());
                emitter.onComplete();
                /**
                 * 上游可以发送无限个onNext, 下游也可以接收无限个onNext.
                 *  当上游发送了一个onComplete后, 上游onComplete之后的事件将会继续发送, 而下游收到onComplete事件之后将不再继续接收事件.
                 *  当上游发送了一个onError后, 上游onError之后的事件将继续发送, 而下游收到onError事件之后将不再继续接收事件.
                 *  上游可以不发送onComplete或onError.
                 *  最为关键的是onComplete和onError必须唯一并且互斥, 即不能发多个onComplete, 也不能发多个onError, 也不能先发一个onComplete, 然后再发一个onError, 反之亦然
                 */


            }
        });

        Observer<Apple> observerApple = new Observer<Apple>() {
            @Override
            public void onSubscribe(Disposable d) {
                //调用dispose()并不会导致上游不再继续发送事件, 上游会继续发送剩余的事件.
            }

            @Override
            public void onNext(Apple apple) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        observable.subscribe(observerApple);
        /**
         * public final Disposable subscribe() {}
         *     public final Disposable subscribe(Consumer<? super T> onNext) {}
         *     public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError) {}
         *     public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete) {}
         *     public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete, Consumer<? super Disposable> onSubscribe) {}
         *     public final void subscribe(Observer<? super T> observer) {}
         *     onNext 参数表示只关心 onnext事件
         */

    }

    private void map() {
        Disposable subscribe =
                Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.i("rx_call", Thread.currentThread().getName());

                emitter.onNext("dd");
                emitter.onComplete();
            }
        })
                //1，修改发送方线程，接收方线程跟随发送方线程
                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
                //1，修改接收方线程
//                .observeOn(Schedulers.io())
                .map(new Function<String, Float>() {
                    @Override
                    public Float apply(String s) throws Exception {
                        Log.i("rx_map", Thread.currentThread().getName());
                        return 88F;
                    }
                }).subscribe(new Consumer<Float>() {
                    @Override
                    public void accept(Float aFloat) throws Exception {
                        Log.i("rx_subscribe", Thread.currentThread().getName());
                    }
                });
        Log.i("rx_map", " main over");
        compositeDisposable.add(subscribe);
    }

    private void flatmap(){
        Observable.create(new ObservableOnSubscribe<Apple>() {
            @Override
            public void subscribe(ObservableEmitter<Apple> emitter) throws Exception {

            }
        }).flatMap(new Function<Apple, ObservableSource<Orange>>() {
            @Override
            public ObservableSource<Orange> apply(Apple apple) throws Exception {
                return null;
            }
        }).subscribe(new Consumer<Fruit>() {
            @Override
            public void accept(Fruit fruit) throws Exception {

            }
        });
    }

    private void zip(){
        Observable<Apple> appleObservable = Observable.create(new ObservableOnSubscribe<Apple>() {
            @Override
            public void subscribe(ObservableEmitter<Apple> emitter) throws Exception {

            }
        });

        Observable<Orange> orangeObservable = Observable.create(new ObservableOnSubscribe<Orange>() {
            @Override
            public void subscribe(ObservableEmitter<Orange> emitter) throws Exception {

            }
        });

        Observable.zip(appleObservable, orangeObservable,
                new BiFunction<Apple, Orange, String>() {
                    @Override
                    public String apply(Apple apple, Orange orange) throws Exception {
                        return null;
                    }
                }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {

            }
        });
    }

    Subscription subscribe;
    private void request(){
        //每次调用执行一次拉取，称为响应式拉取
        subscribe.request(1);
    }
    private void flowable(){
        Flowable.create(new FlowableOnSubscribe<Apple>() {
            @Override
            public void subscribe(FlowableEmitter<Apple> emitter) throws Exception {
                long requested = emitter.requested();   //当前下游需要发送的数量
                //
                emitter.onNext(new Apple());
            }
        }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Apple>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        subscribe = s;
                        s.request(1);
                    }

                    @Override
                    public void onNext(Apple apple) {
                        apple.toString();
                        subscribe.request(1);
                    }

                    @Override
                    public void onError(Throwable t) {
                        subscribe.cancel();
                    }

                    @Override
                    public void onComplete() {
                        subscribe.cancel();
                    }
                });

    }

    private void from(){
//        Observable.fromFuture(new FutureTask<>())
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    public void clickMap(View view) {
        map();
    }

    public void newThread(View view) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                System.out.println("创建新线程");
            }
        }.start();
    }

    static class Fruit {
    }

    static class Orange extends Fruit{}

    static class Apple extends Fruit {
    }

    static class GreenApple extends Apple {
    }
}
