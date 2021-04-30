package com.escredit.base.util.command;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *   Process exec(String command)
     在单独的进程中执行指定的字符串命令

     Process exec(String command, String[] envp)
     在指定环境的单独进程中执行指定的字符串命令

     Process exec(String command, String[] envp, File dir)
     在有指定环境和工作目录的独立进程中执行指定的字符串命令

     Process exec(String[] cmdarray)
     在单独的进程中执行指定命令和变量

     Process exec(String[] cmdarray, String[] envp)
     在指定环境的独立进程中执行指定命令和变量

     Process exec(String[] cmdarray, String[] envp, File dir)
     在指定环境和工作目录的独立进程中执行指定的命令和变量

     command：一条指定的系统命令

     envp：环境变量字符串数组，其中每个环境变量的设置格式为name=value；如果子进程应该继承当前进程的环境，则该参数为null

     dir：子进程的工作目录；如果子进程应该继承当前进程的工作目录，则该参数为null

     cmdarray：包含所调用命令及其参数的数组
 *
 * @author xuwucheng
 * @date 2021/1/14 18:44
 */
public class LinuxCommandUtil {

    public static List<String> executeLinuxCmd(List<String> commands) {

        List<String> rspList = new ArrayList<>();
        Runtime run = Runtime.getRuntime();
        try {
            Process proc = run.exec("/bin/bash", null, null);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
            for (String line : commands) {
                out.println(line);
            }
            // 这个命令必须执行，否则in流不结束
            out.println("exit");
            String rspLine = "";
            while ((rspLine = in.readLine()) != null) {
                System.out.println(rspLine);
                rspList.add(rspLine);
            }
            proc.waitFor();
            in.close();
            out.close();
            proc.destroy();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return rspList;
    }

    public static String executeLinuxCmd(String command) {

        Runtime run = Runtime.getRuntime();
        try {
            Process process = run.exec(command);
            InputStream in = process.getInputStream();
            BufferedReader bs = new BufferedReader(new InputStreamReader(in));
            StringBuffer out = new StringBuffer();
            byte[] b = new byte[1024];
            for (int n; (n = in.read(b)) != -1;) {
                out.append(new String(b, 0, n));
            }
            in.close();
            process.destroy();

            return out.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
