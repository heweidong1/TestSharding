package com.luban.sharding.utils;

public class DbUtil {
    public static String master="master";
    public static String slave="slave";

    //将一个值 保存到本地线程 ，同一个线程共享数据，在多个线程下 这个变量是隔离开的
    private static ThreadLocal<String> threadLocal=new ThreadLocal();


    public static void setDb(String db){
        threadLocal.set(db);
    }

    public static String getDb(){
        return threadLocal.get();
    }

}
