package com.zs.weixin.mp.util.http;

import com.zs.weixin.common.bean.result.WxError;
import com.zs.weixin.common.exception.WxErrorException;
import com.zs.weixin.common.util.http.RequestExecutor;
import com.zs.weixin.common.util.http.Utf8ResponseHandler;
import com.zs.weixin.common.util.json.WxGsonBuilder;
import com.zs.weixin.mp.bean.result.WxMpMaterialVideoInfoResult;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MaterialVideoInfoRequestExecutor implements RequestExecutor<WxMpMaterialVideoInfoResult, String> {

  public MaterialVideoInfoRequestExecutor() {
    super();
  }

  public WxMpMaterialVideoInfoResult execute(CloseableHttpClient httpclient, HttpHost httpProxy, String uri, String materialId) throws WxErrorException, ClientProtocolException, IOException {
    HttpPost httpPost = new HttpPost(uri);
    if (httpProxy != null) {
      RequestConfig config = RequestConfig.custom().setProxy(httpProxy).build();
      httpPost.setConfig(config);
    }

    Map<String, String> params = new HashMap<String, String>();
    params.put("media_id", materialId);
    httpPost.setEntity(new StringEntity(WxGsonBuilder.create().toJson(params)));
    CloseableHttpResponse response = httpclient.execute(httpPost);
    String responseContent = Utf8ResponseHandler.INSTANCE.handleResponse(response);
    WxError error = WxError.fromJson(responseContent);
    if (error.getErrorCode() != 0) {
      throw new WxErrorException(error);
    } else {
      return WxMpMaterialVideoInfoResult.fromJson(responseContent);
    }
  }

}
