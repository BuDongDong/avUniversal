package com.youku.avUniversal;

import com.totoro.client.deeplearning.adstract.OcrElement;
import com.totoro.client.utils.ADBCommandUtils;
import com.totoro.client.utils.TotoroUtils;
import com.youku.avUniversal.Utils.CmdExecutor;
import com.youku.avUniversal.Utils.Constant;
import com.youku.avUniversal.Utils.YoukuLogin;
import com.youku.itami.utility.ImgHandler.ImageML.ImageML;
import com.youku.itami.utility.Login.Login;
import com.youku.itami.utility.OssUpload.FileTypeEnum;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author 庭婳（meifang.ymf@alibaba-inc.com）
 * @date 2021/9/18 1:33 PM
 */
public class StandingPlayForAndroid extends PlayerBaseCase {

    private static Logger logger = LoggerFactory.getLogger(StandingPlayForAndroid.class);
    private int openMobizen = 1;

    @Test
    public void testStandingPlay() {
        logger.warn("开始测试123");
        if (showName == null || videoName == null) {
            logger.warn("参数异常，请检查测试片源、测试剧集的传参是否正确");
            return;
        }

        try {
            Login.login(driver, itamiBaseCase, ACCOUNT_HAVANA_ID, ACCOUNT_SSO_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("免密登录失败, 尝试UI自动化登录");
        }
        if (!YoukuLogin.YoukuLoginAndroid(driver, ACCOUNT_EMAIL, ACCOUNT_SECRET)) {
            logger.warn("ui自动化登录失败");
        }

        logger.warn("登录操作执行完成");
        driver.closeApp(DEVICE.getPackageName());
        logger.warn("关闭app");
        driver.launchApp(DEVICE.getPackageName());
        logger.warn("重启app");
        driver.unRegisterUIWatcher("LiveWatcher");//注销watcher
        TotoroUtils.sleep(5000);
        try {
            if (openMobizen == 1) {
                logger.warn("mobizen录屏准备");
                WebElement mobizen_home = waitForElement(driver, Constant.MOBIZEN_HOME_BUTTON, 4);
                if (mobizen_home == null) {
                    logger.warn("尝试kill调mobizen进程并重启");
                    ADBCommandUtils.exec(DEVICE.getDeviceId(), "shell", "am", "force-stop", "com.rsupport.mvagent");
                    ADBCommandUtils.exec(DEVICE.getDeviceId(), "shell", "monkey", "-p", "com.rsupport.mvagent", "1");
                    TotoroUtils.sleep(2000);
                    mobizen_home = waitForElement(driver, Constant.MOBIZEN_HOME_BUTTON, 4);
                    if (mobizen_home != null) {
                        mobizen_home.click();
                    } else {
                        logger.warn("尝试录屏异常1");
                    }
                    TotoroUtils.sleep(1000);
                    WebElement mobizen_record = waitForElement(driver, Constant.MOBIZEN_RECORD_BUTTON, 4);
                    if (mobizen_record != null) {
                        mobizen_record.click();
                        logger.warn("尝试开始使用mobizen录屏");
                        TotoroUtils.sleep(2000);
                    } else {
                        WebElement mobizen_ready = waitForElement(driver, Constant.MOBIZEN_RECORD_READY, 4);
                        if (mobizen_ready != null) {
                            mobizen_ready.click();
                            logger.warn("尝试开始使用mobizen录屏2");
                            TotoroUtils.sleep(2000);
                            WebElement mobizen_confirm = waitForElement(driver, Constant.MOBIZEN_CONFIRM_BUTTON, 4);
                            if (mobizen_confirm != null) {
                                mobizen_confirm.click();
                                logger.warn("尝试正式开启开始使用mobizen录屏");
                            }
                        } else {
                            logger.warn("尝试录屏异常22");
                        }
                    }
                    TotoroUtils.sleep(5000);
                    mobizen_home = waitForElement(driver, Constant.MOBIZEN_HOME_BUTTON, 4);
                    if (mobizen_home != null) {
                        mobizen_home.click();
                    } else {
                        logger.warn("尝试录屏异常3");
                    }
                    TotoroUtils.sleep(1000);
                    WebElement mobizen_stop = waitForElement(driver, Constant.MOBIZEN_STOP_BUTTON, 4);
                    if (mobizen_stop != null) {
                        mobizen_stop.click();
                        logger.warn("尝试结束录屏");
                    } else {
                        logger.warn("尝试录屏异常4");
                    }
                    TotoroUtils.sleep(1000);

                    WebElement mobizen_close = waitForElement(driver, Constant.MOBIZEN_CLOSE_BUTTON, 4);
                    if (mobizen_close != null && (mobizen_close.getText().contains("关闭") || mobizen_close.getText()
                        .contains("以后再说"))) {
                        mobizen_close.click();
                        logger.warn("尝试关闭录屏");
                    } else {
                        logger.warn("尝试录屏异常5");
                    }
                }
            }
            TotoroUtils.sleep(3000);

            openYoukuAndroidTestVideo();
            TotoroUtils.sleep(5000);

            logger.warn("step3.3 设置分辨率为" + resolution);
            if ((streamType == null || streamType.isEmpty()) && !setResolutionAndroid()) {
                Log.addScreenShot("第" + videoName + "集设置清晰度失败");
            }
            TotoroUtils.sleep(5000);

            logger.warn("step3.4 开始录像");
            CmdExecutor cmdExecutor = new CmdExecutor();
            String time = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
            String recordDirectory = System.getProperty("user.home") + "/av-test/record/";
            String recordFileName = String.format("%s-%s-%s.mp4", exeId, time, DEVICE.getDeviceId());
            File folder = new File(recordDirectory);
            if (!folder.exists() || !folder.isDirectory()) {
                folder.mkdirs();
            }

            driver.unRegisterUIWatcher("LiveWatcher");//注销watcher
            if (openMobizen == 1) {
                ADBCommandUtils.exec(DEVICE.getDeviceId(), "shell", "rm", "-r", "/sdcard/Movies/Mobizen");
                TotoroUtils.sleep(1000);

                WebElement mobizen_home = waitForElement(driver, Constant.MOBIZEN_HOME_BUTTON, 4);
                if (mobizen_home != null) {
                    mobizen_home.click();
                } else {
                    logger.error("录屏异常1");
                }
                TotoroUtils.sleep(1000);

                WebElement mobizen_record = waitForElement(driver, Constant.MOBIZEN_RECORD_BUTTON, 4);
                if (mobizen_record != null) {
                    mobizen_record.click();
                    logger.warn("尝试开始使用mobizen录屏");
                } else {
                    WebElement mobizen_ready = waitForElement(driver, Constant.MOBIZEN_RECORD_READY, 4);
                    if (mobizen_ready != null) {
                        mobizen_ready.click();
                        logger.warn("尝试开始使用mobizen录屏2");
                        TotoroUtils.sleep(2000);
                        WebElement mobizen_confirm = waitForElement(driver, Constant.MOBIZEN_CONFIRM_BUTTON, 4);
                        if (mobizen_confirm != null) {
                            mobizen_confirm.click();
                            logger.warn("尝试正式开启开始使用mobizen录屏");
                        }
                    } else {
                        logger.warn("尝试录屏异常22");
                    }
                }
                TotoroUtils.sleep(duration * 1000);

                mobizen_home = waitForElement(driver, Constant.MOBIZEN_HOME_BUTTON, 4);
                if (mobizen_home != null) {
                    mobizen_home.click();
                } else {
                    logger.error("录屏异常3");
                }
                TotoroUtils.sleep(1000);
                WebElement mobizen_stop = waitForElement(driver, Constant.MOBIZEN_STOP_BUTTON, 4);
                if (mobizen_stop != null) {
                    mobizen_stop.click();
                    logger.warn("结束录屏");
                } else {
                    logger.error("录屏异常4");
                }
                TotoroUtils.sleep(1000);

                WebElement mobizen_close = waitForElement(driver, Constant.MOBIZEN_CLOSE_BUTTON, 4);
                if (mobizen_close != null && (mobizen_close.getText().contains("关闭") || mobizen_close.getText()
                    .contains("以后再说"))) {
                    mobizen_close.click();
                    logger.warn("关闭录屏");
                } else {
                    logger.error("录屏异常5");
                }
                TotoroUtils.sleep(1000);
                driver.back();
                TotoroUtils.sleep(1000);
                driver.back();

                logger.warn("开始拷贝视频文件");
                String tmpDirectory = recordDirectory + "tmp/" + exeId + "/";
                File tmpFolder = new File(tmpDirectory);
                if (!tmpFolder.exists() || !tmpFolder.isDirectory()) {
                    tmpFolder.mkdirs();
                }
                ADBCommandUtils.exec(DEVICE.getDeviceId(), "pull", "/sdcard/Movies/Mobizen", tmpDirectory);
                String mobizenDirectory = tmpDirectory + "Mobizen/";
                File mobizenFolder = new File(mobizenDirectory);
                if (mobizenFolder.exists()) {
                    String[] files = mobizenFolder.list();
                    if (files.length > 0) {
                        File oldFile = new File(mobizenDirectory + files[0]);
                        File newFile = new File(recordDirectory + recordFileName);
                        if (oldFile.renameTo(newFile)) {
                            logger.warn("文件移动成功");
                        } else {
                            logger.error("文件移动失败");
                        }
                    }
                }
            } else {
                String cmd = String.format(
                    "scrcpy -s %s --max-fps 60 --bit-rate 2M --max-size 1080 -Nr %s", DEVICE.getDeviceId(),
                    recordDirectory + recordFileName);
                logger.warn("命令:" + cmd);
                int exitCode = cmdExecutor.execCmd(cmd.split(" "), null, duration);
            }

            logger.warn("step3.5 结束录像, 并上传oss调用魔镜分帧");
            try {
                File recordFile = new File(recordDirectory + recordFileName);
                if (recordFile.exists()) {
                    String ossUrl = ossUpload.uploadFileToLongTerm(recordDirectory + recordFileName,
                        recordFileName, FileTypeEnum.ITAMI);
                    logger.warn("录屏ossUrl:" + ossUrl);
                    // 触发魔镜分帧
                    SplitFrame.callMirror(ossUrl, exeId);
                } else {
                    logger.error("未找到录屏文件:" + recordDirectory + recordFileName);
                }
            } catch (Throwable throwable) {
                logger.error("上传oss失败");
                throwable.printStackTrace();
            }

            logger.warn("step3.6 测试结束");
        } catch (Exception e) {
            logger.warn(e.toString());
            driver.back();
            TotoroUtils.sleep(1000);
            driver.back();
        }
    }

    //@Deprecated
    //public void testStandingPlayOld() {
    //    logger.warn( "开始测试" );
    //    if (showName == null || videoName == null) {
    //        logger.warn( "参数异常，请检查测试片源、测试剧集的传参是否正确" );
    //        return;
    //    }
    //
    //    //YoukuLogin.login( itamiBaseCase, "13161700207", "youkuvip123" );
    //    //driver.closeApp( DEVICE.getPackageName() );
    //    //driver.launchApp( DEVICE.getPackageName() );
    //    //TotoroUtils.sleep( 8000 );
    //
    //    logger.warn( "step1: 进入" + testApp + "搜索页" );
    //    if (!openAndroidSearchPage()) {
    //        logger.warn( "无法打开搜索页" );
    //        Log.addScreenShot( "无法打开搜索页" );
    //        return;
    //    } else {
    //        TotoroUtils.sleep( 2000 );
    //    }
    //
    //    logger.warn( "step2：搜索片源 " + showName );
    //    if (!searchAndroidVideo()) {
    //        logger.warn( "无法搜索到片源" );
    //        Log.addScreenShot( "无法搜索到片源" );
    //        return;
    //    } else {
    //        TotoroUtils.sleep( 2000 );
    //    }
    //
    //    logger.warn( "step3：开始循环测试" );
    //    try {
    //        logger.warn( "step3.1 打开第" + videoName + "集" );
    //        if (!enterAndroidEpisode( videoName )) {
    //            Log.addScreenShot( "无法打开第" + videoName + "集" );
    //            logger.error( "未找到需要视频" );
    //            throw new Exception( "无法打开第" + videoName + "集" );
    //        } else {
    //            TotoroUtils.sleep( 10000 );
    //        }
    //
    //        logger.warn( "step3.2 进入第" + videoName + "集的全屏播放" );
    //        if (!enterAndroidFullScreen()) {
    //            Log.addScreenShot( "无法进入第" + videoName + "集的全屏" );
    //            throw new Exception( "无法进入第" + videoName + "集的全屏" );
    //        } else {
    //            TotoroUtils.sleep( 5000 );
    //        }
    //
    //        //logger.warn("step3." + index + " 关闭弹幕");
    //        //closeBarrageAndroid();
    //        //TotoroUtils.sleep(4000);
    //
    //        logger.warn( "step3.3 设置分辨率为" + resolution );
    //        if (!setResolutionAndroid()) {
    //            Log.addScreenShot( "第" + videoName + "集设置清晰度失败" );
    //        }
    //
    //        logger.warn( "step3.4 开始录像" );
    //        CmdExecutor cmdExecutor = new CmdExecutor();
    //
    //        String time = new SimpleDateFormat( "yyyyMMddHHmmssSSS" ).format( new Date() );
    //        String recordDirectory = "/Users/yktest/av-test/record/";
    //        String recordFileName = String.format( "%s-%s-%s.mp4", exeId, time, DEVICE.getDeviceId() );
    //        String cmd = String.format(
    //            "scrcpy -s %s--max-fps 60 --bit-rate 2M --max-size 1080 -Nr %s",
    //            DEVICE.getDeviceId(),
    //            recordDirectory + recordFileName );
    //        logger.warn( "命令:" + cmd );
    //
    //        int exitCode = cmdExecutor.execCmd( cmd.split( " " ), null, 60 );
    //
    //        // 上传oss并调用魔镜进行分析
    //        try {
    //            String ossUrl = ossUpload.uploadFileToLongTerm( "/Users/yktest/av-test/record/" + recordFileName,
    //                recordFileName, FileTypeEnum.ITAMI );
    //            logger.warn( "录屏ossUrl:" + ossUrl );
    //            // 触发魔镜分帧
    //            String cutFrameUrlTemplate
    //                = "https://mirror-algorithm.alibaba.com/api/detectQrResult?videoUrl=%s&instanceId=%s&interval=40";
    //            String url = String.format( cutFrameUrlTemplate, ossUrl, exeId );
    //            logger.warn( "请求魔镜分帧url:" + url );
    //            String result = Request.Get( url ).execute().returnContent().asString( Charset.forName( "utf-8" ) );
    //            logger.warn( "分帧请求返回:" + result );
    //        } catch (Throwable throwable) {
    //            logger.error( "上传oss失败" );
    //            throwable.printStackTrace();
    //        }
    //
    //        logger.warn( "step3.5 结束录像" );
    //
    //        backToEpisodesAndroid();
    //        Log.addStepName( "step3.6 结束测试第" + videoName + "集" );
    //    } catch (Exception e) {
    //        logger.warn( e.toString() );
    //        backToEpisodesAndroid();
    //    }
    //}
}
