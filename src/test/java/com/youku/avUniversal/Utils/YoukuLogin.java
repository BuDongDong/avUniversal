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

    //public static void login(ItamiBaseCase baseCase, String accountNumber, String scretNumber) {
    //    Device DEVICE = baseCase.DEVICE;
    //    MobileDriver driver = baseCase.driver;
    //    if (DEVICE instanceof AndroidDevice) {
    //        YoukuLoginAndroid( driver, accountNumber, scretNumber );
    //    } else if (DEVICE instanceof IPhoneDevice) {
    //        YoukuLoginIPhone( driver, accountNumber, scretNumber );
    //    }
    //}

    public static void YoukuLoginAndroid(MobileDriver<WebElement> driver, String accountNumber, String scretNumber) {
        TotoroUtils.sleep( 5000 );
        logger.warn( "切换到我的Tab" );
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
                    logger.warn( "弹出了登录框了" );
                } else {
                    logger.warn( "已经是登录状态了" );
                    return;
                }
            } catch (Exception e) {
                logger.warn( "未成功弹出登录框" );
            }
        }
        TotoroUtils.sleep( 5000 );
        try {
            driver.findElementByIdWithoutExp( "com.youku.phone:id/passport_login_protocol_checkbox" ).click();
        } catch (Exception e) {
            logger.warn( "没有出现隐私权限勾选框" );
        }
        try {
            TotoroUtils.sleep( 2000 );
            WebElement wordTwo = null;
            wordTwo = driver.findElementByNameWithoutExp( "手机号登录" );
            logger.warn( "ocr识别手机号登录按钮" );
            if (null != wordTwo) {
                logger.warn( "找到了手机号登录按钮" );
                wordTwo.click();
                TotoroUtils.sleep( 1000 );
                //System.out.println( "账号/手机号登录 登录" );
                //WebElement QQLoginButton = driver.findElementByName( "手机号登录" );
                //QQLoginButton.click();
                //TotoroUtils.sleep( 1000 );
                //WebElement QQLoginButtonDouble = driver.findElementByNameWithoutExp( "手机号登录" );
                //if (null != QQLoginButtonDouble) {
                //    QQLoginButtonDouble.click();
                //}
            } else {
                try {
                    WebElement moreLoginButton = driver.findElementByName( "更多方式登录" );
                    moreLoginButton.click();
                    TotoroUtils.sleep( 2000 );
                    moreLoginButton = driver.findElementById( "com.youku.phone:id/aliuser_oauth_2_layout" );
                    if (moreLoginButton != null) {
                        moreLoginButton.click();
                        TotoroUtils.sleep( 2000 );
                    }
                } catch (Exception ee) {
                    logger.warn( "未发现'更多方式登录'按钮" );
                }
            }
        } catch (Exception e) {
            logger.error( "未找到手机号登录按钮" );
        }

        try {
            WebElement AccountLoginButton = driver.findElementByName( "账号登录" );
            if (AccountLoginButton != null) {
                AccountLoginButton.click();
            }
        } catch (Exception e) {
            logger.error( "未找到账号登录按钮" );
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

    public static void YoukuLoginIPhone(MobileDriver<WebElement> driver, String accountNumber, String scretNumber) {
        TotoroUtils.sleep( 5000 );
        WebElement UserCenterButton = driver.findElementByName( "我的" );
        UserCenterButton.click();
        TotoroUtils.sleep( 2000 );
        try {
            WebElement LoginButton = driver.findElementByName( "登录/注册" );
            LoginButton.click();
        } catch (Exception e) {
            System.out.println( "未找到登录/注册" );
            return;
        }
        TotoroUtils.sleep( 2000 );

        try {
            WebElement unChoose = driver.findElementByName( "未勾选" );
            unChoose.click();
            logger.warn( "已勾选隐私权限" );
        } catch (Exception e) {
            logger.warn( "没有出现隐私权限勾选框" );
        }

        TotoroUtils.sleep( 2000 );

        WebElement QQLoginButton = null;
        try {
            QQLoginButton = driver.findElementByName( "手机号登录" );
            QQLoginButton.click();
            logger.warn( "已点击：手机号登录" );
            TotoroUtils.sleep( 2000 );
        } catch (Exception e) {
            logger.warn( "未发现'手机号登录'按钮" );
            try {
                QQLoginButton = driver.findElementByName( "更多方式登录" );
                QQLoginButton.click();
                TotoroUtils.sleep( 2000 );
                QQLoginButton = driver.findElementByName( "短信登录" );
                if (QQLoginButton != null) {
                    QQLoginButton.click();
                    TotoroUtils.sleep( 2000 );
                }
            } catch (Exception ee) {
                logger.warn( "未发现'更多方式登录'按钮" );
                try {
                    OcrElement ocr = ImageML.itamiOcrElement( "更多方式登录" );
                    driver.click( ocr.getX(), ocr.getY() );
                    TotoroUtils.sleep( 2000 );
                    QQLoginButton = driver.findElementByName( "短信登录" );
                    if (QQLoginButton != null) {
                        QQLoginButton.click();
                        TotoroUtils.sleep( 2000 );
                    }
                } catch (Exception eee) {
                    logger.warn( "ocr也未找到'更多方式登录'" );
                    try {
                        QQLoginButton = driver.findElementByName( "账号密码登录" );
                        QQLoginButton.click();
                        TotoroUtils.sleep( 2000 );
                        logger.warn( "尝试点击'账号密码登录'" );
                    } catch ( Exception eeee) {
                        logger.warn( "没有找到'账号密码登录'" );
                    }
                }
            }
        }

        try {
            WebElement AccountLoginButton = driver.findElementByName( "账号密码登录" );
            AccountLoginButton.click();
            logger.warn( "已点击账号密码登录" );
        } catch (Exception e) {
            try {
                OcrElement ocr = ImageML.itamiOcrElement( "账号密码登录" );
                driver.click( ocr.getX(), ocr.getY() );
                TotoroUtils.sleep( 2000 );
                logger.warn( "通过ocr已点击账号密码登录" );
            } catch (Exception ee) {
                logger.warn( "未找到'账号密码登录'" );
            }
        }

        TotoroUtils.sleep( 2000 );

        try {
            WebElement clearButton = driver.findElementByName( "清除文本" );
            clearButton.click();
            logger.warn( "已点击清除文本" );
        } catch (Exception e) {
            logger.warn( "无需点击'清除文本'" );
        }

        WebElement accountField = null;
        try {
            accountField = driver.findElementByName( "loginSDK_textFiled_loginAccount" );
        } catch (Exception e) {
            try {
                accountField = driver.findElementByName( "请输入手机号码/邮箱" );
            } catch (Exception ee) {
            }
        }
        if (accountField == null) {
            logger.error( "输入账号失败" );
            return;
        }
        accountField.sendKeys( accountNumber );
        logger.warn( "输入账号" );
        TotoroUtils.sleep( 2000 );

        WebElement secretField = null;
        try {
            secretField = driver.findElementByName( "loginSDK_textFiled_loginPwd" );
        } catch (Exception e) {
            try {
                secretField = driver.findElementByName( "请输入密码" );
            } catch (Exception ee) {
            }
        }
        if (secretField == null) {
            logger.error( "输入密码失败" );
            return;
        }
        secretField.sendKeys( scretNumber );
        logger.warn( "输入密码" );
        TotoroUtils.sleep( 2000 );
        driver.click( 200, 200 );

        WebElement loginField = null;
        try {
            loginField = driver.findElementByName( "loginSDK_button_login" );
        } catch (Exception e) {
            try {
                loginField = driver.findElementByName( "登录" );
            } catch (Exception ee) {
            }
        }
        if (loginField == null) {
            logger.error( "登录失败" );
            return;
        }
        loginField.click();
        logger.warn( "点击登录" );

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
        //        List<ItamiImageFeature> it2= ImageML.singleFeatureIdentification(itamiBaseCase,
        // "YoukuPassportCloseButton");
        //        if(it2.size()!=0){
        //            ItamiImageFeature iit2=it2.get(0);
        //            driver.click(iit2.getX(),iit2.getY());
        //        }
    }
}
