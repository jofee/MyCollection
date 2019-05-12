package com.fk.mycollection.bean;


public class BaseBean<T,V> {

    private int result;
    private String error;
    private T data;
    private int total;
    private V extend;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public V getExtend() {
        return extend;
    }

    public void setExtend(V extend) {
        this.extend = extend;
    }
}
