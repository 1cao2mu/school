package com.hhstu.cyy.school.tool;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/14.
 */
public class CustomRequestString extends Request<String> {
    public static final int POST = Method.POST;
    public static final int GET = Method.GET;
    private Response.Listener<String> listener;
    private Map<String, String> params;

    public CustomRequestString(String url, Map<String, String> params, Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = responseListener;
        this.params = params;

    }

    @Override
    public Priority getPriority() {
        return Priority.HIGH;
    }

    public CustomRequestString(int method, String url, Map<String, String> params, Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = responseListener;
        this.params = params;

    }

    protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
        Utils.sysout("参数: " + params.toString());
        return params;
    }


    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String jsonString = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));

            return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }

    }

    @Override
    protected void deliverResponse(String jsonObject) {
        System.out.println("result: " + jsonObject.toString());
        listener.onResponse(jsonObject);

    }
}
