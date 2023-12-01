package com.team.backend.utils;

import java.io.*;

import com.jcraft.jsch.*;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;

public class ExecRemoteDocker {
    static String host = "8.140.38.47";
    static String user = "root";
    static String password = "18611251246Deng!";
    public static ResultCodeEnum backup(String version) {
        String command = "cd /usr/Colin/team_management && ./backup.sh "+version;

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, 22);
            session.setPassword(password);

            // 配置连接
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            // 建立连接
            session.connect();

            // 执行远程命令
            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
            channelExec.setCommand(command);

            InputStream commandOutput = channelExec.getInputStream();
            channelExec.connect();
            // 等待命令执行完成
            while (!channelExec.isClosed()) {
                Thread.sleep(1000);
            }

            // 关闭连接
            channelExec.disconnect();
            session.disconnect();

            System.out.println("备份完成");
            System.out.println(commandOutput);

        } catch (Exception e) {
            e.printStackTrace();
            return ResultCodeEnum.BACKUP_FILE_WRONG;
        }
        return ResultCodeEnum.SUCCESS;
    }

    public static ResultCodeEnum recover(String version) {
        String command = "cd /usr/Colin/team_management && ./recover.sh "+version;

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, 22);
            session.setPassword(password);

            // 配置连接
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            // 建立连接
            session.connect();

            // 执行远程命令
            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
            channelExec.setCommand(command);

            InputStream commandOutput = channelExec.getInputStream();
            channelExec.connect();
            // 等待命令执行完成
            while (!channelExec.isClosed()) {
                Thread.sleep(1000);
            }

            // 关闭连接
            channelExec.disconnect();
            session.disconnect();

            System.out.println("复原完成");
            System.out.println(commandOutput);

        } catch (Exception e) {
            e.printStackTrace();
            return ResultCodeEnum.RECOVER_FILE_WRONG;
        }
        return ResultCodeEnum.SUCCESS;
    }
}
