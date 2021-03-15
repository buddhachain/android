package com.chain.buddha.bean;

/**
 * @Author: haroro
 * @CreateDate: 3/15/21
 */
public class XuperResponse<T> extends BaseBean {

    /**
     * { "result":true, "message":""}
     */
    private boolean result;
    private T message;
    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public T getMessage() {
        return message;
    }

    public void setMessage(T message) {
        this.message = message;
    }
}
