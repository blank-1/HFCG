package forkJoin;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadDemo implements Runnable {
    private /*static*/ int index = 1;
    private final static int MAX = 300;
    private final static Object lock = new Object();


    @Override
    public void run() {
        while (index <= MAX) {
            System.out.println(Thread.currentThread() + " 的号码是：" + (index++));
            System.out.println(execptionBlock().equals("1"));
        }

    }

    public static void main(String[] args) {
        ThreadDemo threadDemo = new ThreadDemo();
        Thread t1 = new Thread(threadDemo, "一号窗口");
        Thread t2 = new Thread(threadDemo, "二号窗口");
        Thread t3 = new Thread(threadDemo, "三号窗口");
        Thread t4 = new Thread(threadDemo, "四号窗口");
        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }


    public  String execptionBlock(){
        return null;
    }

}
