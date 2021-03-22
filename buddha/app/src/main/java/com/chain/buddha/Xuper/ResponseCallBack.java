package com.chain.buddha.Xuper;

import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @Author: haroro
 * @CreateDate: 2021/1/29
 */
public interface ResponseCallBack<T> {
    void onSuccess(T t);
    void onFail(String message);

}
