package com.zs.weixin.common.util;

import com.zs.weixin.common.api.WxErrorExceptionHandler;
import com.zs.weixin.common.exception.WxErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LogExceptionHandler implements WxErrorExceptionHandler {

  private Logger log = LoggerFactory.getLogger(WxErrorExceptionHandler.class);

  @Override
  public void handle(WxErrorException e) {

    log.error("Error happens", e);

  }

}
