package com.team.backend.utils;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.command.ExecStartResultCallback;

import java.io.File;
import java.io.IOException;

/**
 * @ClassName MySQLDatabaseBackupUtils
 * @Description TODO
 * @Author Colin
 * @Date 2023/10/30 15:03
 * @Version 1.0
 */
public class MySQLDatabaseBackupUtils {
    public static void backupMySQLTable(String host, String port, String user, String password, String database, String table, String backupFilePath) {
        String command = "mysqldump -h " + host + " -P " + port + " -u " + user + " -p" + password + " " + database + " " + table + " > " + backupFilePath;
        executeCommand(command);
    }

    public static void restoreMySQLTable(String host, String port, String user, String password, String database, String table, String restoreFilePath) {
        String command = "mysql -h " + host + " -P " + port + " -u " + user + " -p" + password + " " + database + " < " + restoreFilePath;
        executeCommand(command);
    }

    public static void executeCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Command executed successfully.");
            } else {
                System.err.println("Command execution failed.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}


