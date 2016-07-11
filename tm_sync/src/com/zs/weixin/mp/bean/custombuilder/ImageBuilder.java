package com.zs.weixin.mp.bean.custombuilder;

import com.zs.weixin.common.api.WxConsts;
import com.zs.weixin.mp.bean.WxMpCustomMessage;

/**
 * 获得消息builder
 * <pre>
 * 用法: WxMpCustomMessage m = WxMpCustomMessage.IMAGE().mediaId(...).toUser(...).build();
 * </pre>
 * @author chanjarster
 *
 */
public final class ImageBuilder extends BaseBuilder<ImageBuilder> {
  private String mediaId;

  public ImageBuilder() {
    this.msgType = WxConsts.CUSTOM_MSG_IMAGE;
  }

  public ImageBuilder mediaId(String media_id) {
    this.mediaId = media_id;
    return this;
  }

  public WxMpCustomMessage build() {
    WxMpCustomMessage m = super.build();
    m.setMediaId(this.mediaId);
    return m;
  }
}
