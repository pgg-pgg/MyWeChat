package com.pgg.mywechatem.Protocol.Net;

/**
 * Created by PDD on 2017/11/30.
 */

public class ThreadHelper {



    private ThreadDoSomething threadDoSomething;
    public interface ThreadDoSomething{
        void doPre();
        void doing();
        void doEnd();
    }
    public ThreadHelper(ThreadDoSomething threadDoSomething){
        this.threadDoSomething=threadDoSomething;
    }

    public void newThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (threadDoSomething!=null){
                    threadDoSomething.doPre();
                    threadDoSomething.doing();
                    threadDoSomething.doEnd();
                }

            }
        }).start();
    }
}
