package forkJoin;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * <pre>
 * TODO
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/16
 */
public class ParallelTaskTest extends RecursiveAction {

    private final int threshold;
    private int[] myTask;

    private int fromIndex;
    private int taskLen;

    public ParallelTaskTest(int[] myTask, int fromIndex, int taskLen, int threshold) {
        this.myTask = myTask;
        this.fromIndex = fromIndex;
        this.taskLen = taskLen;
        this.threshold = threshold;
    }

    private void doTask() {
        int[] currTask = new int[taskLen];
        System.arraycopy(myTask, fromIndex, currTask, 0, taskLen);
        try {
            Thread.sleep(10L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " -- " + Arrays.toString(currTask));
    }

    @Override
    protected void compute() {
        if (taskLen <= threshold) {
            doTask();
            return;
        }
        int split = taskLen / 2;
        invokeAll(new ParallelTaskTest(myTask, fromIndex, split, threshold),
                new ParallelTaskTest(myTask, fromIndex + split, taskLen - split, threshold));

    }

    public static void main(String[] args) {
        int[] chars = new int[10000];
        for (int i = 0; i < chars.length; i++) {
            Arrays.fill(chars, i, i + 1, 1 + i);
        }


        int runTimes = 10;
        long result = 0;


        for (int i = 0; i < runTimes; i++) {
            result += method1(chars);
//            result += method2(chars);
        }

        System.out.printf("run %s times and finally used millis ==> %s\n", runTimes, (result / runTimes) + "ms");
    }

    private static long method2(int[] chars) {
        long start = System.currentTimeMillis();
        int fromIndex = 0;
        int loops = chars.length / 2;
        for (int i = 0; i < loops; i++) {
            ParallelTaskTest parallelTaskTest = new ParallelTaskTest(chars, fromIndex, 2, 2);
            parallelTaskTest.doTask();
            fromIndex +=2;
        }
        long end = System.currentTimeMillis();
        System.out.println("used millis :" + (end - start));
        return end - start;
    }

    // method 1
    private static long method1(int[] chars) {
        ParallelTaskTest parallelTaskTest = new ParallelTaskTest(chars, 0, chars.length, 2);
        ForkJoinPool forkJoinPool = new ForkJoinPool(64);
        long start = System.currentTimeMillis();
        forkJoinPool.invoke(parallelTaskTest);
        long end = System.currentTimeMillis();
        System.out.println("used millis :" + (end - start));
        return end - start;
    }
}
