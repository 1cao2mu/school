package com.hhstu.cyy.school.tool;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/14.
 */
public class CustomRequest extends Request<JSONObject> {
    public static final int POST = Method.POST;
    public static final int GET = Method.GET;
    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;

    public CustomRequest(String url, Map<String, String> params, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = responseListener;
        this.params = params;

    }

    @Override
    public Priority getPriority() {
        return Priority.HIGH;
    }

    public CustomRequest(int method, String url, Map<String, String> params, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = responseListener;
        this.params = params;

    }

    @Override
    protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
        if (params != null) {
            Utils.sysout("参数: " + params.toString());
        }
        return params;
    }


    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String jsonString = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            if (jsonString.startsWith("[")) {
                jsonString = jsonString.substring(1, jsonString.length() - 1);
            }
            return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }

    }

    @Override
    protected void deliverResponse(JSONObject jsonObject) {
        System.out.println("result: " + jsonObject.toString());
        listener.onResponse(jsonObject);
    }
}
