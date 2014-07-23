package ru.alastar.main;

import java.sql.ResultSet;
import java.util.Hashtable;

import ru.alastar.database.DatabaseClient;

public class ValidateExecutor implements CommandExecutor
{

    @Override
    public void execute(String[] args)
    { 
        try
    {
        Hashtable<String, Skill> plrSkills;
        Hashtable<String, Stat> plrStats;

      if(args[0].equals("all"))
      {
          MainClass.Log("[Validate]","Starting validate ALL entities...");
          ResultSet rs = DatabaseClient.commandExecute("SELECT * FROM entities");

         while(rs.next())
         {   
             MainClass.Log("[Validate]","Validating " + rs.getString("caption")+"("+rs.getInt("id")+")...");

           ResultSet skillsRS = DatabaseClient.commandExecute("SELECT * FROM skills WHERE entityId="+rs.getInt("id"));
            plrSkills = new Hashtable<String, Skill>();
           while(skillsRS.next())
           {
               plrSkills.put(skillsRS.getString("name"), new Skill(skillsRS.getString("name"),skillsRS.getInt("sValue"),skillsRS.getInt("mValue"),skillsRS.getFloat("hardness"),skillsRS.getString("primaryStat"),skillsRS.getString("secondaryStat"), skillsRS.getInt("state")));
           }       
           ResultSet statsRS = DatabaseClient.commandExecute("SELECT * FROM stats WHERE entityId="+rs.getInt("id"));
           plrStats = new Hashtable<String, Stat>();
          while(statsRS.next())
          {
              plrStats.put(statsRS.getString("name"), new Stat(statsRS.getString("name"),statsRS.getInt("sValue"),statsRS.getInt("mValue"),statsRS.getFloat("hardness"), statsRS.getInt("state")));
          }
          
          MainClass.Validate(rs.getInt("id"), plrSkills, plrStats);
          }
         MainClass.Log("[Validate]","Entities have been validated!");
      }
      else
      {
          ResultSet rs = DatabaseClient.commandExecute("SELECT * FROM entities WHERE id=" + args[0]);

         while(rs.next())
         {   
             MainClass.Log("[Validate]","Validating " + rs.getString("caption")+"("+rs.getInt("id")+")...");

           ResultSet skillsRS = DatabaseClient.commandExecute("SELECT * FROM skills WHERE entityId="+rs.getInt("id"));
            plrSkills = new Hashtable<String, Skill>();
           while(skillsRS.next())
           {
               plrSkills.put(skillsRS.getString("name"), new Skill(skillsRS.getString("name"),skillsRS.getInt("sValue"),skillsRS.getInt("mValue"),skillsRS.getFloat("hardness"),skillsRS.getString("primaryStat"),skillsRS.getString("secondaryStat"), skillsRS.getInt("state")));
           }       
           ResultSet statsRS = DatabaseClient.commandExecute("SELECT * FROM stats WHERE entityId="+rs.getInt("id"));
           plrStats = new Hashtable<String, Stat>();
          while(statsRS.next())
          {
              plrStats.put(statsRS.getString("name"), new Stat(statsRS.getString("name"),statsRS.getInt("sValue"),statsRS.getInt("mValue"),statsRS.getFloat("hardness"), statsRS.getInt("state")));
          }
          MainClass.Validate(rs.getInt("id"), plrSkills, plrStats);
          }  
         MainClass.Log("[Validate]","Entity has been validated!");

      }
      } catch (Exception e)
      {   
          MainClass.Log("[Command Usage]", this.getDescription());
          e.printStackTrace();
      }
    }
    @Override
    public String getDescription()
    {
        return "Adding missing skills and stats to the entity. Usage:\n validate all - validates ALL entities;\n validate {id} - validates entity with given id;";
    }

}
