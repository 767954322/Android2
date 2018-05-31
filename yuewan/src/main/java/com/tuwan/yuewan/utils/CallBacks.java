package com.tuwan.yuewan.utils;

/**
 * Created by apple on 2017/7/31.
 */
//泛型一定是表示一个类对象或者一个类。class
public interface CallBacks<T>{
    void suc(T t);
    void fail(String str);

}
