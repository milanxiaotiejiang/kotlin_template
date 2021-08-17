package com.seabreeze.robot.base.router;

/**
 * User: milan
 * Time: 2020/4/8 16:39
 * Des:
 */
public interface RouterPath {

    interface AppCenter {
        String PATH_APP_MAIN = "/appCenter/main";
        String PATH_APP_MESSAGE = "/appCenter/message";
    }

    //用户模块
    interface UserCenter {
        String PATH_APP_LOGIN = "/userCenter/login";
        String PATH_APP_REGISTER = "/userCenter/register";
        String PATH_APP_SHARE = "/userCenter/share";
        String PATH_APP_PAY = "/userCenter/pay";
    }

    //设置模块
    interface SettingCenter {
    }

}
