package ru.alastar.main;

import ru.alastar.database.DatabaseClient;

public class CreateInventoryExecutor implements CommandExecutor
{

    @Override
    public void execute(String[] args)
    {
        try{
      String id = args[0];
      String max = args[1];
      
      DatabaseClient.commandExecute("INSERT INTO inventories(entityId, max) VALUES("+id+","+max+");");
      MainClass.Log("[Inventory]","Inventory has been created! Max: " + max); 
      }catch(Exception e)
      {
          MainClass.Log("[Command Usage]", this.getDescription());
          e.printStackTrace();
      }
    }

    @Override
    public String getDescription()
    {
        return "Creating inventory for the given entity. Usage:\n cinv {id} {max} - create inventory with given maximum items for the entity with given id;";
    }

}
