package ru.alastar.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.alastar.main.net.Server;

public class Main
{

    public static File              logFile;
    public static BufferedWriter    writer  = null;
    public static SimpleDateFormat  dateFormat;
    public static ExecutorService   service;
    public static ArrayList<String> authors = new ArrayList<String>();

    public static void main(String[] args)
    {
        try
        {
            service = Executors.newCachedThreadPool();
            CreateLogFile();
            Server.LoadConfig();
            authors.add("Fess (Artyom) - idea creator, scenarist");
            authors.add("Igor - game designer, scenarist");
            authors.add("Alastar(Michael) - programmer");
            Log("[SERVER]",
                    "Game server version "
                            + Configuration.GetEntryValue("version")
                            + " starting");
            Log("[SERVER]", "Authors: ");
            for (String s : Main.authors)
            {
                Log("[AUTHOR]", s);
            }
            Server.startServer();
        } catch (Exception e)
        {
            Log("[SERVER]", e.getMessage());
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

    public static void Log(String prefix, String msg)
    {
        try
        {
            writer.write("["
                    + dateFormat.format(Calendar.getInstance().getTime()) + "]"
                    + prefix + ":" + msg + "\n");
            System.out.println(prefix + ":" + msg);
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
