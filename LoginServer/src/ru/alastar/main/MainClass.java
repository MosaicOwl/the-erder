package ru.alastar.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainClass
{

    public static File             logFile;
    public static BufferedWriter   writer = null;
    public static SimpleDateFormat dateFormat;
    public static ExecutorService  service;

    public static void main(String[] args)
    {
        try
        {
            service = Executors.newCachedThreadPool();
            CreateLogFile();
            Server.LoadConfig();
            Server.startServer();
            ServerPooler.startServer();
        } catch (Exception e)
        {
            Log("[SERVER]", e.getMessage(), true);
            e.printStackTrace();
        }
    }

    private static void CreateLogFile()
    {
        try
        {
            File theDir = new File("logs");
            if (!theDir.exists())
            {
                try
                {
                    theDir.mkdir();
                } catch (SecurityException se)
                {
                }
            }
            dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String timeLog = dateFormat
                    .format(Calendar.getInstance().getTime());

            logFile = new File("logs/log-" + timeLog + ".txt");
            writer = new BufferedWriter(new FileWriter(logFile));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void Log(String prefix, String msg, boolean b)
    {
        try
        {
            if(b)
                msg += "\n";
            
            writer.write("["
                    + dateFormat.format(Calendar.getInstance().getTime()) + "]"
                    + prefix + ":" + msg);
            System.out.print(prefix + ":" + msg);
            writer.flush();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public static void Log(String prefix, String msg)
    {
         Log(prefix, msg, true);
    }
    
    public static void Log(String msg, boolean b)
    {
        try
        {
            if(b)
                msg += "\n";
            
            writer.write(msg);
            System.out.print(msg);
            writer.flush();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public static void HiddenLog(String prefix, String msg)
    {
        try
        {
            writer.write("["
                    + dateFormat.format(Calendar.getInstance().getTime()) + "]"
                    + prefix + ":" + msg + "\n");
            writer.flush();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
