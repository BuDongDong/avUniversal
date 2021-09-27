package com.youku.avUniversal;

import com.totoro.client.utils.TotoroUtils;
import com.youku.avUniversal.Utils.CmdExecutor;
import com.youku.itami.logic.other.YoukuLogin;
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
        if (!searchAndroidVideo()) {
            logger.warn( "无法搜索到片源" );
            Log.addScreenShot( "无法搜索到片源" );
            return;
        } else {
            TotoroUtils.sleep( 2000 );
        }

        logger.warn( "step3：开始循环测试" );
        for (int i = 0; i < episodes.length; i++) {
            try {
                int index = i + 1;
                logger.warn( "step3." + index + " 打开第" + episodes[i] + "集" );
                if (!enterAndroidEpisode( episodes[i] )) {
                    Log.addScreenShot( "无法打开第" + episodes[i] + "集" );
                    logger.error( "未找到需要视频" );
                    throw new Exception( "无法打开第" + episodes[i] + "集" );
                } else {
                    TotoroUtils.sleep( 10000 );
                }

                logger.warn( "step3." + index + " 进入第" + episodes[i] + "集的全屏播放" );
                if (!enterAndroidFullScreen()) {
                    Log.addScreenShot( "无法进入第" + episodes[i] + "集的全屏" );
                    throw new Exception( "无法进入第" + episodes[i] + "集的全屏" );
                } else {
                    TotoroUtils.sleep( 5000 );
                }

                //logger.warn("step3." + index + " 关闭弹幕");
                //closeBarrageAndroid();
                //TotoroUtils.sleep(4000);

                logger.warn( "step3." + index + " 设置分辨率为" + resolution );
                if (!setResolutionAndroid()) {
                    Log.addScreenShot( "第" + episodes[i] + "集设置清晰度失败" );
                    throw new Exception( "第" + episodes[i] + "集设置清晰度失败" );
                }

                logger.warn( "step3." + index + " 开始录像" );
                CmdExecutor cmdExecutor = new CmdExecutor();
                // -s 指定不同手机
                String time = new SimpleDateFormat( "yyyyMMddHHmmssSSS" ).format( new Date() );
                //String cmd = String.format( "scrcpy%s--max-fps 60 --bit-rate 2M --max-size 1080 -Nr %s.mp4", deviceId,
                String cmd = String.format( "scrcpy%s--max-fps 60 --bit-rate 2M --max-size 1080 -Nr /Users/yktest/av-test/record/%s.mp4", deviceId,
                    time );
                int exitCode = cmdExecutor.execCmd( cmd, null, 30 );

                logger.warn( "step3." + index + " 结束录像" );

                backToEpisodesAndroid();
                Log.addStepName( "step3." + index + " 结束测试第" + episodes[i] + "集" );
            } catch (Exception e) {
                logger.warn( e.toString() );
                backToEpisodesAndroid();
            }
        }
    }

}
