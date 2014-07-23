package ru.alastar.main;

import ru.alastar.database.DatabaseClient;

public class CreateExecutor implements CommandExecutor
{

    @Override
    public void execute(String[] args)
    {
        try{
      String accountId = args[0];
      String caption = args[1];
      String type = args[2];
      String worldId = args[3];
      String x = args[4];
      String y = args[5];
      String z = args[6];
      String max = args[6];
      int id = MainClass.getFreeId();
      
     DatabaseClient.commandExecute("INSERT INTO entities(id, accountId, caption, type, worldId, x, y, z) VALUES("+id+","+accountId+",'"+caption+"', '"+type+"',"+worldId+","+x+","+y+","+z+");");
     
     DatabaseClient.commandExecute("INSERT INTO inventories(entityId, max) VALUES("+id+","+max+");");
    
     for( Skill s: MainClass.skills.values())
     {
         DatabaseClient
         .commandExecute("INSERT INTO skills (sValue, mValue, hardness, entityId, name, primaryStat, secondaryStat) VALUES("
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
                 + s.secondaryStat + "')");
     }  
     for( Stat s: MainClass.stats.values())
     {
     DatabaseClient
     .commandExecute("INSERT INTO stats (sValue, mValue, hardness, entityId, name) VALUES("
             + s.value
             + ","
             + s.maxValue
             + ","
             + s.hardness
             + ","
             + id
             + ",'"
             + s.name + "')");
      }
        }catch(Exception e)
        {
            MainClass.Log("[Command Usage]", this.getDescription());
            e.printStackTrace();
        }
    }

    @Override
    public String getDescription()
    {
        return "Creating new entity. Usage:\n create {accountId} {caption} {type} {worldId} {x} {y} {z} {max items in inv} - Create entity with given properties;\nAllowed types:\n"+MainClass.getAllowedTypes();
    }

}
