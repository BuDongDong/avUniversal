package com.youku.avUniversal.Utils;

import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author 庭婳（meifang.ymf@alibaba-inc.com）
 * @date 2021/9/28 1:58 PM
 */
public class HttpUtils {

    public static String get(String url) throws IOException {
        String result = Request.Get( url ).execute().returnContent().asString();
        return result;
    }

    public static String get(String url, int socketTimeout) throws IOException {
        String result = Request.Get( url ).socketTimeout( socketTimeout ).execute().returnContent().asString();
        return result;
    }

}
