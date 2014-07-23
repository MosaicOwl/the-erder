package ru.alastar.main;

public class HelpExecutor implements CommandExecutor
{

    @Override
    public String getDescription()
    {
        return "Shows all commands of the validator. Usage:\n - help";
    }

    @Override
    public void execute(String[] args)
    {
        try{
        MainClass.Log("[Help]","Commands list:");
        
    for(String command: MainClass.clientCommands.keySet())
    {
        MainClass.Log("[Help]","[]"+command+" - "+ MainClass.clientCommands.get(command).getDescription());
    }
        }catch(Exception r)
        {
            MainClass.Log("[Command]",getDescription());
        }
    }

}
