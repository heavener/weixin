package com.yyg.util;

import com.yyg.WeixinApplication;
import com.yyg.bean.*;
import org.springframework.boot.SpringApplication;

public class MenuUtil {
    public static void main(String[] args) {


        String appid = "wx115337ef1121ae26";
        String appsecret = "162eb8a8922452580e65cd437587ebeb";


        AccessToken accessToken = WeixinUtil.getAccessToken(appid, appsecret);

        ViewButton btn11 = new ViewButton();
        btn11.setName("1菜单1");
        btn11.setType("view");
        btn11.setUrl("http://www.baidu.com");

        CommonButton btn12 = new CommonButton();
        btn12.setName("1菜单2");
        btn12.setType("click");
        btn12.setKey("12");

        CommonButton btn13 = new CommonButton();
        btn13.setName("1菜单3");
        btn13.setType("click");
        btn13.setKey("13");

        ComplexButton btn1 = new ComplexButton();
        btn1.setName("菜单1");
        btn1.setSub_button(new Button[]{btn11,btn12,btn13});

        CommonButton btn21 = new CommonButton();
        btn21.setName("扫码登录");
        btn21.setType("scancode_push");
        btn21.setKey("21");

        CommonButton btn22 = new CommonButton();
        btn22.setName("2菜单2");
        btn22.setType("click");
        btn22.setKey("22");

        CommonButton btn23 = new CommonButton();
        btn23.setName("2菜单3");
        btn23.setType("click");
        btn23.setKey("23");

        ComplexButton btn2 = new ComplexButton();
        btn2.setName("菜单2");
        btn2.setSub_button(new Button[]{btn21,btn22,btn23});

        CommonButton btn31 = new CommonButton();
        btn31.setName("3菜单1");
        btn31.setType("click");
        btn31.setKey("31");

        CommonButton btn32 = new CommonButton();
        btn32.setName("3菜单2");
        btn32.setType("click");
        btn32.setKey("32");

        CommonButton btn33 = new CommonButton();
        btn33.setName("3菜单3");
        btn33.setType("click");
        btn33.setKey("33");

        ComplexButton btn3 = new ComplexButton();
        btn3.setName("菜单3");
        btn3.setSub_button(new Button[]{btn31,btn32,btn33});







        Menu menu = new Menu();
        menu.setButton(new Button[]{btn1,btn2,btn3});




        WeixinUtil.createMenu(menu,accessToken.getAccess_token());






    }

}