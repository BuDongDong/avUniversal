package com.youku.avUniversal.Utils;

import com.totoro.client.deeplearning.adstract.OcrElement;
import com.totoro.client.internal.MobileDriver;
import com.totoro.client.utils.TotoroUtils;
import com.youku.itami.config.AndroidDevice;
import com.youku.itami.config.Device;
import com.youku.itami.config.IPhoneDevice;
import com.youku.itami.core.ItamiBaseCase;
import com.youku.itami.utility.ImgHandler.ImageML.ImageML;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 庭婳（meifang.ymf@alibaba-inc.com）
 * @date 2021/10/11 10:47 AM
 */

public class YoukuLogin {

    private static Logger logger = LoggerFactory.getLogger( YoukuLogin.class );


    public static void login(ItamiBaseCase baseCase, String accountNumber, String scretNumber) {
        Device DEVICE = baseCase.DEVICE;
        MobileDriver driver = baseCase.driver;
        if (DEVICE instanceof AndroidDevice) {
            YoukuLoginAndroid( driver, accountNumber, scretNumber );
        } else if (DEVICE instanceof IPhoneDevice) {
            YoukuLoginIPhone( driver, accountNumber, scretNumber );
        }
    }

    private static void YoukuLoginAndroid(MobileDriver<WebElement> driver, String accountNumber, String scretNumber) {
        TotoroUtils.sleep( 5000 );
        System.out.println( "切换到我的Tab" );
        try {
            WebElement UserCenterButton = driver.findElementByName( "我的" );
            UserCenterButton.click();
        } catch (Exception e) {
            System.out.println( "切换到我的Tab失败，使用ocr方法进行点击切换" );
            OcrElement ocr = ImageML.itamiOcrElement( "我的" );
            driver.click( ocr.getX(), ocr.getY() );
        }
        TotoroUtils.sleep( 2000 );
        logger.warn( "开始登录" );
        System.out.println( "开始登录" );
        WebElement loginWindow = driver.findElementByNameWithoutExp( "登录优酷" );
        if (null == loginWindow) {
            try {
                OcrElement word = ImageML.itamiOcrElement( "登录/注册" );
                if (null != word) {
                    word.click( driver );
                } else {
                    logger.warn( "已经是登录状态了" );
                    System.out.println( "已经是登录状态了" );
                    return;
                }
            } catch (Exception e) {
                logger.warn( "弹出了登录框了" );
                System.out.println( "弹出了登录框了" );
            }
        }
        try {
            TotoroUtils.sleep( 5000 );
            WebElement wordTwo = null;
            wordTwo = driver.findElementByNameWithoutExp( "手机号登录" );
            logger.warn( "ocr识别手机号登录按钮" );
            if (null != wordTwo) {
                logger.warn( "找到了手机号登录按钮" );
                System.out.println( "账号/手机号登录 登录" );
                WebElement QQLoginButton = driver.findElementByName( "手机号登录" );
                QQLoginButton.click();
                TotoroUtils.sleep( 1000 );
                WebElement QQLoginButtonDouble = driver.findElementByNameWithoutExp( "手机号登录" );
                if (null != QQLoginButtonDouble) {
                    QQLoginButtonDouble.click();
                }
            } else {
                logger.warn( "未到了手机号登录按钮" );
                WebElement LoginButton = driver.findElementByName( "登录/注册" );
                LoginButton.click();
                TotoroUtils.sleep( 1000 );
                System.out.println( "账号/手机号登录 登录" );
                WebElement QQLoginButton = driver.findElementByName( "手机号登录" );
                QQLoginButton.click();
                TotoroUtils.sleep( 1000 );
            }
        } catch (Exception e) {
            System.out.println( "弹出了登录框了" );
        }
        TotoroUtils.sleep( 2000 );
        try {
            driver.findElementByIdWithoutExp( "com.youku.phone:id/passport_login_protocol_checkbox" ).click();
        } catch (Exception e) {
            logger.warn( "没有出现隐私权限勾选框" );
        }
        try {
            WebElement AccountLoginButton = driver.findElementByName( "手机号登录" );
            if (AccountLoginButton.isDisplayed()) {
                AccountLoginButton.click();
            }
            AccountLoginButton = driver.findElementByName( "账号登录" );
            if (AccountLoginButton.isDisplayed()) {
                AccountLoginButton.click();
            }
        } catch (Exception e) {
            System.out.println( "未找到按钮" );
        }
        TotoroUtils.sleep( 2000 );
        try {
            WebElement text = driver.findElementByName( "请输入手机号码/邮箱" );
            //text.sendKeys("346405176@qq.com");
            text.sendKeys( accountNumber );
        } catch (Exception e) {
            System.out.println( "请输入手机号码/邮箱" );
        }
        TotoroUtils.sleep( 1000 );
        try {
            WebElement ScretNumber = driver.findElementByName( "请输入密码" );
            //ScretNumber.sendKeys("yxf123456");
            ScretNumber.sendKeys( scretNumber );
        } catch (Exception e) {
            System.out.println( "未找到密码输入框" );
        }
        try {
            WebElement ScretNumber = driver.findElementById(
                ItamiBaseCase.DEVICE.getPackageName() + ":id/aliuser_login_password_et" );
            ScretNumber.sendKeys( scretNumber );
        } catch (Exception e) {
            System.out.println( "未找到低端机器密码输入框" );
        }
        TotoroUtils.sleep( 5000 );
        driver.findElementByNameWithoutExp( "登录" ).click();
        System.out.println( "登录" );
        TotoroUtils.sleep( 3000 );
    }

    private static void YoukuLoginIPhone(MobileDriver<WebElement> driver, String accountNumber, String scretNumber) {
        TotoroUtils.sleep( 5000 );
        WebElement UserCenterButton = driver.findElementByName( "我的" );
        UserCenterButton.click();
        TotoroUtils.sleep( 2000 );
        try {
            WebElement LoginButton = driver.findElementByName( "登录/注册" );
            LoginButton.click();
        } catch (Exception e) {
            System.out.println( "未找到登录/注册" );
        }
        TotoroUtils.sleep( 2000 );
        //WebElement QQLoginButton = driver.findElementByName("账号/手机号登录");
        WebElement QQLoginButton = driver.findElementByName( "手机号登录" );
        QQLoginButton.click();
        TotoroUtils.sleep( 2000 );
        WebElement AccountLoginButton = driver.findElementByName( "账户密码登录" );
        AccountLoginButton.click();
        TotoroUtils.sleep( 2000 );
        WebElement QQtext = driver.findElementByName( "请输入手机号码/邮箱" );
        QQtext.sendKeys( accountNumber );
        TotoroUtils.sleep( 2000 );
        WebElement ScretNumber = driver.findElementByName( "请输入密码" );
        ScretNumber.sendKeys( scretNumber );
        TotoroUtils.sleep( 2000 );
        driver.findElementByName( "登录" ).click();
        TotoroUtils.sleep( 5000 );
        try {
            driver.findElementByName( "现在不" ).click();
        } catch (Exception e) {
            System.out.println( "未找到对应按钮" );
        }
        try {
            driver.findElementByName( "暂不需要绑定" ).click();
        } catch (Exception e) {

            System.out.println( "未找到对应按钮" );
        }
        //        TotoroUtils.sleep(2000);
        //        List<ItamiImageFeature> it2= ImageML.singleFeatureIdentification(itamiBaseCase,"YoukuPassportCloseButton");
        //        if(it2.size()!=0){
        //            ItamiImageFeature iit2=it2.get(0);
        //            driver.click(iit2.getX(),iit2.getY());
        //        }
    }
}
