package com.youku.avUniversal;

/**
 * @author 庭婳（meifang.ymf@alibaba-inc.com）
 * @date 2021/9/18 1:41 PM
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.totoro.client.deeplearning.adstract.IDLRect;
import com.totoro.client.deeplearning.adstract.OcrElement;
import com.totoro.client.internal.MobileDriver;
import com.totoro.client.utils.ADBCommandUtils;
import com.totoro.client.utils.TotoroUtils;
import com.youku.avUniversal.Utils.Constant;
import com.youku.avUniversal.Utils.YoukuLogin;
import com.youku.itami.config.AndroidDevice;
import com.youku.itami.config.IPhoneDevice;
import com.youku.itami.core.ItamiBaseCase;
import com.youku.itami.core.Permission;
import com.youku.itami.core.Router;
import com.youku.itami.utility.ImgHandler.ImageML.ImageML;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PlayerBaseCase extends ItamiBaseCase {

    private static Logger logger = LoggerFactory.getLogger( PlayerBaseCase.class );

    public String exeId = "null";
    public String testApp = "优酷";

    public int duration = 30;

    // ott测试视频
    //public String showName = "鬮乆彡瓩 14";
    //public String videoName = "优酷视频质量测试";
    //public String resolution = "1080P";
    //public String vid = "XNTEyNTQxMTYwNA==";

    // hls5hd3_hbr_bit10
    public String showName = "无";
    public String videoName = "新疆琼库什台";
    public String resolution = "帧享影音";
    public String vid = "XNTgzNDUyMjg5Mg==";
    public int startPoint = 1000;

    // hls5qd3_hfr_hbr
    //public String showName = "无";
    //public String videoName = "新疆大沙漠";
    //public String resolution = "帧享影音";
    //public String vid = "XNTgzNDUyNjMwNA==";

    private String more_dot_url
        = "https://av-universal.oss-cn-beijing.aliyuncs.com/res_pic/more_dot"
        + ".jpg?OSSAccessKeyId=LTAIDHmh6a8P8brD&Expires=1949563354&Signature=s2KjfIazxi8zSCR2KJnd%2BmujbDY%3D";
    private String choose_episode_url
        = "https://av-universal.oss-cn-beijing.aliyuncs.com/res_pic/choose_episode"
        + ".jpg?OSSAccessKeyId=LTAIDHmh6a8P8brD&Expires=1949563266&Signature=lUlKwCJWIDzomby91fVk5ETOYOw%3D";
    private String choose_episode_playing_url
        = "https://av-universal.oss-cn-beijing.aliyuncs.com/res_pic/choose_episode_playing"
        + ".jpg?OSSAccessKeyId=LTAIDHmh6a8P8brD&Expires=1949563381&Signature=y68lIqlQATTa2tTH7uSTt%2FlbZuQ%3D";

    //static {
    //    DEVICE = new IPhoneDevice( null, null );
    //    logger.warn( "尝试查找iphone设备: " + DEVICE.getDeviceId() );
    //    if (DEVICE.getDeviceId() == null) {
    //        DEVICE = new AndroidDevice( null, null, null );
    //        logger.warn( "尝试查找android设备: " + DEVICE.getDeviceId() );
    //    }
    //    Permission.permissionYouku( DEVICE.getDeviceId(), DEVICE.getPackageName() );
    //}

    @Before
    public void before() {
        if (DEVICE.getPlatform().equalsIgnoreCase( "ios" )) {
            TotoroUtils.sleep( 2000 );
            //driver.back();
            //TotoroUtils.sleep(2000);
        }

        super.before();

        //TotoroUtils.sleep(1000 * 60);

        logger.warn( "测试准备: 测试参数初始化" );
        if (DEVICE.getPackageName().contains( "qiyi" )) {
            testApp = "爱奇艺";
        } else if (DEVICE.getPackageName().contains( "tencent" )) {
            testApp = "腾讯";
        } else {
            testApp = "优酷";
        }
        logger.warn( "测试准备: 当前测试应用为" + testApp );

        String otherArgStr = System.getenv( "extraArgs" );
        System.out.println( "otherArgStr: " + otherArgStr );
        if (otherArgStr != null && !otherArgStr.isEmpty()) {
            try {
                JSONObject otherArgs = JSONObject.parseObject( otherArgStr );
                exeId = otherArgs.getString( "exeId" );
                JSONArray paramArray = JSONArray.parseArray( otherArgs.getString( "params" ) );
                for (Object each : paramArray) {
                    if ("showName".equals( ((JSONObject)each).getString( "name" ) )) {
                        showName = ((JSONObject)each).getString( "value" );
                        logger.warn( "设置showName: " + showName );
                    } else if ("videoName".equals( ((JSONObject)each).getString( "name" ) )) {
                        videoName = ((JSONObject)each).getString( "value" );
                        logger.warn( "设置videoName: " + videoName );
                    } else if ("videoFormat".equals( ((JSONObject)each).getString( "name" ) )) {
                        resolution = ((JSONObject)each).getString( "value" );
                        logger.warn( "设置resolution: " + resolution );
                    } else if ("vid".equals( ((JSONObject)each).getString( "name" ) )) {
                        vid = ((JSONObject)each).getString( "value" );
                        logger.warn( "设置vid: " + vid );
                    } else if ("duration".equals( ((JSONObject)each).getString( "name" ) )) {
                        duration = ((JSONObject)each).getIntValue( "value" );
                        logger.warn( "设置duration: " + duration );
                    } else if ("startPoint".equals( ((JSONObject)each).getString( "name" ) )) {
                        startPoint = ((JSONObject)each).getIntValue( "value" );
                        logger.warn( "设置startPoint: " + startPoint );
                    }
                }
            } catch (Exception e) {
                logger.error( "extraArgs解析失败: " + otherArgStr );
            }
        }

        driver.manage().timeouts().implicitlyWait( 1, TimeUnit.SECONDS );

        logger.warn( "测试准备: 启动" + testApp );
        driver.closeApp( DEVICE.getPackageName() );
        driver.launchApp( DEVICE.getPackageName() );
        TotoroUtils.sleep( 8000 );
        //跳过启动广告
        //        skipLaunchAd(driver, DEVICE.getPlatform());
    }

    public boolean openYoukuIphoneTestVideo() {
        logger.warn( "step1: 打开测试视频" );
        String scheme = String.format( "youku://play?vid=%s&point=%s&mode=1&quality=600", startPoint, vid );
        Router.IphoneSchemeLaunch( driver, scheme );
        logger.warn( scheme );
        return true;
    }

    public boolean openYoukuAndroidTestVideo() {
        logger.warn( "step1: 打开测试视频" );
        Router.AnroidSchemeLaunch( driver, String.format( "youku://play?source=stardetail\\&vid=%s\\&mode"
            + "=full_horizontal\\&quality=400\\&point=%s", vid, startPoint ), DEVICE.getDeviceId() );
        return true;
    }

    @After
    public void after() {
        super.after();
        logger.warn( "测试结束关闭APP" );
        driver.closeApp( DEVICE.getPackageName() );
    }

    public void setTestApp(String app) {
        testApp = app;
    }

    public void iphoneYoukuFullDetailPageBack() {
        TotoroUtils.sleep( 5000 );
        driver.click( 200, 200 );
        TotoroUtils.sleep( 1000 );
        WebElement backButton = null;
        try {
            backButton = driver.findElementByName( "返回" );
            backButton.click();
            TotoroUtils.sleep( 1000 );
            backButton = driver.findElementByName( "返回" );
            backButton.click();
            logger.warn( "成功返回" );
        } catch (Exception e) {
            logger.warn( "退出播放页失败" );
        }
    }

    public int loginAndroid() {
        WebElement myBtn = waitForElement( driver, Constant.LOGIN_ANDROID_MY_TEXT, 5 );
        if (myBtn != null) {
            myBtn.click();
            TotoroUtils.sleep( 2000 );
        } else {
            logger.warn( "在" + testApp + "中未找到我的按钮" );
            return -1;
        }

        if (testApp.equals( "优酷" )) {
            WebElement vipText = waitForElement( driver, Constant.LOGIN_ANDROID_YOUKU_UCENTER_GUIDE_ID, 3 );
            if (vipText != null) {
                String currentUser = vipText.getText();
                if (currentUser.equals( Constant.LOGIN_ANDROID_YOUKU_LOGINED_TEXT )) {
                    //需要登录
                    vipText.click();
                    TotoroUtils.sleep( 2000 );
                } else {
                    logger.warn( "在" + testApp + "已登录VIP" );
                    return 0;
                }
            }
            WebElement passportBtn = waitForElement( driver, Constant.LOGIN_ANDROID_YOUKU_SMS_ID, 3 );
            if (passportBtn == null) {
                WebElement ucenterGuideBtn = waitForElement( driver,
                    Constant.LOGIN_ANDROID_YOUKU_UCENTER_GUIDE_ID, 3 );
                if (ucenterGuideBtn != null) {
                    ucenterGuideBtn.click();
                    TotoroUtils.sleep( 2000 );
                } else {
                    logger.warn( "在" + testApp + "我的界面未找到登录/注册按钮" );
                    return -1;
                }
                passportBtn = waitForElement( driver, Constant.LOGIN_ANDROID_YOUKU_SMS_ID, 3 );
                if (passportBtn != null) {
                    passportBtn.click();
                    TotoroUtils.sleep( 2000 );
                } else {
                    logger.warn( "在" + testApp + "我的界面点击登录后未找到账号/手机号登录按钮" );
                    return -1;
                }
            } else {
                passportBtn.click();
                TotoroUtils.sleep( 2000 );
            }

            WebElement switchText = waitForElement( driver, Constant.LOGIN_ANDROID_YOUKU_SWITCH_ID, 3 );
            if (switchText != null) {
                String currentText = switchText.getText();
                if (currentText.equals( Constant.LOGIN_ANDROID_YOUKU_SWITCH_TEXT )) {
                    switchText.click();
                    TotoroUtils.sleep( 1000 );
                }
            }

            WebElement accountText = waitForElement( driver, Constant.LOGIN_ANDROID_YOUKU_ACCOUNT_ID, 3 );
            WebElement pwdText = waitForElement( driver, Constant.LOGIN_ANDROID_YOUKU_PWD_ID, 3 );
            if (accountText == null || pwdText == null) {
                logger.warn( "在" + testApp + "找不到账号或者密码输入框" );
                return -1;
            }
            accountText.sendKeys( Constant.LOGIN_ANDROID_YOUKU_ACCOUNT );
            pwdText.sendKeys( Constant.LOGIN_ANDROID_YOUKU_PWD );

            WebElement loginBtn = waitForElement( driver, Constant.LOGIN_ANDROID_YOUKU_LOGIN_BTN_ID, 3 );
            if (loginBtn != null) {
                loginBtn.click();
                TotoroUtils.sleep( 3000 );
            } else {
                logger.warn( "在" + testApp + "找不到登录按钮" );
                return -1;
            }

            WebElement notNowBtn = waitForElement( driver, "现在不", 3 );
            if (notNowBtn != null) {
                notNowBtn.click();
                TotoroUtils.sleep( 2000 );
            }
        }
        return 1;
    }

    public boolean openAndroidSearchPage() {
        TotoroUtils.sleep( 5000 );
        String searchBtnInfo;
        if (testApp.equals( "腾讯" )) {
            searchBtnInfo = Constant.TENCENT_SEARCH_BTN_IN_HOME;
        } else if (testApp.equals( "爱奇艺" )) {
            searchBtnInfo = Constant.IQIYI_SEARCH_BTN_IN_HOME;
        } else {
            searchBtnInfo = Constant.YOUKU_SEARCH_BTN_IN_HOME;
        }
        WebElement searchBtn;
        if (testApp.equals( "腾讯" )) {
            driver.back();
            TotoroUtils.sleep( 1000 );
            List<WebElement> searchBtns = waitForElements( driver, searchBtnInfo, 10 );
            if (searchBtns != null && searchBtns.size() > 0) {
                searchBtn = searchBtns.get( 0 );
            } else {
                searchBtn = null;
            }
        } else {
            searchBtn = waitForElement( driver, searchBtnInfo, 10 );
        }
        if (searchBtn != null) {
            searchBtn.click();
            return true;
        } else {
            logger.warn( "未在" + testApp + "首页找到搜索按钮" );
            return false;
        }
    }

    public boolean openYoukuIphoneSearchPage() {
        logger.debug( "step1: 搜索片源，进入搜索结果页" );
        Router.IphoneSchemeLaunch( driver, Constant.YOUKU_SEARCH_SCHEME_TEXT + showName.replaceAll( " ", "%20" ) );
        return true;
    }

    public boolean enterYoukuIphoneEpisode() {

        OcrElement ocrElement = ImageML.itamiOcrElement( videoName );
        if (ocrElement != null) {
            ocrElement.click( driver );
            logger.warn( "找到测试视频:" + videoName );
            return true;
        } else {
            logger.warn( "未找到测试视频:" + videoName );
            Log.addScreenShot( "未找到测试视频:" + videoName );
            return false;
        }
    }

    public boolean searchAndroidVideo() {
        String textItemInfo = null;
        if (testApp.equals( "优酷" )) {
            textItemInfo = Constant.YOUKU_SEARCH_EDIT_TEXT_ID;
        } else {
            textItemInfo = Constant.NOT_YOUKU_SEARCH_EDIT_TEXT_NAME;
        }
        WebElement searchTextEle = waitForElement( driver, textItemInfo, 5 );
        if (searchTextEle != null) {
            searchTextEle.click();
            searchTextEle.sendKeys( showName );
        } else {
            logger.warn( "未找到搜索输入框" );
            return false;
        }

        WebElement searchBtnEle = waitForElement( driver, Constant.ANDROID_SEARCH_BTN_TEXT, 5 );
        if (searchBtnEle != null) {
            searchBtnEle.click();
        } else {
            logger.warn( "未找到搜索按钮，使用adb回车命令" );
            ADBCommandUtils.exec( DEVICE.getDeviceId(), "shell input keyevent 66" );
        }
        return true;
    }

    public boolean enterAndroidEpisode(String episode) {
        if (episode.equals( "-1" )) { //电影，腾讯的查找信息有误，后续处理
            logger.warn( "当前播放的是电影" );
            String btnInfo;
            if (testApp.equals( "爱奇艺" )) {
                btnInfo = Constant.IQIYI_MOVIE_PLAY_BTN_ID_ANDROID;
            } else if (testApp.equals( "腾讯" )) {
                btnInfo = Constant.TENCENT_MOVIE_PLAY_BTN_ID_ANDROID;
            } else if ("哔哩哔哩".equals( testApp )) {
                btnInfo = Constant.BILIBILI_MOVIE_PLAY_BTN_ID_ANDROID;
            } else {
                btnInfo = Constant.YOUKU_MOVIE_PLAY_BTN_ID_ANDROID;
            }
            WebElement playBtn = waitForElement( driver, btnInfo, 5 );
            if (playBtn != null) {
                playBtn.click();
            } else {
                logger.warn( "未找到电影的播放按钮" );
                return false;
            }
        } else {
            if (testApp.equals( "优酷" )) {
                List<WebElement> episodeBtns = waitForElements( driver,
                    Constant.YOUKU_EPISODE_BTN_ID_ANDROID,
                    5 );
                if (null != episodeBtns && !episodeBtns.isEmpty()) {
                    episodeBtns.get( 0 ).click(); //点击更多按钮
                } else {
                    //IDLRect target = ImageML.itamiImageSearchInCurrentScreen( "more_dot.jpg" );
                    IDLRect target = null;
                    int retryCount = 0;
                    while (retryCount < 2) {
                        try {
                            target = ImageML.itamiImageSearchInCurrentScreenByIcon( more_dot_url );
                            break;
                        } catch (Exception e) {
                            retryCount++;
                        }
                    }
                    logger.warn( "尝试图像识别" );
                    if (target != null) {
                        driver.click( target.getX() + 20, target.getY() + 20 ); //点击更多按钮
                        System.out.println( target );
                    } else {
                        logger.warn( "未找到更多按钮" );
                        return false;
                    }
                }
                TotoroUtils.sleep( 2000 );
                HashSet<String> episodeSet = new HashSet();
                boolean findIt = false;
                boolean needScroll = true;
                while (needScroll) {
                    List<WebElement> possibleElements = waitForElements( driver, "android.widget.ImageView", 5 );
                    boolean allIn = true;
                    if (possibleElements != null) {
                        for (WebElement element : possibleElements) {
                            String text = element.getAttribute( "content-desc" );
                            if (text != null && text.contains( episode )) {
                                needScroll = false;
                                findIt = true;
                                element.click();
                                break;
                            } else {
                                if (!episodeSet.contains( text )) {
                                    allIn = false;
                                    episodeSet.add( text );
                                }
                            }
                        }
                    }
                    if (allIn || findIt) {
                        needScroll = false;
                    } else {
                        scrollUp();
                        TotoroUtils.sleep( 500 );
                    }
                }
                if (!findIt) {
                    logger.warn( "尝试图像识别" );
                    //IDLRect target = ImageML.itamiImageSearchInCurrentScreen( "choose_episode_playing.jpg" );
                    IDLRect target = null;
                    int retryCount = 0;
                    while (retryCount < 2) {
                        try {
                            target = ImageML.itamiImageSearchInCurrentScreenByIcon( choose_episode_playing_url );
                            break;
                        } catch (Exception e) {
                            retryCount++;
                        }
                    }
                    if (target != null) {
                        driver.click( target.getX() + 20, target.getY() + 20 ); //点击更多按钮
                        System.out.println( target );
                        findIt = true;
                    } else {
                        //target = ImageML.itamiImageSearchInCurrentScreen( "choose_episode.jpg" );
                        retryCount = 0;
                        while (retryCount < 2) {
                            try {
                                target = ImageML.itamiImageSearchInCurrentScreenByIcon( choose_episode_url );
                                break;
                            } catch (Exception e) {
                                retryCount++;
                            }
                        }
                        if (target != null) {
                            driver.click( target.getX() + 20, target.getY() + 20 ); //点击更多按钮
                            System.out.println( target );
                            findIt = true;
                        }
                    }
                }
                return findIt;
            } else {
                WebElement moreBtn = waitForElement( driver, Constant.NOT_YOUKU_MORE_BTN_NAME_ANDROID, 5 );
                if (moreBtn != null) {
                    moreBtn.click();
                } else {
                    logger.warn( "未找到更多按钮" );
                    return false;
                }
                TotoroUtils.sleep( 2000 );

                //腾讯和爱奇艺的选集列表都存在预告，且无法通过元素进行区分。预告出现的位置和优酷不同，可能间杂在正片中。
                //首先获取全部剧集数字，然后进行去重，若有重复数字，排在后面的为正片。
                //问题：出现在正片后方的预告，无法区分，暂时不进行处理，在测试时通过优酷的剧集列表确认选集数字的上限
                String textViewInfo = null;
                if (testApp.equals( "爱奇艺" )) {
                    textViewInfo = Constant.IQIYI_EPISODES_ITEM_XPATH_ANDROID;
                } else {
                    textViewInfo = Constant.TENCENT_EPISODES_ITEM_ID_ANDROID;
                }
                List<WebElement> episodeEles = new ArrayList<>();
                episodeEles = waitForElements( driver, textViewInfo, 5 );
                if (episodeEles == null || episodeEles.size() == 0) {
                    // try again
                    List<WebElement> possibleElements = driver.findElementsByClassName( "android.widget.TextView" );
                    for (WebElement element : possibleElements) {
                        if (element != null && element.getText() != null && element.getText().matches( "\\d+" )) {
                            episodeEles.add( element );
                        }
                    }

                    if (episodeEles == null || episodeEles.size() == 0) {
                        logger.warn( "找不到剧集列表" );
                        return false;
                    }
                }

                WebElement findEle = null;
                int cnt = 0;
                for (WebElement ele : episodeEles) {
                    if (ele.getText().equals( episode )) {
                        findEle = ele;
                        cnt++;
                        if (cnt == 2) {
                            break;
                        }
                    }
                }
                if (findEle != null) {
                    findEle.click();
                } else {
                    logger.warn( "未找到第 " + episode + " 集" );
                    return false;
                }
            }
        }

        return true;
    }

    public boolean enterAndroidFullScreen() {
        String fullScreenBtnInfo = null;
        String passAdInfo = null;
        if (testApp.equals( "爱奇艺" )) {
            fullScreenBtnInfo = Constant.IQIYI_FULL_SCREEN_BTN_ID_ANDROID;
            passAdInfo = Constant.IQIYI_SUGGEST_AD_TEXT_ANDROID;
        } else if (testApp.equals( "腾讯" )) {
            fullScreenBtnInfo = Constant.TENCENT_FULL_SCREEN_BTN_ID_ANDROID;
            TotoroUtils.sleep( 10000 );
        } else {
            fullScreenBtnInfo = Constant.YOUKU_FULL_SCREEN_BTN_ID_ANDROID;
            passAdInfo = Constant.YOUKU_SUGGEST_AD_ID_ANDROID;
        }

        //        WebElement adBtn = waitForElement(driver, passAdInfo, 3);
        //        if (adBtn != null) {
        //            try {
        //                adBtn.click();
        //                logger.warn("点击了跳过广告按钮");
        //                TotoroUtils.sleep(3000);
        //            } catch (Exception e) {
        //                logger.warn("进入全屏前找到了广告按钮但点击时出错，可忽略");
        //                logger.warn(e.getMessage());
        //            }
        //        }

        float[] coor = getClickCoordinate( driver, false, 0.5, 0.2 );
        if (coor == null) {
            logger.warn( "半屏播放时获取播放器点击坐标出错" );
            return false;
        }
        Log.addScreenShot( "进去全屏前的截图" );
        boolean inFullScreen = false;
        int times = 5;
        while (times > 0) {
            try {
                driver.click( coor[0], coor[1] );
                TotoroUtils.sleep( 1000 );
            } catch (Exception e) {
                logger.warn( "点击半屏播放器中心时出错: " + e.toString() );
                driver.click( 100, 100 );
            }
            WebElement fullScreenBtn = null;
            if (testApp.equals( "腾讯" )) {
                List<WebElement> btns = waitForElements( driver, fullScreenBtnInfo, 4 );
                if (btns != null) {
                    if (btns.size() >= 3) {
                        fullScreenBtn = btns.get( 2 );
                    } else if (btns.size() >= 2) {
                        fullScreenBtn = btns.get( 1 );
                    }
                }
            } else {
                fullScreenBtn = waitForElement( driver, fullScreenBtnInfo, 4 );
            }
            if (null != fullScreenBtn) {
                try {
                    fullScreenBtn.click();
                } catch (Exception e) {
                    logger.warn( "点击全屏按钮时按钮疑似消失: " + e.toString() );
                    continue;
                }
                inFullScreen = true;
                break;
            } else {
                times--;
                continue;
            }
        }

        if (testApp.equals( "腾讯" ) && !inFullScreen) {
            driver.click( coor[0], coor[1] );
            TotoroUtils.sleep( 1000 );
            float[] coorFullScreen = getClickCoordinate( driver, false, 0.938, 0.27 );
            if (coorFullScreen != null) {
                driver.click( coorFullScreen[0], coorFullScreen[1] );
                inFullScreen = true;
            }
        }

        if (!inFullScreen) {
            logger.warn( "未找到全屏按钮" );
        }
        return inFullScreen;
    }

    public void closeBarrageAndroid() {
        String barrageSettingInfo = null;
        String barrageSwitchInfo = null;
        if (testApp.equals( "爱奇艺" )) {
            barrageSettingInfo = Constant.IQIYI_BARRAGE_SETTING_ID_ANDROID;
            barrageSwitchInfo = Constant.IQIYI_BARRAGE_SWITCH_ID_ANDROID;
        } else if (testApp.equals( "腾讯" )) {
            //            barrageSettingInfo = Constant.TENCENT_BARRAGE_SETTING_ID_ANDROID;
            barrageSwitchInfo = Constant.TENCENT_BARRAGE_SWITCH_ID_ANDROID;
        } else {
            barrageSettingInfo = Constant.YOUKU_BARRAGE_SETTING_ID_ANDROID;
            barrageSwitchInfo = Constant.YOUKU_BARRAGE_SWITCH_ID_ANDROID;
        }

        float[] coor = getClickCoordinate( driver, true, 0.5, 0.5 );
        if (coor == null) {
            logger.warn( "全屏播放时获取播放器点击坐标出错" );
            return;
        }

        for (int i = 0; i < 3; i++) {
            try {
                driver.click( coor[0], coor[1] );
            } catch (Exception e) {
                logger.warn( "全屏播放时点击屏幕中心时出错: " + e.toString() );
            }
            TotoroUtils.sleep( 1000 );
            if (testApp.equals( "腾讯" )) {
                WebElement barrageSwitchBtn = waitForElement( driver, barrageSwitchInfo, 2 );
                if (barrageSwitchBtn != null) {
                    if (barrageSwitchBtn.getAttribute( "checked" ).equalsIgnoreCase( "true" )) {
                        barrageSwitchBtn.click();
                    }
                    break;
                }
            } else {
                WebElement barrageSettingBtn = waitForElement( driver, barrageSettingInfo, 2 );
                if (barrageSettingBtn != null) {
                    WebElement barrageSwitchBtn = waitForElement( driver, barrageSwitchInfo, 2 );
                    if (barrageSwitchBtn != null) {
                        barrageSwitchBtn.click();
                    }
                } else {
                    break;
                }
            }
        }
    }

    public boolean setResolutionAndroid() {
        String resolutionSetBtnInfo = null;
        if (testApp.equals( "爱奇艺" )) {
            resolutionSetBtnInfo = Constant.IQIYI_RESOLUTION_BTN_ID_ANDROID;
        } else if (testApp.equals( "腾讯" )) {
            resolutionSetBtnInfo = Constant.TENCENT_RESOLUTION_BTN_ID_ANDROID;
        } else {
            resolutionSetBtnInfo = Constant.YOUKU_RESOLUTION_BTN_ID_ANDROID;
        }
        int times = 5;
        WebElement definitionBtn = null;
        while (times > 0) {
            driver.click( 200, 200 );
            TotoroUtils.sleep( 1000 );
            definitionBtn = waitForElement( driver, resolutionSetBtnInfo, 4 );
            if (definitionBtn != null) {
                break;
            } else {
                TotoroUtils.sleep( 1000 );
                times--;
            }
        }

        if (definitionBtn != null) {
            String currentResolution = definitionBtn.getText();
            if (currentResolution.equals( resolution )
                || (resolution.equals( "1080P" ) && currentResolution.equals( "蓝光" ))
                || (resolution.equals( "720P" ) && currentResolution.equals( "超清" ))
                || (resolution.equals( "帧享" ) && currentResolution.equals( "HDR" ))) {
                logger.warn( "清晰度不需要设置" );
                return true;
            }
            definitionBtn.click();
            TotoroUtils.sleep( 2000 );

            String realResolution = null;
            if (testApp.equals( "爱奇艺" )) {
                switch (resolution) {
                    case "720P":
                        realResolution = "超清720P";
                        break;
                    case "1080P":
                        realResolution = "蓝光1080P";
                        break;
                    case "HDR":
                        realResolution = "爱奇艺HDR";
                        break;
                }
            } else {
                realResolution = resolution;
            }
            WebElement oneDefinitionBtn = waitForElement( driver, realResolution, 4 );
            if (oneDefinitionBtn != null) {
                oneDefinitionBtn.click();
            } else {
                logger.warn( "无法找到清晰度: " + resolution );
                return false;
            }
        } else {
            logger.warn( "无法找到清晰度设置按钮" );
            return false;
        }
        return true;
    }

    public void seekToAndroid(int time) {
        try {
            Dimension fullScreenSize = driver.getWindowSize();
            int sizeW = fullScreenSize.getWidth();
            int sizeH = fullScreenSize.getHeight();
            int realW, realH;
            realW = Math.max( sizeW, sizeH );
            realH = Math.min( sizeW, sizeH );
            float xFullScreenPos = (float)(realW * 0.5);
            float yFullScreenPos = (float)(realH * 0.5);

            float touchX = 0, touchY = 0, beginX, endX, length, rate;
            int mins;
            driver.click( xFullScreenPos, yFullScreenPos );
            //TotoroUtils.sleep(2000);

            if (testApp.equals( "优酷" )) {
                Rectangle currentTimeRect = driver.findElementById( Constant.YOUKU_CURRENT_VIDEO_TIME_ID_ANDROID )
                    .getRect();
                WebElement totalTimeEle = driver.findElementById( Constant.YOUKU_TOTAL_VIDEO_TIME_ID_ANDROID );
                Rectangle totalTimeRect = totalTimeEle.getRect();
                String totalTime = totalTimeEle.getText();
                touchY = currentTimeRect.getY() + (float)currentTimeRect.height / 2;
                beginX = currentTimeRect.getX() + currentTimeRect.width;
                endX = totalTimeRect.getX();
                length = endX - beginX;
                mins = getVideoTime( totalTime );
                rate = (float)time / mins;
                touchX = (int)(rate * length) + beginX;
            } else {
                String seekBarInfo, tatolTimeInfo;
                if (testApp.equals( "爱奇艺" )) {
                    seekBarInfo = Constant.IQIYI_SEEKBAR_ID_ANDROID;
                    tatolTimeInfo = Constant.IQIYI_TOTAL_VIDEO_TIME_ID_ANDROID;
                    beginX = (float)(realW * 0.137);
                    endX = (float)(realW * 0.862);
                } else {
                    seekBarInfo = Constant.TENCENT_SEEKBAR_ID_ANDROID;
                    tatolTimeInfo = Constant.TENCENT_TOTAL_VIDEO_TIME_ID_ANDROID;
                    beginX = (float)(realW * 0.066);
                    endX = (float)(realW * 0.935);
                }
                String totalTime;
                if (testApp.equals( "腾讯" )) {
                    List<WebElement> texts = waitForElements( driver, tatolTimeInfo, 2 );
                    if (texts != null && texts.size() >= 2) {
                        totalTime = texts.get( 1 ).getText();
                    } else {
                        totalTime = "40:00";
                    }
                } else {
                    totalTime = waitForElement( driver, tatolTimeInfo, 2 ).getText();
                }
                Rectangle seekBarRect = waitForElement( driver, seekBarInfo, 2 ).getRect();
                mins = getVideoTime( totalTime );
                touchY = seekBarRect.getY() + (float)seekBarRect.height / 2;
                length = endX - beginX;
                touchX = beginX + (float)time / mins * length;
            }
            driver.click( touchX, touchY );
            if (testApp.equals( "爱奇艺" )) {
                TotoroUtils.sleep( 100 );
                driver.click( touchX, touchY );
            }
        } catch (Exception e) {
            logger.warn( "SEEK到第" + time + "分钟时出错" );
        }
    }

    public void seekToIphone(int time) {

    }

    public void backToEpisodesAndroid() {
        for (int i = 0; i < 5; i++) {
            Map<String, String> map = ADBCommandUtils.getTopPackageAndActivity( DEVICE.getDeviceId() );
            if (map.get( "curPkg" ).equals( DEVICE.getPackageName() ) && map.get( "curAct" ).contains( "Search" )) {
                return;
            } else {
                driver.back();
                TotoroUtils.sleep( 1000 );
            }
        }
        logger.warn( "无法回到结果搜索页" );
    }

    public static WebElement waitForElement(MobileDriver<WebElement> driver, String info, int timeOut) {
        if (info == null) {
            return null;
        }
        WebElement result = null;
        while (timeOut > 0) {
            try {
                if (info.startsWith( "//" )) {
                    result = driver.findElementByXPath( info );
                } else if (info.startsWith( "android." )) {
                    result = driver.findElementByClassName( info );
                } else if (info.contains( ":id" )) {
                    result = driver.findElementById( info );
                } else {
                    result = driver.findElementByName( info );
                }
                if (null != result) {
                    break;
                } else {
                    TotoroUtils.sleep( 1000 );
                    timeOut--;
                }
            } catch (Exception e) {
                TotoroUtils.sleep( 1000 );
                timeOut--;
            }
        }
        return result;
    }

    public static List<WebElement> waitForElements(MobileDriver<WebElement> driver, String info, int timeOut) {
        if (info == null) {
            return null;
        }
        List<WebElement> result = null;
        while (timeOut > 0) {
            try {
                if (info.startsWith( "//" )) {
                    result = driver.findElementsByXPath( info );
                } else if (info.startsWith( "android." )) {
                    result = driver.findElementsByClassName( info );
                } else if (info.contains( ":id" )) {
                    result = driver.findElementsById( info );
                } else {
                    result = driver.findElementsByName( info );
                }
                if (null != result && !result.isEmpty()) {
                    break;
                } else {
                    TotoroUtils.sleep( 1000 );
                    timeOut--;
                }
            } catch (Exception e) {
                e.printStackTrace();
                TotoroUtils.sleep( 1000 );
                timeOut--;
            }
        }
        return result;
    }

    public static float[] getClickCoordinate(MobileDriver<WebElement> driver, boolean isFullScreen, double xRate,
                                             double yRate) {
        float[] coor = new float[4];
        Dimension windowSize = null;
        try {
            windowSize = driver.getWindowSize();
        } catch (Exception e) {
        }
        if (windowSize == null) {
            return null;
        }
        int sizeW, sizeH;
        try {
            sizeW = windowSize.getWidth();
            sizeH = windowSize.getHeight();
        } catch (Exception e) {
            logger.warn( e.getMessage() );
            return null;
        }
        if (isFullScreen) {
            if (sizeW < sizeH) {
                int temp = sizeW;
                sizeW = sizeH;
                sizeH = temp;
            }
        }
        coor[0] = (float)(sizeW * xRate);
        coor[1] = (float)(sizeH * yRate);
        coor[2] = sizeW;
        coor[3] = sizeH;
        return coor;
    }

    public static int getVideoTime(String timeInfo) {
        String[] times = timeInfo.split( ":" );
        int videoTime;
        if (times.length == 3) {
            videoTime = Integer.parseInt( times[0] ) * 60 + Integer.parseInt( times[1] );
            int sec = Integer.parseInt( times[2] );
            if (sec > 30) {
                videoTime += 1;
            }
        } else {
            videoTime = Integer.parseInt( times[0] );
            int sec = Integer.parseInt( times[1] );
            if (sec > 30) {
                videoTime += 1;
            }
        }
        return videoTime;
    }

    public static WebElement waitForElementIphone(MobileDriver<WebElement> driver, String info, int timeOut) {
        if (info == null) {
            return null;
        }
        WebElement result = null;
        while (timeOut > 0) {
            try {
                result = driver.findElementByName( info );
                if (null != result) {
                    break;
                } else {
                    TotoroUtils.sleep( 100 );
                    timeOut--;
                }
            } catch (Exception e) {
                TotoroUtils.sleep( 1000 );
                timeOut--;
            }
        }
        return result;
    }

    public void scrollUp() {
        Dimension screenSize = driver.getScreenSize();
        int screenHeight = screenSize.getHeight();
        int screenWidth = screenSize.getWidth();
        driver.swipe( screenWidth / 2f, screenHeight * 0.7, screenWidth / 2f, screenHeight * 0.3, 1000 );
    }
}
