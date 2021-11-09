package com.youku.avUniversal;

import com.totoro.client.utils.TotoroUtils;
import com.youku.avUniversal.Utils.CmdExecutor;
import com.youku.avUniversal.Utils.YoukuLogin;
import com.youku.itami.utility.OssUpload.FileTypeEnum;
import org.apache.http.client.fluent.Request;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 庭婳（meifang.ymf@alibaba-inc.com）
 * @date 2021/9/18 1:33 PM
 */
public class StandingPlayForIphone extends PlayerBaseCase {

    private static Logger logger = LoggerFactory.getLogger( StandingPlayForIphone.class );

    @Test
    public void testStandingPlay() {
        logger.warn( "开始测试" );
        if (showName == null || videoName == null) {
            logger.warn( "参数异常，请检查测试片源、测试剧集的传参是否正确" );
            return;
        }

        WebElement UserCenterButton = driver.findElementByName( "我的" );
        UserCenterButton.click();
        TotoroUtils.sleep( 2000 );
        driver.closeApp( DEVICE.getPackageName() );
        logger.warn( "关闭app" );
        driver.launchApp( DEVICE.getPackageName() );
        TotoroUtils.sleep( 5000 );
        YoukuLogin.login( itamiBaseCase, "13161700207", "youkuvip123" );
        logger.warn( "登录操作执行完成" );
        driver.closeApp( DEVICE.getPackageName() );
        logger.warn( "关闭app" );
        driver.launchApp( DEVICE.getPackageName() );
        logger.warn( "重启app" );
        TotoroUtils.sleep( 5000 );

        try {
            openYoukuIphoneTestVideo();
            TotoroUtils.sleep( 10000 );

            //logger.warn( "step3.3 设置分辨率为" + resolution );
            //if (!setResolutionAndroid()) {
            //    Log.addScreenShot( "第" + videoName + "集设置清晰度失败" );
            //}

            logger.warn( "step3.4 开始录像" );
            CmdExecutor cmdExecutor = new CmdExecutor();

            String time = new SimpleDateFormat( "yyyyMMddHHmmssSSS" ).format( new Date() );
            String recordDirectory = System.getProperty("user.home") + "/av-test/record/";
            String recordFileName = String.format( "%s-%s-%s.mp4", exeId, time, DEVICE.getDeviceId() );
            File folder = new File( recordDirectory );
            if (!folder.exists() || !folder.isDirectory()) {
                folder.mkdirs();
            }
            String cmd = String.format( "/Users/mayfeel/Work/Code/xrecord-pro/bin/xrecord --quicktime --id %s --out=%s --force --quality 540p", DEVICE.getDeviceId(),
                recordDirectory + recordFileName );
            logger.warn( "命令:" + cmd );

            int exitCode = cmdExecutor.execCmd( cmd.split( " " ), null, 60 );
            logger.warn( "step3.5 结束录像, 并上传oss调用魔镜分帧" );

            // 上传oss并调用魔镜进行分析
            try {
                String ossUrl = ossUpload.uploadFileToLongTerm( recordDirectory + recordFileName,
                    recordFileName, FileTypeEnum.ITAMI );
                logger.warn( "录屏ossUrl:" + ossUrl );
                // 触发魔镜分帧
                SplitFrame.callMirror( ossUrl, exeId );
            } catch (Throwable throwable) {
                logger.error( "上传oss失败" );
                throwable.printStackTrace();
            }

            logger.warn( "step3.6 测试结束" );
        } catch (Exception e) {
            logger.warn( e.toString() );
            backToEpisodesAndroid();
        }
    }

}