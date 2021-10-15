package com.youku.avUniversal;

import com.totoro.client.utils.TotoroUtils;
import com.youku.avUniversal.Utils.CmdExecutor;
import com.youku.avUniversal.Utils.YoukuLogin;
import com.youku.itami.utility.OssUpload.FileTypeEnum;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 庭婳（meifang.ymf@alibaba-inc.com）
 * @date 2021/9/18 1:33 PM
 */
public class StandingPlayForAndroid extends PlayerBaseCase {

    private static Logger logger = LoggerFactory.getLogger( StandingPlayForAndroid.class );
    //private String deviceId = " ";
    private String deviceId = " -s c4c4c852 ";

    //@Test
    //public void test(){
    //    CmdExecutor cmdExecutor = new CmdExecutor();
    //    // -s 指定不同手机
    //    String time = new SimpleDateFormat( "yyyyMMddHHmmssSSS" ).format( new Date() );
    //    String cmd = String.format( "scrcpy%s--max-fps 60 --bit-rate 2M --max-size 1080 -Nr
    // /Users/yktest/av-test/record/%s.mp4", deviceId, time );
    //    logger.warn( "命令:" + cmd );
    //
    //    try {
    //        int exitCode = cmdExecutor.execCmd( cmd.split( " " ), null, 30 );
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //    }
    //    logger.warn( "结束测试" );
    //}

    @Test
    public void testStandingPlay() {
        logger.warn( "开始测试" );
        if (showName == null || videoName ==null){
            logger.warn( "参数异常，请检查测试片源、测试剧集的传参是否正确" );
            return;
        }

        //YoukuLogin.login( itamiBaseCase, "13161700207", "youkuvip123" );
        //driver.closeApp( DEVICE.getPackageName() );
        //driver.launchApp( DEVICE.getPackageName() );
        //TotoroUtils.sleep( 8000 );

        logger.warn( "step1: 进入" + testApp + "搜索页" );
        if (!openAndroidSearchPage()) {
            logger.warn( "无法打开搜索页" );
            Log.addScreenShot( "无法打开搜索页" );
            return;
        } else {
            TotoroUtils.sleep( 2000 );
        }

        logger.warn( "step2：搜索片源 " + showName );
        if (!searchAndroidVideo()) {
            logger.warn( "无法搜索到片源" );
            Log.addScreenShot( "无法搜索到片源" );
            return;
        } else {
            TotoroUtils.sleep( 2000 );
        }

        logger.warn( "step3：开始循环测试" );
        try {
            logger.warn( "step3.1 打开第" + videoName + "集" );
            if (!enterAndroidEpisode( videoName )) {
                Log.addScreenShot( "无法打开第" + videoName + "集" );
                logger.error( "未找到需要视频" );
                throw new Exception( "无法打开第" + videoName + "集" );
            } else {
                TotoroUtils.sleep( 10000 );
            }

            logger.warn( "step3.2 进入第" + videoName + "集的全屏播放" );
            if (!enterAndroidFullScreen()) {
                Log.addScreenShot( "无法进入第" + videoName + "集的全屏" );
                throw new Exception( "无法进入第" + videoName + "集的全屏" );
            } else {
                TotoroUtils.sleep( 5000 );
            }

            //logger.warn("step3." + index + " 关闭弹幕");
            //closeBarrageAndroid();
            //TotoroUtils.sleep(4000);

            logger.warn( "step3.3 设置分辨率为" + resolution );
            if (!setResolutionAndroid()) {
                Log.addScreenShot( "第" + videoName + "集设置清晰度失败" );
            }

            logger.warn( "step3.4 开始录像" );
            CmdExecutor cmdExecutor = new CmdExecutor();
            // -s 指定不同手机
            String time = new SimpleDateFormat( "yyyyMMddHHmmssSSS" ).format( new Date() );
            String recordFileName = String.format( "%s-%s.mp4", exeId, time );
            String cmd = String.format(
                "scrcpy%s--max-fps 60 --bit-rate 2M --max-size 1080 -Nr /Users/yktest/av-test/record/%s",
                deviceId,
                recordFileName );
            logger.warn( "命令:" + cmd );

            int exitCode = cmdExecutor.execCmd( cmd.split( " " ), null, 60 );

            try {
                String ossUrl = ossUpload.uploadFileToLongTerm( "/Users/yktest/av-test/record/" + recordFileName,
                    recordFileName, FileTypeEnum.ITAMI );
                logger.warn( "录屏ossUrl:" + ossUrl );
            } catch (Throwable throwable) {
                logger.error( "上传oss失败" );
                throwable.printStackTrace();
            }

            logger.warn( "step3.5 结束录像" );

            backToEpisodesAndroid();
            Log.addStepName( "step3.6 结束测试第" + videoName + "集" );
        } catch (Exception e) {
            logger.warn( e.toString() );
            backToEpisodesAndroid();
        }
    }

}
