package com.starrypay.utils;

import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GlobalTaskExecutor {

    private static final GlobalTaskExecutor executor = new GlobalTaskExecutor();

    private final Executor IO;

    private final Executor MAIN;

    private GlobalTaskExecutor() {
        EventHandler mainHandler = new EventHandler(EventRunner.getMainEventRunner());
        MAIN = mainHandler::postTask;
        IO = Executors.newCachedThreadPool();
    }

    public static GlobalTaskExecutor getInstance() {
        return executor;
    }

    public Executor IO() {
        return IO;
    }

    public Executor MAIN() {
        return MAIN;
    }

    public void MAIN(Runnable runnable){
        MAIN.execute(runnable);
    }

    public void IO(Runnable runnable){
        IO().execute(runnable);
    }

}
