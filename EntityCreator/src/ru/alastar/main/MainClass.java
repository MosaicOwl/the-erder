package ru.alastar.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;

import com.alastar.game.enums.EntityType;

import ru.alastar.database.DatabaseClient;


public class MainClass
{  
    public static Hashtable<String, CommandExecutor> clientCommands = new Hashtable<String, CommandExecutor>();
    
    public static Hashtable<String, Skill> skills = new Hashtable<String, Skill>();
    public static Hashtable<String, Stat> stats = new Hashtable<String, Stat>();

    public static BufferedReader                     in             = new BufferedReader(
            new InputStreamReader(
                    System.in));

    public static String dbName = "theerder";

    public static String dbUser = "root";

    public static String dbPass = "";
    
    public static File                               logFile;
    public static BufferedWriter                     writer         = null;
    public static SimpleDateFormat                   dateFormat;
    
    public static void main(String[] args)
    {
        try
        {  
            SetUpLog();

            RegisterDefaults();
            RegisterCommands();
            DatabaseClient.Start();
            ListenToCommands();        
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private static void SetUpLog()
    {
        try
        {
            File theDir = new File("validatorlogs");
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

            logFile = new File("validatorlogs/log-" + timeLog + ".txt");
            writer = new BufferedWriter(new FileWriter(logFile));

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private static void RegisterDefaults()
    {
        Log("[Validator]","Registering defaults...");
        skills.put("Swords", new Skill("Swords", 0, 50, 5, "Strength", "Dexterity", 2));
        skills.put("Chivalry", new Skill("Chivalry", 0, 50, 5, "Strength", "Int", 2));
        skills.put("Magery", new Skill("Magery", 0, 50, 5, "Int", "Int", 2));
        skills.put("Lumberjacking", new Skill("Lumberjacking", 0, 50, 5, "Strength", "Dexterity", 2));
        skills.put("Mining", new Skill("Mining", 0, 50, 5, "Strength", "Int", 2));
        skills.put("Taming", new Skill("Taming", 0, 50, 5, "Int", "Strength", 2));
        skills.put("Necromancy", new Skill("Necromancy", 0, 50, 5, "Int", "Int", 2));
        skills.put("Parrying", new Skill("Parrying", 0, 50, 5, "Dexterity", "Strength", 2));
        skills.put("Herding", new Skill("Herding", 0, 50, 5, "Int", "Int", 2));
        skills.put("Carpentry", new Skill("Carpentry", 0, 50, 5, "Int", "Strength", 2));
        
        stats.put("Hits", new Stat("Hits", 10, 50, 5, 2));
        stats.put("Strength", new Stat("Strength", 5, 50, 5, 2));
        stats.put("Dexterity", new Stat("Dexterity", 5, 50, 5, 2));
        stats.put("Int", new Stat("Int", 5, 50, 5, 2));
        stats.put("Mana", new Stat("Mana", 20, 20, 5, 2));

    }

    private static void RegisterCommands()
    {  
        Log("[Validator]","Registering commands...");
        
        clientCommands.put("help", new HelpExecutor());
        clientCommands.put("cinv", new CreateInventoryExecutor());      
        clientCommands.put("validate", new ValidateExecutor());      
        clientCommands.put("create", new CreateExecutor());
    }

    private static void ListenToCommands()
    {    
        MainClass.Log("[Validator]","Ready! Now, enter the command:");
        String line;
        for (;;)
        {
            try
            {
                line = in.readLine();
                if (line != null)
                {
                    ProcessCommand(line);
                    continue;
                }
                else
                {
                    continue;
                }
            } catch (IOException e)
            {
                Log("[ERROR]", e.getLocalizedMessage());
            }
        }        
    }

    public static void ProcessCommand(String line)
    {
            if (clientCommands.containsKey(line.split(" ")[0].toLowerCase()))
            {
                String[] args = new String[line.split(" ").length - 1];
                for (int i = 0; i < line.split(" ").length - 1; ++i)
                {
                    args[i] = line.split(" ")[i + 1];
                }
                    clientCommands.get(line.split(" ")[0].toLowerCase()).execute(args);
            } else
                System.out.println("Invalid command to client");
    }  
    
    public static void Log(String prefix, String msg)
    {  
        try
    {
        System.out.println(prefix + ":" + msg);
        writer.write("["
                + dateFormat.format(Calendar.getInstance().getTime()) + "]"
                + prefix + ":" + msg + "\n");

            writer.flush();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    static int getFreeId()
    {
        try
        {
            ResultSet rs = DatabaseClient
                    .commandExecute("SELECT max(id) as id FROM entities");
            int i = 0;
            if (rs.next())
            {
                i = rs.getInt("id");
            }
            return i + 1;
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return -1;
    }

    public static void Validate(int id, Hashtable<String, Skill> plrSkills,
            Hashtable<String, Stat> plrStats)
    {
        Hashtable<String, Skill> neededSkills = new Hashtable<String, Skill>();
        Hashtable<String, Stat> neededStats = new Hashtable<String, Stat>();
        
        fillNeededSkills(neededSkills, plrSkills);
        fillNeededStats(neededStats, plrStats);
        
        MainClass.Log("[Validate]","Executing gathered info...");
        for(Skill s: neededSkills.values())
        {
            DatabaseClient
            .commandExecute("INSERT INTO skills (sValue, mValue, hardness, entityId, name, primaryStat, secondaryStat, state) VALUES("
                    + s.value
                    + ","
                    + s.maxValue
                    + ","
                    + s.hardness
                    + ","
                    + id
                    + ",'"
                    + s.name
                    + "','"
                    + s.primaryStat
                    + "','"
                    + s.secondaryStat
                    + "',"
                    + s.state
                    + ")");
        }
        
        for(Stat s: neededStats.values())
        {
            DatabaseClient
            .commandExecute("INSERT INTO stats (sValue, mValue, hardness, entityId, name, state) VALUES("
                    + s.value
                    + ","
                    + s.maxValue
                    + ","
                    + s.hardness
                    + ","
                    + id
                    + ",'"
                    + s.name
                    + "',"
                    + s.state
                    + ")");
         }
    }

    private static void fillNeededStats(Hashtable<String, Stat> neededStats, Hashtable<String, Stat> plrStats)
    {
        for(String n: stats.keySet())
        {
            if(!plrStats.containsKey(n))
            {
                MainClass.Log("[Validate]","Missing stat " + n);
                neededStats.put(n, stats.get(n));
            }
        }
    }

    private static void fillNeededSkills(Hashtable<String, Skill> neededSkills, Hashtable<String, Skill> plrSkills)
    {
        for(String n: skills.keySet())
        {
            if(!plrSkills.containsKey(n))
            {     
                MainClass.Log("[Validate]","Missing skill " + n);
                neededSkills.put(n, skills.get(n));
            }
        }        
    }
    public static String getAllowedTypes()
    {
        String s = "";
        for(int i = 0; i < EntityType.values().length; ++i)
        {
            s += " - " + EntityType.values()[i].name() + ";\n";
        }
        return s;
    }

}
