package com.xile.script.utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * date: 2017/5/23 10:42
 *
 * @scene 执行shell指令 需root权限
 */

public class CMDUtil {
    private static final String COMMAND_SU = "su";
    private static final String COMMAND_SH = "sh";
    private static final String COMMAND_EXIT = "exit\n";
    private static final String COMMAND_LINE_END = "\n";


    private CMDUtil() {
        throw new AssertionError();
    }

    /**
     * 执行shell指令 无回执
     *
     * @param cmd
     */
    public static void execShell(String cmd) {
        try {
            // 权限设置
            Process process = Runtime.getRuntime().exec(COMMAND_SU);
            // 获取输出流
            OutputStream outputStream = process.getOutputStream();
            process.getErrorStream();
            // 将命令写入
            outputStream.write(cmd.getBytes(StandardCharsets.UTF_8));
            outputStream.write(COMMAND_LINE_END.getBytes());
            // 提交命令
            outputStream.flush();
            // 关闭流操作
            outputStream.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * 执行shell指令 有回执
     *
     * @param cmd
     */
    public static boolean execShellWaitFor(String cmd) throws InterruptedException {
        Process process = null;
        DataOutputStream outputStream = null;
        try {
            process = Runtime.getRuntime().exec(COMMAND_SU);
            outputStream = new DataOutputStream(process.getOutputStream());
            outputStream.writeBytes(cmd + COMMAND_LINE_END);
            outputStream.writeBytes(COMMAND_EXIT);
            outputStream.flush();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        return process.waitFor() == 0;
    }


    /**
     * 更改文件或文件夹的可执行权限  改为可读可写可执行  需有root权限
     *
     * @param pkgCodePath
     * @return boolean hasPermission
     */
    public static boolean upgradeRootPermission(String pkgCodePath) throws InterruptedException {
        Process process = null;
        DataOutputStream os = null;
        try {
            String cmd = "chmod -R 777 " + pkgCodePath;
            process = Runtime.getRuntime().exec(COMMAND_SU);
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        return process.waitFor() == 0;
    }


    /**
     * 检查是否有ROOT权限
     *
     * @return
     */
    public static boolean checkRootPermission() {
        return execCommand(new String[]{"echo root"}, true, false).result == 0;
    }


    /**
     * 执行shell指令 有回执
     *
     * @param commands
     * @param isRoot
     * @param isNeedResultMsg
     */
    public static CommandResult execCommand(String[] commands, boolean isRoot, boolean isNeedResultMsg) {
        int result = -1;
        if (commands == null || commands.length == 0) {
            return new CommandResult(result, null, null);
        }

        Process process = null;
        InputStream successResult = null;
        InputStream errorResult = null;
        StringBuilder successMsg = null;
        StringBuilder errorMsg = null;

        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec(isRoot ? COMMAND_SU : COMMAND_SH);
            os = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (command == null) {
                    continue;
                }

                // donnot use os.writeBytes(commmand), avoid chinese charset error
                os.writeBytes(command);
                os.writeBytes(COMMAND_LINE_END);
                os.flush();
            }
            os.writeBytes(COMMAND_EXIT);
            os.flush();

            result = process.waitFor();
            // get command result
            if (isNeedResultMsg) {
                successMsg = new StringBuilder();
                errorMsg = new StringBuilder();
                successResult = process.getInputStream();
                errorResult = process.getErrorStream();
                byte[] b = new byte[successResult.available()];
                successResult.read(b);
                successMsg.append(new String(b));
                byte[] be = new byte[errorResult.available()];
                errorResult.read(be);
                successMsg.append(new String(be));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (process != null) {
                process.destroy();
            }
        }
        return new CommandResult(result, successMsg == null ? null : successMsg.toString().trim(), errorMsg == null ? null : errorMsg.toString().trim());
    }

    public static class CommandResult {
        public int result;
        public String successMsg;
        public String errorMsg;

        public CommandResult(int result) {
            this.result = result;
        }

        public CommandResult(int result, String successMsg, String errorMsg) {
            this.result = result;
            this.successMsg = successMsg;
            this.errorMsg = errorMsg;
        }

        @Override
        public String toString() {
            return "CommandResult{" +
                    "result=" + result +
                    ", successMsg='" + successMsg + '\'' +
                    ", errorMsg='" + errorMsg + '\'' +
                    '}';
        }
    }

}
