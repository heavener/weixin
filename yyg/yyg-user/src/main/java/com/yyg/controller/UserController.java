package com.yyg.controller;

import com.yyg.bean.Address;
import com.yyg.bean.User;
import com.yyg.config.RedisUtil;
import com.yyg.service.UserService;
import com.yyg.util.HttpUtil;
import com.yyg.util.QRCodeUtil;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import weibo4j.Users;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@Controller
@RequestMapping("/user")
public class UserController {

    @Value("${server.port}")
    private int port;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;


    @RequestMapping("saveAddress")
    @ResponseBody
    public boolean saveAddress(Address address, HttpServletRequest request) {
        //获取用户信息
        User user = (User) request.getSession().getAttribute("user");
        address.setUserid(user.getId());
        return userService.saveAddress(address);
    }

    @RequestMapping("getAddress")
    @ResponseBody
    public List<Address> getAddress(HttpServletRequest request) {
        //获取用户信息
        User user = (User) request.getSession().getAttribute("user");
        return userService.getAddress(user);
    }


    @GetMapping("{id}")
    @ResponseBody
    public User getUser(@PathVariable("id") Long id) {
        return userService.getUser(id);
    }

    @GetMapping("redis")
    @ResponseBody
    public Object redis() {
        redisUtil.set("redis", "redis111111");

        return redisUtil.get("redis");
    }

    @GetMapping("session")
    @ResponseBody
    public Object session(String name, HttpServletRequest request) {
        request.getSession().setAttribute("name", name);
        return "端口号：" + port + " sessionId：" + request.getSession().getId();
    }


    @RequestMapping("login")
    @ResponseBody
    public Map<String, String> login(User user, HttpServletRequest request) {
        Map map = new HashMap();

        User dataUser = userService.login(user);

        if (dataUser != null) {
            //登录成功
            map.put("status", "200");
            request.getSession().setAttribute("user", dataUser);
        } else {
            map.put("status", "400");
        }
        return map;
    }

    @RequestMapping("register")
    @ResponseBody
    public Map<String, String> register(User user) {
        Map map = new HashMap();

        //密码加密
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));
        user.setCreated(new Date());

        //保存用户
        boolean flag = userService.save(user);
        //返回数据
        if (flag) {
            map.put("status", "200");
        } else {
            map.put("status", "400");
        }
        return map;
    }

    //判断用户是否登录
    @RequestMapping("verify")
    @ResponseBody
    public User verify(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            return user;
        } else {
            return null;
        }
    }


    //第三方登录
    @RequestMapping("threeLogin")
    public String threeLogin(String code, HttpServletRequest request) {

            //接收code
            System.out.println(code);
            //换取token
            Map map = new HashMap();
            map.put("client_id", "2375657433");
            map.put("client_secret", "218d93af8c86df18b49af9c57369b5eb");
            map.put("grant_type", "authorization_code");
            map.put("code", code);
            map.put("redirect_uri", "http://www.yunyougou.com/api/user-service/user/threeLogin");
            String tokenstr = HttpUtil.post("https://api.weibo.com/oauth2/access_token", map);
            String access_token = JSONObject.fromObject(tokenstr).getString("access_token");
            String uid = JSONObject.fromObject(tokenstr).getString("uid");

        weibo4j.model.User user =null;
        try {
            //获取用户信息
            Users um = new Users(access_token);
            user = um.showUserById(uid);
            System.out.println(user.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
            //根据uid查询数据库是否有对应记录
        User dataUser  = userService.getUserByUid(uid);

            if (dataUser == null) {

                //保存微博用户信息
                request.getSession().setAttribute("uid", user.getId());
                request.getSession().setAttribute("headimg", user.getProfileImageUrl());

                //跳转到完善用户信息页面
                return "redirect:http://www.yunyougou.com/userinfo.html";

            } else {
                //保存用户信息到session
                request.getSession().setAttribute("user", dataUser);
                //跳转首页
                return "redirect:http://www.yunyougou.com";
            }

}


    //完善用户信息
    @RequestMapping("userinfo")
    @ResponseBody
    public Map userinfo(User user,HttpServletRequest request) {
        Map map = new HashMap();
        //密码加密
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));
        user.setCreated(new Date());
        user.setuId(request.getSession().getAttribute("uid").toString());
        user.setHeadimg(request.getSession().getAttribute("headimg").toString());

        //保存用户
        boolean flag = userService.save(user);

        request.getSession().setAttribute("user",user);

        request.getSession().removeAttribute("uid");
        request.getSession().removeAttribute("headimg");

        //返回数据
        if (flag) {
            map.put("status", "200");
        } else {
            map.put("status", "400");
        }
        return map;

    }


    //绑定用户
    @PostMapping("binduser")
    @ResponseBody
    public int binduser(String openid,String username,String password) {
        System.out.println(openid);
        User user = new User();
        user.setOpenId(openid);
        user.setUsername(username);
        user.setPassword(DigestUtils.md5Hex(password));
        //绑定用户
        return userService.binduser(user);



    }


    //生成二维码图片
    @RequestMapping("showcode")
    @ResponseBody
    public String showcode(HttpServletRequest request) throws Exception {

        String uuid = UUID.randomUUID().toString();

        //拼凑网页授权url
        String url ="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+ QRCodeUtil.appID+"&redirect_uri="+ URLEncoder.encode(QRCodeUtil.www_url+"scancode?uuid="+uuid,"UTF-8")+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";

        //中间logo
        String logoPath = "E:\\workspaces\\1910\\yyg\\yyg-zuul\\src\\main\\resources\\static\\imgs\\head.png";
        //生成图片路径
        String codeImgPath ="E:\\workspaces\\1910\\yyg\\yyg-zuul\\target\\classes\\static\\imgs\\"+uuid+".jpg";


        QRCodeUtil.encode(url,logoPath,codeImgPath,true);
        return  uuid;
    }


    //扫码登录
    @RequestMapping("scanlogin")
    @ResponseBody
    public int scanlogin(String openid,String uuid,HttpServletRequest request) throws Exception {

        return userService.scanlogin(openid,uuid);

    }

    //根据uuid查询数据库用户

    @RequestMapping("uuidlogin")
    @ResponseBody
    public int uuidlogin(String uuid,HttpServletRequest request) throws Exception {

        User user = userService.uuidlogin(uuid);

        if(user!=null){
            request.getSession().setAttribute("user",user);
            return 1;
        }else{
            return 0;
        }

    }



}
