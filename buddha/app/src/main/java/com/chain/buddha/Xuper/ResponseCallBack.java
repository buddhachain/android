package com.chain.buddha.Xuper;

/**
 * @Author: haroro
 * @CreateDate: 2021/1/29
 */
public interface ResponseCallBack<T> {
    void onSuccess(T t);
    void onFail(String message);

}
