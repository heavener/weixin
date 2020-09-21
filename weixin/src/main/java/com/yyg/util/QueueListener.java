package com.yyg.util;

import com.yyg.bean.*;
import net.sf.json.JSONObject;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class QueueListener {


    @JmsListener(destination = "publish.queue", containerFactory = "jmsListenerContainerQueue")
    @SendTo("out.queue")
    public String receive(OrderInfo orderinfo){

        System.out.println("用户订单交易号："+orderinfo.getPayid());
        System.out.println("用户微信号："+orderinfo.getOpenid());


        MsgTemplate msgTemplate = new MsgTemplate();
        msgTemplate.setTouser(orderinfo.getOpenid());
        msgTemplate.setTemplate_id("riJH0EcRYMlzGlmj2WcV9mwmJXoFvzHdfL_gJffq2M4");
        msgTemplate.setUrl(WeixinUtil.www_url+"/orderinfo?id="+orderinfo.getId()+"&totalprice="+orderinfo.getTotalprice());

        Map data = new HashMap();
        ValTemplate valTemplate1 = new ValTemplate();
        valTemplate1.setColor("#DC143C");
        valTemplate1.setValue(orderinfo.getTotalprice().toString());
        data.put("totalprice",valTemplate1);
        ValTemplate valTemplate2 = new ValTemplate();
        valTemplate2.setColor("#DC143C");
        valTemplate2.setValue(DateFormatUtils.format(orderinfo.getPaytime(),"yyyy-MM-dd HH:mm:ss"));
        data.put("paytime",valTemplate2);
        msgTemplate.setData(data);
        String json =JSONObject.fromObject(msgTemplate).toString();

        AccessToken accessToken = WeixinUtil.getAccessToken(WeixinUtil.appID, WeixinUtil.appsecret);
        WeixinUtil.httpRequest("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+accessToken.getAccess_token(),"POST",json);

        return "success";
    }
}
