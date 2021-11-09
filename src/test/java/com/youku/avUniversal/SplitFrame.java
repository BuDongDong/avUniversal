package com.youku.avUniversal;

import com.youku.itami.utility.OssUpload.FileTypeEnum;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.Charset;

/**
 * @author 庭婳（meifang.ymf@alibaba-inc.com）
 * @date 2021/11/9 5:10 PM
 */
public class SplitFrame {
    private static Logger logger = LoggerFactory.getLogger( SplitFrame.class );

    public static boolean callMirror (String ossUrl, String exeId) {
        try {
            String cutFrameUrlTemplate
                = "https://mirror-algorithm.alibaba.com/api/detectQrResult?videoUrl=%s&instanceId=%s&interval=40";
            String url = String.format( cutFrameUrlTemplate, ossUrl, exeId );
            logger.warn( "请求魔镜分帧url:" + url );
            String result = Request.Get( url ).execute().returnContent().asString( Charset.forName( "utf-8" ) );
            logger.warn( "分帧请求返回:" + result );
            return true;
        } catch (Throwable throwable) {
            logger.error( "上传oss失败" );
            throwable.printStackTrace();
            return false;
        }
    }

    public static void main( String [] args) {
        String recordDirectory = System.getProperty("user.home") + "/av-test/record/";
        File folder = new File( recordDirectory );
        if (!folder.exists() || !folder.isDirectory()) {
            folder.mkdirs();
        }
    }
}
