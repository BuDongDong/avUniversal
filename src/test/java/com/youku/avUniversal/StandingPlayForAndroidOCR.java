package com.youku.avUniversal;

import com.totoro.client.deeplearning.adstract.OcrElement;
import com.totoro.client.utils.ADBCommandUtils;
import com.totoro.client.utils.TotoroUtils;
import com.youku.avUniversal.Utils.CmdExecutor;
import com.youku.avUniversal.Utils.Constant;
import com.youku.itami.core.ItamiBaseCase;
import com.youku.itami.utility.ImgHandler.ImageML.ImageML;
import com.youku.itami.utility.ImgHandler.ScreenShot;
import mirror.MirrorImage;
import model.OcrModel;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * @author 庭婳（meifang.ymf@alibaba-inc.com）
 * @date 2021/9/18 1:33 PM
 */
public class StandingPlayForAndroidOCR extends PlayerBaseCase {

    private static Logger logger = LoggerFactory.getLogger( StandingPlayForAndroidOCR.class );
    //private String deviceId = " ";
    private String deviceId = " -s c4c4c852 ";

    @Test
    public void testStandingPlay() {
        logger.warn( "开始测试" );
        if (videoName == null || episodes == null) {
            logger.warn( "参数异常，请检查测试片源、测试剧集的传参是否正确" );
            return;
        }
        //
        //YoukuLogin(itamiBaseCase, )

        logger.warn( "step1: 进入" + testApp + "搜索页" );
        if (!openAndroidSearchPage()) {
            logger.warn( "无法打开搜索页" );
            Log.addScreenShot( "无法打开搜索页" );
            return;
        } else {
            TotoroUtils.sleep( 2000 );
        }

        logger.warn( "step2：搜索片源 " + videoName );

        String textItemInfo = null;
        if (testApp.equals( "优酷" )) {
            textItemInfo = Constant.YOUKU_SEARCH_EDIT_TEXT_ID;
        } else {
            textItemInfo = Constant.NOT_YOUKU_SEARCH_EDIT_TEXT_NAME;
        }
        WebElement searchTextEle = waitForElement( driver, textItemInfo, 5 );
        if (searchTextEle != null) {
            searchTextEle.click();
            searchTextEle.sendKeys( videoName );
        } else {
            logger.warn( "未找到搜索输入框" );
            Log.addScreenShot( "未找到搜索输入框" );
            return;
        }

        OcrElement ocrElement = ImageML.itamiOcrElement( "搜索" );
        if (ocrElement != null) {
            ocrElement.click( driver );
        } else {
            logger.warn( "无法搜索到片源" );
            Log.addScreenShot( "无法搜索到片源" );
            return;
        }

        TotoroUtils.sleep( 2000 );

        ocrElement = ImageML.itamiOcrElement( "更多" );
        if (ocrElement != null) {
            ocrElement.click( driver );
            logger.warn( "点击更多按钮" );
        } else {
            logger.warn( "未找到更多按钮" );
            Log.addScreenShot( "未找到更多按钮" );
            return;
        }

        logger.warn( "step3：开始查找指定视频" );
        if (episodes.length == 0) {
            logger.warn( "未指定测试视频" );
            return;
        }
        try {
            logger.warn( "step3.1 打开: " + episodes[0] );
            String baseImage = ScreenShot.ScreenShot( itamiBaseCase );

            boolean findIt = false;
            int counter = 0;
            while (counter < 20) {
                ocrElement = ImageML.itamiOcrElement( episodes[0].replaceAll( " ", "" ) );
                if (ocrElement != null) {
                    logger.warn( "找到指定视频并点击" );
                    ocrElement.click( driver );
                    findIt = true;
                    break;
                } else {
                    scrollUp();
                    TotoroUtils.sleep( 500 );
                    boolean isSimilarity = ImageML.similarityCurrentCompare( itamiBaseCase, baseImage, 0.9 );
                    if (isSimilarity) {
                        break;
                    }
                    baseImage = ScreenShot.ScreenShot( itamiBaseCase );
                }
                counter++;
            }

            if (!findIt) {
                logger.warn( "未找到指定视频" );
                return;
            }

            TotoroUtils.sleep( 5000 );

            logger.warn( "step3.2 进入全屏播放" );
            if (!enterAndroidFullScreen()) {
                logger.warn( "无法进入全屏" );
                Log.addScreenShot( "无法进入全屏" );
                return;
            }

            TotoroUtils.sleep( 5000 );

            //logger.warn("step3." + index + " 关闭弹幕");
            //closeBarrageAndroid();
            //TotoroUtils.sleep(4000);

            logger.warn( "step3.3 设置分辨率为" + resolution );
            if (!setResolutionAndroid()) {
                logger.warn( "设置清晰度失败" );
                Log.addScreenShot( "设置清晰度失败" );
                return;
            }

            logger.warn( "step3.4 开始录像" );
            CmdExecutor cmdExecutor = new CmdExecutor();
            // -s 指定不同手机
            String time = new SimpleDateFormat( "yyyyMMddHHmmssSSS" ).format( new Date() );
            String cmd = String.format(
                "scrcpy%s--max-fps 60 --bit-rate 2M --max-size 1080 -Nr /Users/yktest/av-test/record/%s.mp4",
                deviceId,
                time );
            logger.warn( "命令:" + cmd );

            int exitCode = cmdExecutor.execCmd( cmd.split( " " ), null, 30 );

            logger.warn( "step3.5 结束录像" );

            backToEpisodesAndroid();
            logger.warn( "step4 结束测试" );
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn( e.toString() );
            backToEpisodesAndroid();
        }
    }

}
