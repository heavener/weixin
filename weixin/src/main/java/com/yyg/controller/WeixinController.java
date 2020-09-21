package com.yyg.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.yyg.bean.Article;
import com.yyg.bean.NewsMessage;
import com.yyg.bean.Order;
import com.yyg.bean.TextRespMessage;
import com.yyg.util.HttpUtil;
import com.yyg.util.MessageUtil;
import com.yyg.util.SignUtil;
import com.yyg.util.WeixinUtil;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.*;

@Controller
public class WeixinController {

    @RequestMapping(value = "connect",method = RequestMethod.GET)
    public void connect(HttpServletRequest request, HttpServletResponse response)  {
        //接入逻辑
        // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");

        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        if (SignUtil.checkSignature(signature, timestamp, nonce)) {
            out.print(echostr);
        }
        out.close();
        out = null;

    }

    @RequestMapping(value = "connect",method = RequestMethod.POST)
    public void postConnect(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");


        //接收消息
        Map<String, String> map = MessageUtil.parseXml(request);

        //消息类型
        String MsgType = map.get("MsgType");
        String ToUserName = map.get("ToUserName");
        String FromUserName = map.get("FromUserName");
        String CreateTime = map.get("CreateTime");

        String content ="";

        if(MsgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)){
            content ="您发送的是文本消息";
        }else  if(MsgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)){
            content ="您发送的是图片消息";
        }else  if(MsgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)){
            content ="您发送的是地理位置";
        }else  if(MsgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)){
            String event = map.get("Event");
            if(event.equals("subscribe")){
                //订阅事件
                NewsMessage newsMessage = new NewsMessage();
                newsMessage.setToUserName(FromUserName);
                newsMessage.setFromUserName(ToUserName);
                newsMessage.setCreateTime(new Date().getTime());
                newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);

                List items = new ArrayList();

                Article article1 = new Article();
                article1.setTitle("用户绑定");
                article1.setDescription("用户绑定");
                article1.setPicUrl(WeixinUtil.www_url+"imgs/userbind.png");
                article1.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+WeixinUtil.appID+"&redirect_uri="+ URLEncoder.encode(WeixinUtil.www_url+"userbindPage","UTF-8")+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");

                Article article2 = new Article();
                article2.setTitle("消息推送");
                article2.setDescription("消息推送");
                article2.setPicUrl(WeixinUtil.www_url+"imgs/userbind.png");
                article2.setUrl("http://www.baidu.com");

                items.add(article1);
                items.add(article2);
                newsMessage.setArticleCount(items.size());
                newsMessage.setArticles(items);
                // 响应消息
                PrintWriter out = response.getWriter();
                out.print(MessageUtil.newsMessageToXml(newsMessage));
            }

        }else{
            content="未知消息";
        }


        //响应消息
        TextRespMessage textRespMessage =new TextRespMessage();
        textRespMessage.setToUserName(FromUserName);
        textRespMessage.setFromUserName(ToUserName);
        textRespMessage.setCreateTime(new Date().getTime());
        textRespMessage.setContent(content);
        textRespMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
        // 响应消息
        PrintWriter out = response.getWriter();
        out.print(MessageUtil.textMessageToXml(textRespMessage));



    }


    @RequestMapping("userbindPage")
    public String userbindPage(String code,HttpServletRequest request)  {

        System.out.println(code);

        JSONObject jsonObject = WeixinUtil.httpRequest("https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + WeixinUtil.appID + "&secret=" + WeixinUtil.appsecret + "&code=" + code + "&grant_type=authorization_code", "GET", null);


        String token =jsonObject.getString("access_token");
        String openid =jsonObject.getString("openid");


        jsonObject = WeixinUtil.httpRequest("https://api.weixin.qq.com/sns/userinfo?access_token="+token+"&openid="+openid+"&lang=zh_CN", "GET", null);

        System.out.println(jsonObject.toString());

        String nickname =jsonObject.getString("nickname");
        String headimgurl =jsonObject.getString("headimgurl");

        request.setAttribute("openid",openid);
        request.setAttribute("nickname",nickname);
        request.setAttribute("headimgurl",headimgurl);




        return "userbind";
    }


    @RequestMapping("orderinfo")
    public String orderinfo(Order order, HttpServletRequest request)  {

        request.setAttribute("order",order);
        return "orderinfo";
    }


    @RequestMapping("getUser")
    public String getUser(String openid,String username,String password,HttpServletRequest request)  {
    Map map = new HashMap();
    map.put("openid",openid);
    map.put("username",username);
    map.put("password",password);
    //将数据远程发送到电商项目
        String msg ="";
        synchronized (msg){
             msg =HttpUtil.post("http://yyg1.ngrok2.xiaomiqiu.cn/api/user-service/user/binduser",map);
        }

        System.out.println(msg);
        request.setAttribute("msg",msg);

        return "result";
    }





    @RequestMapping("scancode")
    public String scancode(String code,String uuid,HttpServletRequest request)  {

        System.out.println(code);

        JSONObject jsonObject = WeixinUtil.httpRequest("https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + WeixinUtil.appID + "&secret=" + WeixinUtil.appsecret + "&code=" + code + "&grant_type=authorization_code", "GET", null);


        String token =jsonObject.getString("access_token");
        String openid =jsonObject.getString("openid");


        jsonObject = WeixinUtil.httpRequest("https://api.weixin.qq.com/sns/userinfo?access_token="+token+"&openid="+openid+"&lang=zh_CN", "GET", null);

        System.out.println(jsonObject.toString());

        String headimgurl =jsonObject.getString("headimgurl");

        request.setAttribute("openid",openid);
        request.setAttribute("uuid",uuid);
        request.setAttribute("headimgurl",headimgurl);

        return "confirmlogin";
    }


    @RequestMapping("scanlogin")
    public String scanlogin(String openid,String uuid,HttpServletRequest request)  {
        System.out.println();
        //传递数据给电商项目
        Map map = new HashMap();
        map.put("openid",openid);
        map.put("uuid",uuid);
        //将数据远程发送到电商项目
        Integer result = 0;
        synchronized (result){
//            msg =HttpUtil.post("http://yyg1.ngrok2.xiaomiqiu.cn/api/user-service/user/scanlogin",map);
            result =Integer.parseInt(HttpUtil.post("http://localhost:10000/api/user-service/user/scanlogin",map));
        }

       if(result >0){

           request.setAttribute("msg","登录成功");
       }else{

           request.setAttribute("msg","登录失败");
       }

        return "loginresult";
    }



}
