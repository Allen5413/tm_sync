package com.zs.weixin.mp.bean.outxmlbuilder;

import com.zs.weixin.mp.bean.WxMpXmlOutNewsMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 图文消息builder
 * @author chanjarster
 */
public final class NewsBuilder extends BaseBuilder<NewsBuilder, WxMpXmlOutNewsMessage> {

  protected final List<WxMpXmlOutNewsMessage.Item> articles = new ArrayList<WxMpXmlOutNewsMessage.Item>();
  
  public NewsBuilder addArticle(WxMpXmlOutNewsMessage.Item item) {
    this.articles.add(item);
    return this;
  }
  
  public WxMpXmlOutNewsMessage build() {
    WxMpXmlOutNewsMessage m = new WxMpXmlOutNewsMessage();
    for(WxMpXmlOutNewsMessage.Item item : articles) {
      m.addArticle(item);
    }
    setCommon(m);
    return m;
  }
  
}
