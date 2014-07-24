package ru.alastar.main.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.alastar.game.enums.ItemType;
import com.alastar.game.enums.UpdateType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryonet.Connection;

import ru.alastar.database.DatabaseClient;
import ru.alastar.enums.ActionType;
import ru.alastar.enums.EntityType;
import ru.alastar.enums.EquipType;
import ru.alastar.game.Account;
import ru.alastar.game.Attributes;
import ru.alastar.game.Entity;
import ru.alastar.game.Equip;
import ru.alastar.game.Inventory;
import ru.alastar.game.Item;
import ru.alastar.game.PlantsType;
import ru.alastar.game.Skill;
import ru.alastar.game.Skills;
import ru.alastar.game.Statistic;
import ru.alastar.game.Stats;
import ru.alastar.game.ai.AI;
import ru.alastar.game.ai.BaseAI;
import ru.alastar.game.security.Crypt;
import ru.alastar.game.spells.Heal;
import ru.alastar.game.systems.CraftSystem;
import ru.alastar.game.systems.MagicSystem;
import ru.alastar.game.systems.gui.NetGUISystem;
import ru.alastar.game.systems.gui.hadlers.EquipButtonGUIHandler;
import ru.alastar.game.systems.gui.hadlers.InvButtonGUIHandler;
import ru.alastar.game.systems.gui.hadlers.PeaceButtonGUIHandler;
import ru.alastar.game.systems.gui.hadlers.SkillsButtonGUIHandler;
import ru.alastar.game.worldwide.Location;
import ru.alastar.main.Configuration;
import ru.alastar.main.Main;
import ru.alastar.main.handlers.*;
import ru.alastar.main.net.responses.AddCharacterResponse;
import ru.alastar.main.net.responses.AddSkillResponse;
import ru.alastar.main.net.responses.AddStatResponse;
import ru.alastar.main.net.responses.AddToContainerResponse;
import ru.alastar.main.net.responses.CreateContainerResponse;
import ru.alastar.main.net.responses.LoadWorldResponse;
import ru.alastar.main.net.responses.LoginResponse;
import ru.alastar.main.net.responses.MessageResponse;
import ru.alastar.main.net.responses.ProcessLoginResponse;
import ru.alastar.main.net.responses.RegisterResponse;
import ru.alastar.main.net.responses.SetData;
import ru.alastar.main.net.responses.SpeechResponse;
import ru.alastar.main.net.responses.UpdatePlayerResponse;
import ru.alastar.world.ServerWorld;

public class Server
{

    public static com.esotericsoftware.kryonet.Server           server;
    public static int                                           port         = 25565;
    public static Hashtable<InetSocketAddress, ConnectedClient> clients;
    public static Hashtable<Integer, ServerWorld>               worlds;
    public static Hashtable<Integer, Inventory>                 inventories;
    public static Hashtable<Integer, Entity>                    entities;
    public static Hashtable<String, Handler>                    commands;
    public static Hashtable<String, Float>                      plantsGrowTime;
    public static Hashtable<String, Handler>                    consoleCommands;
    public static Hashtable<Integer, AI>                        ais;
    public static Hashtable<Integer, Equip>                     equips;

    public static Random                                        random;
    public static float                                         syncDistance = 20;
    private static Hashtable<Integer, Item>                     items;
    public static ServerState                                   state = ServerState.Working;

    public static void startServer()
    {
        try
        {
            server = new com.esotericsoftware.kryonet.Server();
            server.start();
            server.bind(Integer.parseInt(Configuration.GetEntryValue("port")),
                    Integer.parseInt(Configuration.GetEntryValue("port")) + 1);
            server.addListener(new TListener(server));
            Init();
        } catch (IOException e)
        {
            handleError(e);
        }
    }

    public static void Init()
    {
        try
        {
            random = new Random();
            clients = new Hashtable<InetSocketAddress, ConnectedClient>();
            worlds = new Hashtable<Integer, ServerWorld>();
            inventories = new Hashtable<Integer, Inventory>();
            entities = new Hashtable<Integer, Entity>();
            commands = new Hashtable<String, Handler>();
            plantsGrowTime = new Hashtable<String, Float>();
            consoleCommands = new Hashtable<String, Handler>();
            ais = new Hashtable<Integer, AI>();
            equips = new Hashtable<Integer, Equip>();
            items = new Hashtable<Integer, Item>();
            
            if (DatabaseClient.Start())
            {
                LoadWorlds();
                LoadEntities();
                // LoadInventories();
                LoadPlants();
                FillWoods();
                FillMiningItems();
                SetupSpells();
                FillCommands();
                FillPlants();
                FillCrafts();
                try
                {
                    ServerRegistrator.StartClient();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            else
                Main.Log("[ERROR]", "MySQL database is not reachable!");
        } catch (InstantiationException e)
        {
            Main.Log("[ERROR]", e.getLocalizedMessage());
        } catch (IllegalAccessException e)
        {
            Main.Log("[ERROR]", e.getLocalizedMessage());
        } catch (ClassNotFoundException e)
        {
            Main.Log("[ERROR]", e.getLocalizedMessage());
        }
        try
        {
            ExecutorService service = Executors.newCachedThreadPool();
            service.submit(new Runnable()
            {
                public void run()
                {
                    try
                    {
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));

                        for (;;)
                        {
                            String line;

                            line = in.readLine();

                            if (line == null)
                            {
                                continue;
                            } else
                            {
                                if (consoleCommands.containsKey(line
                                        .toLowerCase().split(" ")[0]))
                                {
                                    consoleCommands.get(
                                            line.toLowerCase().split(" ")[0])
                                            .execute(
                                                    line.toLowerCase().split(
                                                            " "), null);
                                }
                            }
                            if ("save".equals(line.toLowerCase()))
                            {
                                Save();
                                continue;
                            }

                            if ("stop".equals(line.toLowerCase()))
                            {
                                Save();
                                server.close();
                                Main.writer.close();
                                break;
                            }

                            if ("encrypt".equals(line.split(" ")[0]
                                    .toLowerCase()))
                            {
                                System.out.println(Crypt.encrypt(line
                                        .split(" ")[1]));
                                continue;
                            }

                        }
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }

                private void Save()
                {

                }
            });
        } catch (Exception e)
        {
            handleError(e);
        }
    }

    private static AI LoadAI(Entity e)
    {
        try
        {
            ResultSet allAIs = DatabaseClient
                    .commandExecute("SELECT * FROM ai WHERE entityId="+e.id);
            AI ai = null;
            while (allAIs.next())
            {
                ai = (AI) Class.forName(allAIs.getString("class"))
                        .newInstance();
                ai.setEntity(e);
                ai.setReactionTime(allAIs.getFloat("reaction"));
                ais.put(allAIs.getInt("entityId"), ai);
                Main.HiddenLog("[AI]", "AI loaded. class: " + ai.getClassPath()
                        + ". entityId: " + ai.getEntity().id);
            }
            return ai;
        } catch (SQLException | InstantiationException | IllegalAccessException
                | ClassNotFoundException ex)
        {
            Server.handleError(ex);
        }
        return null;
    }

    private static void FillCrafts()
    {
        ArrayList<String> neededItems = new ArrayList<String>();
        Attributes attrs = new Attributes();

        neededItems.add("plain wood");
        neededItems.add("amber");
        attrs.addAttribute("Charges", 10);
        attrs.addAttribute("Durability", 100);

     //   CraftSystem.registerCraft("wooden_totem", new CraftInfo(neededItems,
     //           "Carpentry", 0, "Wooden Totem", EquipType.None,
     //           ActionType.Cast, attrs));
    }

    private static void FillPlants()
    {
        plantsGrowTime.put("wheat", (float) (24 * 60 * 60 * 1000));
    }

    private static void FillCommands()
    {
        registerCommand("help", new HelpHandler());

        registerConsoleCommand("save", new SaveHandler());
        registerConsoleCommand("stop", new StopHandler());
        registerConsoleCommand("worlds", new WorldsHandler());

    }

    public static void registerCommand(String key, Handler h)
    {
        try
        {
            commands.put(key, h);
        } catch (Exception e)
        {
            handleError(e);
        }
    }

    public static void registerConsoleCommand(String key, Handler h)
    {
        try
        {
            consoleCommands.put(key, h);
        } catch (Exception e)
        {
            handleError(e);
        }
    }

    private static void SetupSpells()
    {
        MagicSystem.addSpell("heal", new Heal());
    }

    private static void FillWoods()
    {
        Location.woods.put(0, "plain wood");
        Location.woods.put(10, "oak wood");
        Location.woods.put(25, "yew wood");
        Location.woods.put(45, "ash wood");
        Location.woods.put(50, "greatwood");
    }

    private static void FillMiningItems()
    {
        Location.miningItems.put(0, "copper ore");
        Location.miningItems.put(10, "iron ore");
        Location.miningItems.put(25, "shadow metal ore");
        Location.miningItems.put(25, "amber");
        Location.miningItems.put(30, "old corrored golem core");
        Location.miningItems.put(35, "emerald");
        Location.miningItems.put(45, "gold ore");
        Location.miningItems.put(50, "valor ore");
        Location.miningItems.put(50, "diamond");
        Location.miningItems.put(50, "swiftstone");
    }

    private static void LoadPlants()
    {

    }

    public static void SavePlant(PlantsType p)
    {
        // Main.Log("[DEBUG]","save plant grow");

        ResultSet plantsRs = DatabaseClient
                .commandExecute("SELECT * FROM plants WHERE locationId="
                        + p.loc.id);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");

        String time = sdf.format(p.finish);
        try
        {
            if (plantsRs.next())
            {
                DatabaseClient.commandExecute("UPDATE plants SET name='"
                        + p.plantName + "', growTime='" + time
                        + "' WHERE locationId=" + p.loc.id);

            } else
            {
                DatabaseClient
                        .commandExecute("INSERT INTO plants(name, growTime, locationId) VALUES('"
                                + p.plantName
                                + "','"
                                + time
                                + "',"
                                + p.loc.id
                                + ");");
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

    }

    private static ArrayList<Item> LoadItems(int i)
    {
        ArrayList<Item> items = new ArrayList<Item>();
        Attributes attrs;
        ResultSet itemsRS = DatabaseClient
                .commandExecute("SELECT * FROM items WHERE entityId=" + i);
        ResultSet attrsRS;
        try
        {
            while (itemsRS.next())
            {
                attrsRS = DatabaseClient
                        .commandExecute("SELECT * FROM attributes WHERE itemId="
                                + itemsRS.getInt("id"));
                // Loading attributes
                attrs = new Attributes();
                while (attrsRS.next())
                {
                    attrs.addAttribute(attrsRS.getString("name"),
                            attrsRS.getInt("value"));
                }

                items.add(new Item(itemsRS.getInt("id"), itemsRS
                        .getInt("entityId"), itemsRS.getString("caption"),
                        itemsRS.getInt("amount"), itemsRS.getInt("x"), itemsRS
                                .getInt("y"), itemsRS.getInt("z"), EquipType
                                .valueOf(itemsRS.getString("equipType")), ItemType.valueOf(itemsRS.getString("type")),
                        attrs, itemsRS.getInt("worldId")));
            }
        } catch (SQLException e)
        {
            handleError(e);
        }

        return items;
    }

    private static void LoadEntities()
    {
        try
        {
            ResultSet allEntities;

            Stats stats;
            Skills skills;
            ResultSet skillsRS;
            ResultSet statsRS;
            Entity e;

            allEntities = DatabaseClient
                    .commandExecute("SELECT * FROM entities WHERE accountId=-1");

            while (allEntities.next())
            {
                stats = new Stats();

                skills = new Skills();


                skillsRS = DatabaseClient
                        .commandExecute("SELECT * FROM skills WHERE entityId="
                                + allEntities.getInt("id"));
                statsRS = DatabaseClient
                        .commandExecute("SELECT * FROM stats WHERE entityId="
                                + allEntities.getInt("id"));
                while (skillsRS.next())
                {
                    skills.put(
                            skillsRS.getString("name"),
                            new Skill(skillsRS.getString("name"), skillsRS
                                    .getInt("sValue"), skillsRS
                                    .getInt("mValue"), skillsRS
                                    .getFloat("hardness"), skillsRS
                                    .getString("primaryStat"), skillsRS
                                    .getString("secondaryStat"),skillsRS
                                    .getInt("state")));
                }

                while (statsRS.next())
                {
                    stats.put(
                            statsRS.getString("name"),
                            new Statistic(statsRS.getString("name"), statsRS
                                    .getInt("sValue"),
                                    statsRS.getInt("mValue"), statsRS
                                            .getFloat("hardness"), statsRS
                                            .getInt("state")));
                }


                e = new Entity(allEntities.getInt("id"),
                        allEntities.getString("caption"),
                        EntityType.valueOf(allEntities.getString("type")),
                        allEntities.getInt("x"), allEntities.getInt("y"),
                        allEntities.getInt("z"), skills, stats,
                        getWorld(allEntities.getInt("worldId")));
                e.isAI = true;
                e.AI = LoadAI(e);
                LoadEquip(e);

                e.world.AddEntity(e);
                e.world.UpdateNear(e);
                entities.put(e.id, e);
               // Main.Log("[DEBUG]", "Non-player entity loaded");

            }
        } catch (SQLException e)
        {
            handleError(e);
        }

    }

    private static void LoadEquip(Entity e)
    {
        ResultSet eqRS = DatabaseClient.commandExecute("SELECT * FROM equip WHERE entityId=" + e.id);
        try
        {
            Equip eq = new Equip(e);
            while(eqRS.next())
            {
                eq.addEquipSlot(eqRS.getString("slotName"));
                if(eqRS.getInt("itemId") > -1){
                eq.addToEquip(eqRS.getString("slotName"), Server.LoadItem(eqRS.getInt("itemId")));
                }
            }
            equips.put(e.id, eq);

        } catch (SQLException e1)
        {
            handleError(e1);
        }
    }

    private static Item LoadItem(int int1)
    {  
       try
       {
        ResultSet rs = DatabaseClient.commandExecute("SELECT * FROM items WHERE id=" + int1);

            while(rs.next()){
               // Main.Log("[DEBUG]","Loading item for equip id " + int1);
                Item i = new Item(rs.getInt("id"), rs.getInt("entityId"), rs.getString("caption"),
                        rs.getInt("amount"), rs.getInt("x"), rs.getInt("y"), rs.getInt("z"), EquipType.valueOf(rs.getString("equipType")),
                        ItemType.valueOf(rs.getString("type")), getItemAttrs(rs.getInt("id")), rs.getInt("worldId"));
                items.put(rs.getInt("id"), i);
                return i;
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    return null;
    }

    private static Attributes getItemAttrs(int int1)
    {
        Attributes a = new Attributes();
        ResultSet rs = DatabaseClient.commandExecute("SELECT * FROM attributes WHERE itemId="+int1);
        try
        {
            while(rs.next()){
                a.addAttribute(rs.getString("name"), rs.getInt("value"));
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return a;
    }

    private static void LoadWorlds()
    {
        try
        {
            File worldDir = new File(System.getProperty("user.dir")
                    + "\\worlds\\");
            if (!worldDir.exists())
                worldDir.mkdir();

            String fileName = "";
            ServerWorld w;
            com.alastar.game.World clientW;
            FileInputStream f_in = null;
            ObjectInputStream obj_in = null;
            File worldFile;
            if (worldDir.listFiles().length > 0)
            {
                for (int i = 0; i < worldDir.listFiles().length; ++i)
                {
                    worldFile = worldDir.listFiles()[i];
                    fileName = worldFile.getName();
                    fileName = fileName.replaceAll(".bin", "");
                    f_in = new FileInputStream(worldFile);
                    obj_in = new ObjectInputStream(f_in);
                    clientW = (com.alastar.game.World) obj_in.readObject();
                    w = new ServerWorld(i, fileName, clientW.xMax,
                            clientW.xMin, clientW.yMax, clientW.yMin,
                            clientW.zMax, clientW.zMin);
                    w.tiles = clientW.tiles;
                    w.id = clientW.id;
                    w.version = clientW.version;
                    Main.Log("[LOAD]", "Loaded world " + w.name + " id: "
                            + w.id);
                    worlds.put(w.id, w);
                    obj_in.close();
                    f_in.close();
                }
            } else
            {
                Main.Log("[LOAD]", "World files not found!");
            }
        } catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public static boolean hasClient(Connection connection)
    {
        if (clients.containsKey(connection.getRemoteAddressUDP()))
            return true;
        else
            return false;
    }

    public static void addClient(Connection connection)
    {
        Main.Log("[Connection]","Client connected");
        clients.put(connection.getRemoteAddressUDP(), new ConnectedClient(
                connection));
    }

    public static void removeClient(Connection connection)
    {
        ConnectedClient c = getClient(connection);
        if (c.controlledEntity != null)
        {
           // Main.Log("[LOGIN]", "Controlled entity is not null, saving it...");
            c.controlledEntity.RemoveYourself(c.getAccountId());
        } else
           // Main.Log("[LOGIN]", "Controlled entity is null, skipping save");

        clients.remove(connection.getRemoteAddressUDP());
    }

    public static void Login(String login, String pass, Connection c)
    {
        try
        {
            Main.HiddenLog("[SERVER]", "Process auth for " + login + " password: " + pass);
            ServerRegistrator.getClient(login, pass);
            ConnectedClient client = getClient(c);
            client.awaitedLogin = login;
            
           /* ResultSet l = DatabaseClient
                    .commandExecute("SELECT * FROM accounts WHERE login='"
                            + login + "' AND password='" + Crypt.encrypt(pass)
                            + "'");
            if (l.next())
            {
                Main.HiddenLog("[SERVER]", "...auth succesful!");

                LoginResponse r = new LoginResponse();
                r.succesful = true;
                SendTo(c, r);

                ConnectedClient client = getClient(c);
                if (!client.logged)
                {
                    client.account = new Account(l.getInt("id"),
                            l.getString("login"), l.getString("password"),
                            l.getString("mail"));
                    client.logged = true;
                    SendCharacters(client);
                } else
                {
                    MessageResponse r1 = new MessageResponse();
                    r1.msg = "This account is already logged in!";
                    SendTo(c, r1);
                }
            } else
            {
                Main.HiddenLog("[SERVER]", "...auth unsuccesful(");

                LoginResponse r = new LoginResponse();
                r.succesful = false;
                SendTo(c, r);
            }
            */
        } catch (Exception e)
        {
            handleError(e);
        }
    }

    private static void SendCharacters(ConnectedClient c)
    {
        try
        {
            // Main.Log("[DEBUG]","Sending characters to account, id:" +
            // c.getAccountId());
            ResultSet chars = DatabaseClient
                    .commandExecute("SELECT * FROM entities WHERE accountId="
                            + c.getAccountId());
            AddCharacterResponse r = new AddCharacterResponse();
            while (chars.next())
            {
                r.name = chars.getString("caption");
                r.type = chars.getString("type");
                // Main.Log("[DEBUG]","Sending character " +
                // chars.getString("caption"));
                SendTo(c.connection, r);
            }
        } catch (SQLException e)
        {
            Server.handleError(e);
        }
    }

    private static void LoadPlayer(int i, ConnectedClient c)
    {
        try
        {
            ResultSet e = DatabaseClient
                    .commandExecute("SELECT * FROM entities WHERE id=" + i);

            if (e.next())
            {

                SetData sd = new SetData();
                LoadWorldResponse lw = new LoadWorldResponse();
                 Main.Log("[SERVER]", "Creating entity...");
                Stats stats = new Stats();
                Skills skills = new Skills();

                ResultSet skillsRS = DatabaseClient
                        .commandExecute("SELECT * FROM skills WHERE entityId="
                                + e.getInt("id"));
                ResultSet statsRS = DatabaseClient
                        .commandExecute("SELECT * FROM stats WHERE entityId="
                                + e.getInt("id"));

                while (skillsRS.next())
                {
                    skills.put(
                            skillsRS.getString("name"),
                            new Skill(skillsRS.getString("name"), skillsRS
                                    .getInt("sValue"), skillsRS
                                    .getInt("mValue"), skillsRS
                                    .getFloat("hardness"), skillsRS
                                    .getString("primaryStat"), skillsRS
                                    .getString("secondaryStat"),skillsRS
                                    .getInt("state")));
                }

                while (statsRS.next())
                {
                    stats.put(
                            statsRS.getString("name"),
                            new Statistic(statsRS.getString("name"), statsRS
                                    .getInt("sValue"),
                                    statsRS.getInt("mValue"), statsRS
                                            .getFloat("hardness"), statsRS
                                            .getInt("state")));
                }


                Entity entity = new Entity(e.getInt("id"),
                        e.getString("caption"), EntityType.valueOf(e
                                .getString("type")), e.getInt("x"),
                        e.getInt("y"), e.getInt("z"), skills, stats,
                        getWorld(e.getInt("worldId")));
                 Main.Log("[SERVER]",
                 "Assigning it as a controlled to the connected client...");
                c.controlledEntity = entity;
                sd.id = entity.id;
                lw.name = entity.world.name;
                 Main.Log("[SERVER]", "Sending set data packet...");
                SendTo(c.connection, sd);
                SendTo(c.connection, lw);
                LoadInventory(entity);
                LoadEquip(entity);

                entity.world.AddEntity(entity);
                 Main.Log("[SERVER]", "Sending other entities to it...");
                entity.world.UpdateNear(entity);
                 Main.Log("[SERVER]", "Sending stats...");
                AddStatResponse r = new AddStatResponse();
                for (String s : entity.stats.vals.keySet())
                {
                    r.name = s;
                    r.sValue = entity.stats.get(s).value;
                    r.mValue = entity.stats.get(s).maxValue;
                    SendTo(c.connection, r);
                }
                 Main.Log("[SERVER]", "Sending skills...");
                AddSkillResponse sr = new AddSkillResponse();
                for (String s : entity.skills.vals.keySet())
                {
                    sr.name = s;
                    sr.sValue = entity.skills.get(s).value;
                    sr.mValue = entity.skills.get(s).maxValue;
                    SendTo(c.connection, sr);

                }
                 Main.Log("[SERVER]", "Sending inventory...");
                CreateContainerResponse ccr = new CreateContainerResponse();
                ccr.name = "inv";
                ccr.max = getInventory(entity).maxItems;
                SendTo(c.connection, ccr);
                AddToContainerResponse ir = new AddToContainerResponse();
                ir.name = "inv";
                for (Item i1 : inventories.get(entity.id).items)
                {
                    
                    ir.id = i1.id;
                    ir.captiion = i1.caption;
                    ir.amount = i1.amount;
                    ir.type = i1.type;
                    ir.attrs = i1.attributes.values;
                    SendTo(c.connection, ir);
                }

                Main.Log("[SERVER]", "Data was sent to player. Fuf...");
                entities.put(entity.id, entity);

                NetGUISystem.OpenGUI(NetGUISystem.CreateGUIInfo("inv_button",
                        new Vector2(250, 10), new Vector2(100, 50), "",
                        "com.alastar.game.gui.GUIButton", "", "inventory"), c);
                c.controlledEntity.AddGUIHandler("inv_button",
                        new InvButtonGUIHandler());

                NetGUISystem.OpenGUI(NetGUISystem.CreateGUIInfo("peace_button",
                        new Vector2(250, 70), new Vector2(100, 50), "",
                        "com.alastar.game.gui.GUIButton", "", "peace"), c);
                c.controlledEntity.AddGUIHandler("peace_button",
                        new PeaceButtonGUIHandler()); 
                
                NetGUISystem.OpenGUI(NetGUISystem.CreateGUIInfo("equip_button",
                        new Vector2(250, 130), new Vector2(100, 50), "",
                        "com.alastar.game.gui.GUIButton", "", "Equip"), c);
                c.controlledEntity.AddGUIHandler("equip_button",
                        new EquipButtonGUIHandler());
                
                NetGUISystem.OpenGUI(NetGUISystem.CreateGUIInfo("skills_button",
                        new Vector2(250, 190), new Vector2(100, 50), "",
                        "com.alastar.game.gui.GUIButton", "", "Skills"), c);
                c.controlledEntity.AddGUIHandler("skills_button",
                        new SkillsButtonGUIHandler());

            }
        } catch (SQLException e1)
        {
            handleError(e1);
        }
    }

    private static void LoadInventory(Entity e)
    {
        ResultSet inventoriesRs = DatabaseClient
                .commandExecute("SELECT * FROM inventories WHERE entityId="
                        + e.id);
        Inventory i;
        try
        {
            if (inventoriesRs.next())
            {
                i = new Inventory(inventoriesRs.getInt("entityId"),
                        inventoriesRs.getInt("max"),
                        LoadItems(inventoriesRs.getInt("entityId")));
                inventories.put(i.entityId, i);
            }
        } catch (SQLException ex)
        {
            handleError(ex);
        }
    }

    private static ServerWorld getWorld(int int1)
    {
        try
        {
            return worlds.get(int1);
        } catch (Exception e)
        {
            Server.handleError(e);
        }
        return null;
    }

    public static void warnEntity(Entity e, String m)
    {
        try
        {
            warnEntity(e, m, 0);
        } catch (Exception er)
        {
            handleError(er);
        }
    }

    public static void warnEntity(Entity e, String m, int type)
    {
        try
        {
            if(getClient(e) != null){
            MessageResponse r = new MessageResponse();
            r.msg = m;
            r.type = type;
            SendTo(getClient(e).connection, r);}
        } catch (Exception er)
        {
            handleError(er);
        }
    }

    public static ConnectedClient getClient(Connection c)
    {
        return clients.get(c.getRemoteAddressUDP());
    }

    public static void SendTo(Connection c, Object o)
    {
        if (c != null)
            server.sendToUDP(c.getID(), o);
    }

    public static ConnectedClient getClient(Entity e1)
    {
        for (ConnectedClient c : clients.values())
        {
            if (c.controlledEntity != null)
            {
                if (c.controlledEntity.id == e1.id)
                {
                    return c;
                }
            }
        }
        return null;
    }

    public static void ProcessChat(String msg, Connection connection)
    {
        ConnectedClient c = getClient(connection);
        if (c.controlledEntity != null)
        {
            c.controlledEntity.world.sendAll(msg, c.controlledEntity.caption);
        }
    }

    public static void saveEntity(Entity entity, int id)
    {
        try
        {
            // Main.Log("[SAVE]", "Saving entity...");
            // Entity Main
            ResultSet entityEqRS = DatabaseClient
                    .commandExecute("SELECT * FROM entities WHERE id="
                            + entity.id);
            if (entityEqRS.next())
                DatabaseClient.commandExecute("UPDATE entities SET caption='"
                        + entity.caption + "', type='" + entity.type.name()
                        + "', worldId=" + entity.world.id + ", x="
                        + entity.pos.x + ", y=" + entity.pos.y + ", z="
                        + entity.pos.z + ", accountId=" + id + " WHERE id="
                        + entity.id);
            else
                DatabaseClient
                        .commandExecute("INSERT INTO entities(id, accountId, caption, type, worldId, x, y, z) VALUES("
                                + entity.id
                                + ","
                                + id
                                + ",'"
                                + entity.caption
                                + "', '"
                                + entity.type.name()
                                + "',"
                                + entity.world.id
                                + ", "
                                + entity.pos.x
                                + ","
                                + entity.pos.y + "," + entity.pos.z + ")");
            // Main.Log("[SAVE]", "Saving stats...");

            // Stats
            ResultSet statEqRS;
            for (Statistic s : entity.stats.vals.values())
            {
                statEqRS = DatabaseClient
                        .commandExecute("SELECT * FROM stats WHERE entityId="
                                + entity.id + " AND name='" + s.name + "'");

                if (statEqRS.next())
                    DatabaseClient.commandExecute("UPDATE stats SET sValue="
                            + s.value + ", mValue=" + s.maxValue
                            + ", hardness=" + s.hardness + ", state="+s.state+" WHERE entityId="
                            + entity.id + " AND name='" + s.name + "'");
                else
                    DatabaseClient
                            .commandExecute("INSERT INTO stats (sValue, mValue, hardness, entityId, name, state) VALUES("
                                    + s.value
                                    + ","
                                    + s.maxValue
                                    + ","
                                    + s.hardness
                                    + ","
                                    + entity.id
                                    + ",'"
                                    + s.name
                                    + "',"
                                    + s.state
                                    + ")");

            }
            // Main.Log("[SAVE]", "Saving skills...");

            // Skills
            ResultSet skillsEqRS;

            for (Skill s : entity.skills.vals.values())
            {
                skillsEqRS = DatabaseClient
                        .commandExecute("SELECT * FROM skills WHERE entityId="
                                + entity.id + " AND name='" + s.name + "'");
                if (skillsEqRS.next())
                    DatabaseClient.commandExecute("UPDATE skills SET sValue="
                            + s.value + ", mValue=" + s.maxValue
                            + ", hardness=" + s.hardness + ", primaryStat='"
                            + s.primaryStat + "', secondaryStat='"
                            + s.secondaryStat + "', state="+s.state+" WHERE entityId=" + entity.id
                            + " AND name='" + s.name + "'");
                else
                    DatabaseClient
                            .commandExecute("INSERT INTO skills (sValue, mValue, hardness, entityId, name, primaryStat, secondaryStat, state) VALUES("
                                    + s.value
                                    + ","
                                    + s.maxValue
                                    + ","
                                    + s.hardness
                                    + ","
                                    + entity.id
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
            // Main.Log("[SAVE]", "Saving inventory...");

            // Inventory
            Inventory inv = inventories.get(entity.id);
            if (inv != null)
            {
                saveInventory(inv);
            }
            
        } catch (SQLException e)
        {
            handleError(e);
        }
    }

    public static void ProcessRegister(String login, String pass, String mail,
            String name, String race, Connection connection)
    {
        try
        {
            ResultSet regRS = DatabaseClient
                    .commandExecute("SELECT * FROM accounts WHERE login='"
                            + login + "' AND mail='" + mail + "'");
            RegisterResponse r = new RegisterResponse();

            if (regRS.next())
            {
                r.successful = false;
                Server.SendTo(connection, r);
            } else
            {
                CreateAccount(login, pass, mail,
                        getClient(connection));
                r.successful = true;
                Server.SendTo(connection, r);
            }
        } catch (SQLException e)
        {
            handleError(e);
        }
    }

    private static void CreateAccount(String login, String pass, String mail,
             ConnectedClient client)
    {
        ResultSet accountExists = DatabaseClient
                .commandExecute("SELECT * FROM accounts WHERE login='" + login
                        + "'");

        try
        {
            if (accountExists.next())
            {
                warnClient(client, "That account already exists!");
            } else
            {
                DatabaseClient
                        .commandExecute("INSERT INTO accounts(id, login, password, mail) VALUES("
                                + Server.getAccountFreeId()
                                + ",'"
                                + login
                                + "','"
                                + Crypt.encrypt(pass)
                                + "','"
                                + mail
                                + "')");
            }

        } catch (Exception e)
        {
            handleError(e);
        }
    }

    public static void CreateCharacter(String name, EntityType type,
            ConnectedClient client)
    {
        Entity e = new Entity(getFreeId(), name, type, 0, 0, 0,
                Server.getStandardSkillsSet(), Server.getStandardStatsSet(),
                getWorld(1));
        client.controlledEntity = e;
        AddCharacterResponse r = new AddCharacterResponse();
        r.name = name;
        SendTo(client.connection, r);
        // entities.put(e.id, e);
        createInventory(e.id);
        saveInventory(inventories.get(e.id));
        saveEntity(e, client.getAccountId());
    }

    private static int getAccountFreeId()
    {
        try
        {
            ResultSet rs = DatabaseClient
                    .commandExecute("SELECT max(id) as id FROM accounts");
            int i = 0;
            if (rs.next())
            {
                i = rs.getInt("id");
            }
            return i + 1;
        } catch (SQLException e)
        {
            handleError(e);
        }
        return -1;
    }

    private static void createInventory(int id)
    {
        Inventory i = new Inventory(id, 20);
        i.AddItem(new Item(getFreeItemId(), id, "Coin", 1, 0, 0, 0,
                EquipType.None, ItemType.Gold, new Attributes(), 1));
        inventories.put(id, i);
    }

    private static void saveInventory(Inventory i)
    {
        ResultSet entityEqRS = DatabaseClient
                .commandExecute("SELECT * FROM inventories WHERE entityId="
                        + i.entityId);
        try
        {
            if (entityEqRS.next())
            {
                DatabaseClient.commandExecute("UPDATE inventories SET max="
                        + i.maxItems + " WHERE entityId=" + i.entityId);

            } else
            {
                DatabaseClient
                        .commandExecute("INSERT INTO inventories(entityId, max) VALUES("
                                + i.entityId + "," + i.maxItems + ")");
            }

            for (Item item : i.items)
            {
                SaveItem(item);
            }
        } catch (SQLException e)
        {
            handleError(e);
        }
    }

    public static void SaveItem(Item item)
    {
        ResultSet itemEqRS = DatabaseClient
                .commandExecute("SELECT * FROM items WHERE entityId="
                        + item.entityId + " AND id=" + item.id);
        try
        {
            if (itemEqRS.next())
            {
                DatabaseClient.commandExecute("UPDATE items SET entityId="
                        + item.entityId + ", worldId=" + item.worldId
                        + ", amount=" + item.amount + ", caption='"
                        + item.caption + "', equipType='" + item.eqType.name()
                        + "', type='" + item.type.name() + "', x="
                        + item.pos.x + ", y=" + item.pos.y + ", z="
                        + item.pos.z + " WHERE id=" + item.id);

            } else
            {
                DatabaseClient
                        .commandExecute("INSERT INTO items(id, worldId, caption, amount, entityId, equipType, type, x, y, z) VALUES("
                                + item.id
                                + ","
                                + item.worldId
                                + ",'"
                                + item.caption
                                + "',"
                                + item.amount
                                + ", "
                                + item.entityId
                                + ",'"
                                + item.eqType.name()
                                + "','"
                                + item.type.name()
                                + "',"
                                + item.pos.x
                                + ", " + item.pos.y + ", " + item.pos.z + ")");
            }
            SaveAttributes(item);
        } catch (SQLException e)
        {
            handleError(e);
        }
    }

    private static void SaveAttributes(Item item)
    {
        ResultSet itemEqRS;
        try
        {
            for (String s : item.attributes.values.keySet())
            {
                itemEqRS = DatabaseClient
                        .commandExecute("SELECT * FROM attributes WHERE itemId="
                                + item.id);

                if (itemEqRS.next())
                {
                    DatabaseClient
                            .commandExecute("UPDATE attributes SET value="
                                    + item.getAttributeValue(s)
                                    + " WHERE name='" + s + "' AND itemId="
                                    + item.id);

                } else
                {
                    DatabaseClient
                            .commandExecute("INSERT INTO attributes(name, itemId, value) VALUES('"
                                    + s
                                    + "',"
                                    + item.id
                                    + ","
                                    + item.getAttributeValue(s) + ")");
                }
            }
        } catch (SQLException e)
        {
            handleError(e);
        }
    }

    private static Stats getStandardStatsSet()
    {
        Stats sts = new Stats();
        sts.put("Hits", new Statistic("Hits", 10, 50, 5, 2));
        sts.put("Strength", new Statistic("Strength", 5, 50, 5, 2));
        sts.put("Dexterity", new Statistic("Dexterity", 5, 50, 5, 2));
        sts.put("Int", new Statistic("Int", 5, 50, 5, 2));
        sts.put("Mana", new Statistic("Mana", 20, 20, 5, 2));
        return sts;
    }

    public static void LoadConfig()
    {
        try
        {

            File configFile = null;
            FileReader fr;
            BufferedReader br;
            FileWriter fw;
            BufferedWriter bw;
            String s;

            configFile = new File("config.cfg");

            if (!configFile.exists())
            {
                configFile.createNewFile();
                fw = new FileWriter(configFile);
                bw = new BufferedWriter(fw);

                bw.write("dbName=theerder\n");
                bw.write("dbuser=root\n");
                bw.write("dbpass=\n");
                bw.write("name=ErderServer\n");
                bw.write("loginHost=127.0.0.1\n");
                bw.write("loginPort=3526\n");
                bw.write("port=25565\n");
                bw.write("version=1.0");

                bw.flush();
                fw.close();
                bw.close();
            }

            fr = new FileReader(configFile);
            br = new BufferedReader(fr);

            while ((s = br.readLine()) != null)
            {
                if (s.split("=").length > 1)
                {
                    Configuration.AddEntry(s.split("=")[0], s.split("=")[1]);
                } else
                {
                    Configuration.AddEntry(s.split("=")[0], "");
                }
            }

            br.close();
            fr.close();
            Main.Log("[CONFIG]", "Config loaded! ");
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static Skills getStandardSkillsSet()
    {
        Skills sks = new Skills();
        sks.put("Swords",
                new Skill("Swords", 0, 50, 5, "Strength", "Dexterity", 2));
        sks.put("Chivalry", new Skill("Chivalry", 0, 50, 5, "Strength", "Int", 2));
        sks.put("Magery", new Skill("Magery", 0, 50, 5, "Int", "Int", 2));
        sks.put("Lumberjacking", new Skill("Lumberjacking", 0, 50, 5,
                "Strength", "Dexterity", 2));
        sks.put("Mining", new Skill("Mining", 0, 50, 5, "Strength", "Int", 2));
        sks.put("Taming", new Skill("Taming", 0, 50, 5, "Int", "Strength", 2));
        sks.put("Necromancy", new Skill("Necromancy", 0, 50, 5, "Int", "Int", 2));
        sks.put("Parrying", new Skill("Parrying", 0, 50, 5, "Dexterity",
                "Strength", 2));
        sks.put("Herding", new Skill("Herding", 0, 50, 5, "Int", "Int", 2));
        sks.put("Carpentry",
                new Skill("Carpentry", 0, 50, 5, "Int", "Strength", 2));

        return sks;
    }

    private static int getFreeId()
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
            handleError(e);
        }
        return -1;
    }

    public static int getFreeItemId()
    {
        try
        {
            ResultSet rs = DatabaseClient
                    .commandExecute("SELECT max(id) as id FROM items");
            int i = 0;
            if (rs.next())
            {
                i = rs.getInt("id");
            }
            return i + 1;
        } catch (SQLException e)
        {
            handleError(e);
        }
        return -1;
    }

    public static void HandleMove(int x, int y, Connection connection)
    {
        ConnectedClient c = getClient(connection);

        try
        {
            Entity e = c.controlledEntity;
            
            if(e.haveGUI("dropdown"))
                e.closeGUI("dropdown");
            
            e.tryMove(x, y);
        } catch (Exception e)
        {
            handleError(e);
        }
    }

    private static void MovePlayerAt(Vector3 vector3, Entity e)
    {
        e.pos = vector3;
        Server.UpdateEntityPosition(e);

    }

   // public static void HandleCast(String spellName, int eId,
   //         Connection connection)
   // {
   //     try
   //     {
   //         ConnectedClient c = getClient(connection);
   //         // c.controlledEntity.tryCast(spellName.toLowerCase(), eId);
   //     } catch (Exception e)
   //     {
   //         handleError(e);
   //     }
   // }

    public static void HandleChat(String msg, Connection connection)
    {
        try
        {
            ConnectedClient c = getClient(connection);
            c.controlledEntity.world.sendAll(msg, c.controlledEntity);
        } catch (Exception e)
        {
            handleError(e);
        }
    }

    public static void EntityDead(final Entity entity, Entity from)
    {
        /*
         * Handling entitys death
         */

        if(isAI(from))
        {
            getAI(from).OnKill(entity);
        }
        
        if(isAI(entity))
        {
            getAI(entity).OnDeath(from);
        }
        
        from.tryStopAttack();

        entity.tryStopAttack();

        Main.Log("[MESSAGE]", entity.caption + " the " + entity.type.name()
                + "(" + entity.id + ") dead!");

        warnEntity(entity, "You're dead! Next time be more careful", 0x01);

        TravelEntity(entity, new Vector3(3, 2, 2));

        if(entity.isAI)
            entity.AI.OnTick();
        
        entity.setRebirthHitsAmount();
        entity.invul = true;
        warnEntity(entity, "You cannot be hurt by anyone", 0x01);

        Timer t = new Timer();
        t.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                entity.invul = false;
                warnEntity(entity, "You can be hurt now", 0x01);
            }
        }, (long) (entity.invulTime * 1000));
    }

    public static void TravelEntity(Entity e, Vector3 vector3)
    {
        MovePlayerAt(vector3, e);
    }

    public static void handleError(Exception e)
    {
        Main.Log("[ERROR]", e.getMessage());
        e.printStackTrace();
    }

    public static Item checkInventory(Entity e, ItemType at)
    {
        for (Item i : inventories.get(e.id).getItems())
        {
            if (i.type == at)
                return i;
        }
        return null;
    }

    public static Inventory getInventory(int id)
    {
        return inventories.get(id);
    }

    public static Inventory getInventory(Entity id)
    {
        return inventories.get(id.id);
    }
    
    public static Entity getEntity(int entityId)
    {
        return entities.get(entityId);
    }

    public static void DestroyItem(Item item)
    {
        DatabaseClient.commandExecute("DELETE FROM items WHERE id = " + item.id
                + " LIMIT 1;");
        DatabaseClient.commandExecute("DELETE FROM attributes WHERE itemId = "
                + item.id + ";");
    }

    public static void DestroyItem(Inventory i, Item item)
    {
        i.RemoveItem(item.id);
        DestroyItem(item);
    }

    public static boolean haveItemSet(Entity e, ArrayList<String> reagentsNeeded)
    {
        Inventory i = getInventory(e);
        if (i != null)
        {
            for (String s : reagentsNeeded)
            {
                if (haveItem(e, s))
                {
                    continue;
                } else
                {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean haveItem(Entity e, String s)
    {
        Inventory i = getInventory(e);
        if (i != null)
        {
            for (Item item : i.items)
            {
                if (item.caption.equals(s))
                    return true;
            }
        }
        return false;
    }

    public static void consumeItem(Entity entity, String s)
    {
        Inventory i = getInventory(entity);
        if (i != null)
        {
            Item item = i.getItem(s);
            i.consume(item);
        }
    }

    public static void warnClient(ConnectedClient client, String string)
    {
        warnClient(client, string, 0);
    }

    public static void warnClient(ConnectedClient client, String string,
            int type)
    {
        MessageResponse r = new MessageResponse();
        r.msg = string;
        r.type = type;
        SendTo(client.connection, r);
    }

    public static ActionType getActionFromString(String string)
    {
        for (ActionType aT : ActionType.values())
        {
            if (aT.name().toLowerCase().contains(string))
            {
                return aT;
            }
        }
        return ActionType.None;
    }

    public static float getPlantGrowTime(String seed)
    {
        try
        {
            return plantsGrowTime.get(seed);
        } catch (Exception e)
        {
            handleError(e);
            return 10;
        }
    }

    public static void DestroyFlag(int id, String string)
    {
        DatabaseClient
                .commandExecute("DELETE FROM locationflags WHERE locationId = "
                        + id + " AND flag='" + string + "' LIMIT 1;");
    }

    public static void HandleCraft(String string, int i, Connection c)
    {
        CraftSystem.tryCraft(string, i, getClient(c).controlledEntity);
    }

    public static void Save()
    {

    }

    public static void Stop()
    {
        Server.server.stop();
    }

    public static void HandleCharacterChoose(String nick, Connection connection)
    {
        try
        {
            ConnectedClient c = getClient(connection);
            ResultSet charInfo = DatabaseClient
                    .commandExecute("SELECT id FROM entities WHERE accountId="
                            + c.getAccountId() + " AND caption='" + nick + "'");

            if (charInfo.next())
            {
                LoadPlayer(charInfo.getInt("id"), c);
            } else
            {
                MessageResponse r = new MessageResponse();
                r.msg = "There's no character with given name";
                SendTo(connection, r);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void HandleCharacterCreate(String nick, EntityType type,
            Connection connection)
    {
        try
        {
            ResultSet charExist = DatabaseClient
                    .commandExecute("SELECT * FROM entities WHERE caption='"
                            + nick + "';");
            if (charExist.next())
            {
                warnConnection(connection, "That name already taken!");
            } else
            {
                CreateCharacter(nick, type, getClient(connection));
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    private static void warnConnection(Connection connection, String string)
    {
        warnConnection(connection, string, 0);
    }

    private static void warnConnection(Connection connection, String string,
            int type)
    {
        MessageResponse r = new MessageResponse();
        r.msg = string;
        r.type = type;
        SendTo(connection, r);
    }

    public static void HandleCharacterRemove(String nick, Connection connection)
    {
        try
        {
            ResultSet charExist = DatabaseClient
                    .commandExecute("SELECT * FROM entities WHERE caption='"
                            + nick + "';");
            if (charExist.next())
            {
                DeleteCharacter(nick, getClient(connection).getAccountId());
                warnConnection(connection, "Character " + nick
                        + " succesfully removed!");
            } else
            {
                warnConnection(connection, "You don't have such player!");
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    private static void DeleteCharacter(String nick, int id)
    {
        DatabaseClient.commandExecute("DELETE FROM entities WHERE accountId="
                + id + " AND caption='" + nick + "';");
    }

    public static void UpdateEntityPosition(Entity entity)
    {    
        entity.world.UpdateNear(entity);

        // Main.Log("[DEBUG]", "Update entity position");
        ConnectedClient c;
        
        UpdatePlayerResponse r = new UpdatePlayerResponse();
        r.id = entity.id;
        r.updType = UpdateType.Position;
        r.x = (int) entity.pos.x;
        r.y = (int) entity.pos.y;
        r.z = (int) entity.pos.z;
        if(getClient(entity) != null)
        SendTo(getClient(entity).connection, r);
        
        for (Entity ent : entity.entitiesAround)
        {
            c = getClient(ent);

            if (c != null)
               SendTo(c.connection, r);
        }
    }

    public static void HandleTarget(int id, Entity e1)
    {
        try
        {
            Entity e2 = Server.getEntity(id);
            if (e1 != null && e2 != null)
            {
                e1.actToTarget(e2);
            }
        } catch (Exception e)
        {
            handleError(e);
        }
    }

    public static void sendDropdownFor(AI ai, ConnectedClient c, float x,
            float y)
    {
        ai.OnDropdownRequest(c.controlledEntity);
    }

    public static void sendSpeech(Entity entity, String msg)
    {
        SpeechResponse r = new SpeechResponse();

        for (Entity e : entity.world.entities)
        {
            r.msg = msg;
            r.id = entity.id;
            if(!e.isAI)
            SendTo(getClient(e).connection, r);
        }
    }

    public static void SaveAI(BaseAI baseAI)
    {
        try
        {
            ResultSet ai = DatabaseClient
                    .commandExecute("SELECT * From ai WHERE entityId="
                            + baseAI.getEntity().id);

            if (ai.next())
            {
                DatabaseClient.commandExecute("UPDATE ai SET reaction="
                        + baseAI.getReactionTime() + " WHERE entityId="
                        + baseAI.getEntity().id);
            } else
            {
                DatabaseClient
                        .commandExecute("INSERT INTO ai(entityId, class, reaction) VALUES("
                                + baseAI.getEntity().id
                                + ",'"
                                + baseAI.getClassPath()
                                + "',"
                                + baseAI.getReactionTime() + ")");
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static boolean isAI(Entity e)
    {
        if(ais.get(e.id) != null)
        return true;
        else
            return false;
    }

    public static AI getAI(Entity e)
    {
        return ais.get(e.id);
    }

    public static Equip getEquip(Entity controlledEntity)
    {
        return equips.get(controlledEntity.id);
    }
    
    public static Equip getEquip(int id)
    {
        return equips.get(id);
    }
    
    public static void unloadInventory(int id)
    {
        Inventory inv = getInventory(id);
        if(inv != null)
        {
            for(Item i: inv.items)
            {
                Server.SaveItem(i);
                Server.items.remove(i);
            }
        }
    }

    public static void unloadEquip(int id)
    {
        Equip eq = getEquip(id);
        int itemId = -1;
        if(eq != null)
        {
            for(String slotName: eq.contents.keySet())
            {
                if(eq.contents.get(slotName).item == null)
                    itemId = -1;
                else
                    itemId = eq.contents.get(slotName).item.id;
                
                eq.SaveSlot(id, slotName, itemId);
            }
        }        
    }

    public static void ProcessLogin(ProcessLoginResponse object)
    {

            for(ConnectedClient client: clients.values())
            {
                if(client.awaitedLogin.equals(object.login))
                {
                    if(object.allow)
                    {
                        Main.Log("[DEBUG]", "Auth allowed");
                        LoginResponse r = new LoginResponse();
                        r.succesful = true;
                        SendTo(client, r);

                        if (!client.logged)
                        {
                            client.account = new Account(object.id,
                                    object.login, object.pass,
                                    object.mail);
                            client.logged = true;
                            SendCharacters(client);
                        } else
                        {
                            MessageResponse r1 = new MessageResponse();
                            r1.msg = "This account is already logged in!";
                            SendTo(client, r1);
                        }
                        break;
                    }
                    else
                    {
                        LoginResponse r = new LoginResponse();
                        r.succesful = false;
                        SendTo(client, r);
                        break;
                    }
                }
            }
        
    }

    private static void SendTo(ConnectedClient client, Object r)
    {
        client.connection.sendUDP(r);
    }
}
