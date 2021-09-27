package com.youku.avUniversal.Utils;

/**
 * @author 庭婳（meifang.ymf@alibaba-inc.com）
 * @date 2021/9/18 1:46 PM
 */

import com.youku.avUniversal.StandingPlayForAndroid;
import org.apache.commons.io.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;

public class CmdExecutor {

    private static Logger logger = LoggerFactory.getLogger( StandingPlayForAndroid.class );

    private final static int BUFFER_SIZE = 1024;

    private static class ProcessWorker extends Thread {
        private final Process process;
        private volatile int exitCode = -99;
        private volatile boolean completed = false;
        private volatile String output = "";

        private ProcessWorker(Process process) {
            this.process = process;
        }

        @Override
        public void run() {
            try (InputStreamReader reader = new InputStreamReader(
                process.getInputStream(), Charsets.UTF_8 )) {

                StringBuilder log = new StringBuilder();
                char[] buffer = new char[BUFFER_SIZE];
                int length;
                while ((length = reader.read( buffer )) != -1) {
                    log.append( buffer, 0, length );
                    logger.warn( new String( buffer ) );
                }
                output = log.toString();
                exitCode = process.waitFor();
                completed = true;
            } catch (InterruptedException | IOException e) {
                Thread.currentThread().interrupt();
            }
        }

        public int getExitCode() {
            return exitCode;
        }

        public String getOutput() {
            return output;
        }

        public boolean isCompleted() {
            return completed;
        }
    }

    public static int execCmd(String command, StringBuilder log, int timeoutSecond)
        throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder( command.split( " " ) );
        // 合并错误输出流
        processBuilder.redirectErrorStream( true );
        Process process = processBuilder.start();
        ProcessWorker processWorker = new ProcessWorker( process );
        int exitCode = processWorker.getExitCode();
        processWorker.start();
        try {
            processWorker.join( timeoutSecond * 1000 );
            if (processWorker.isCompleted()) {
                if (log != null) {
                    log.append( processWorker.getOutput() );
                }
                exitCode = processWorker.getExitCode();
            } else {
                process.destroyForcibly();
                processWorker.interrupt();
            }
        } catch (InterruptedException e) {
            processWorker.interrupt();
        }
        return exitCode;
    }
}
