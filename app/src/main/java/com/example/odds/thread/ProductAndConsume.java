package com.example.odds.thread;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author oddshou
 * 2022/12/12 5:11 PM
 */
public class ProductAndConsume {

    private AtomicInteger lastCount = new AtomicInteger(0);
    private AtomicInteger consumeCount = new AtomicInteger(0);


    public void startProduct() {
        new Thread("product") {
            @Override
            public void run() {
                super.run();
                while (true) {
                    if (consumeCount.get() == 10) {
                        System.out.println(Thread.currentThread().getName() + " end " + System.currentTimeMillis());
                        return;
                    }
                    if (lastCount.get() <= 3) {
                        try {
                            Thread.sleep(1000);
                            lastCount.getAndIncrement();
                            System.out.println(Thread.currentThread().getName() + " 生产 " + lastCount);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        synchronized (lastCount) {
                            lastCount.notify();
                        }
                    }
                    if (lastCount.get() > 3) {
                        synchronized (lastCount) {
                            try {
                                lastCount.notify();
                                System.out.println(Thread.currentThread().getName() + " 进入等待 " + lastCount);
                                lastCount.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }.start();


    }

    public void startConsume() {
        new Thread("consume") {
            @Override
            public void run() {
                super.run();
                while (true) {
                    if (lastCount.get() > 0) {
                        try {
                            lastCount.decrementAndGet();
                            consumeCount.incrementAndGet();
                            Thread.sleep(1000);
                            System.out.println("消费 " + lastCount);
                            if (consumeCount.get() == 10) {
                                System.out.println(Thread.currentThread().getName() + " end " +  + System.currentTimeMillis());
                                return;
                            }
                            synchronized (lastCount) {
                                lastCount.notify();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        synchronized (lastCount) {
                            try {
                                lastCount.notify();
                                System.out.println(Thread.currentThread().getName() + " 进入等待 " + lastCount);
                                lastCount.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                }
            }
        }.start();
    }

    public static void main(String[] args) {
        ProductAndConsume test = new ProductAndConsume();
        System.out.println("start " + System.currentTimeMillis());
        test.startProduct();
        test.startConsume();
    }
}
