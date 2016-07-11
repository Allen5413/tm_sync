package com.zs.weixin.common.api;

import com.zs.weixin.common.exception.WxErrorException;

/**
 * WxErrorException处理器
 */
public interface WxErrorExceptionHandler {

  public void handle(WxErrorException e);

}
