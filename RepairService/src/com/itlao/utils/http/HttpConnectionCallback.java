package com.itlao.utils.http;

public interface HttpConnectionCallback<T> {
/**
 * Call back method will be execute after the http request return.
 * @param response the response of http request.
 * The value will be null if any error occur.
 */
public T execute(String response);

}
