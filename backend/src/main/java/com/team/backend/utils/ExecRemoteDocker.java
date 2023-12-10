package com.team.backend.utils;

import java.io.*;

import com.jcraft.jsch.*;
import com.team.backend.config.RemoteConfig;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ExecRemoteDocker {
    public static ResultCodeEnum backup(RemoteConfig remoteConfig,String version) {
        String command = "cd "+remoteConfig.getBackupDir()+" && ./backup.sh "+version;

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(remoteConfig.getUsername(), remoteConfig.getIp(), 22);
            session.setPassword(remoteConfig.getPassword());

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

    public static ResultCodeEnum recover(RemoteConfig remoteConfig,String version) {
        String command = "cd "+remoteConfig.getBackupDir()+" && ./recover.sh "+version;

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(remoteConfig.getUsername(), remoteConfig.getIp(), 22);
            session.setPassword(remoteConfig.getPassword());

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
