package net.portalblock.configmigrator;

import net.portalblock.configmigrator.options.OptionParser;
import net.portalblock.configmigrator.options.OptionSet;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;

/**
 * Created by portalBlock on 3/22/2015.
 */
public class ConfigMigrator {

    private static final String
            HELP = "help",
            INPUT = "input",
            OUTPUT = "output";

    public static void main(String[] args){

        OptionParser parser = new OptionParser()
                .acceptOther(false)
                .accepts(HELP, "h")
                .accepts(INPUT, "i", "in")
                .accepts(OUTPUT, "o", "out");
        OptionSet options = parser.parse(args);

        if(options.hasOption(HELP) || (!options.hasArgument(INPUT) || !options.hasArgument(OUTPUT)) || !options.hasOptions()){
            System.out.println("ConfigMigrator.jar -input <file name> -output <file name>");
            System.exit(0);
        }

        File input = new File(options.getArgument(INPUT));
        File output = new File(options.getArgument(OUTPUT));

        YamlConfig conf = new YamlConfig(input);

        conf.load();

        JSONObject cfg = new JSONObject();

        OtherInfo defaultListener = conf.getListeners().toArray(new OtherInfo[conf.getListeners().size()])[0];

        cfg.put("password", "default");
        InetSocketAddress addr = defaultListener.getBind();
        cfg.put("bindTo", addr.getHostString() + ":" + addr.getPort());
        cfg.put("motd", defaultListener.getMotd());
        cfg.put("maintMotd", "&4Undergoing some work, be back soon :D");
        cfg.put("prefix", "&7[&cProxyConn2.0&7]");

        JSONArray staff = new JSONArray();
        staff.put("portalBlock");
        cfg.put("staff", staff);

        JSONArray servers = new JSONArray();
        for(Map.Entry<String, ServerDefinition> entry : conf.getServers().entrySet()){
            JSONObject server = new JSONObject();
            server.put("name", entry.getKey());
            server.put("ip", entry.getValue().getAddress().getHostString() + ":" + entry.getValue().getAddress().getPort());
            server.put("isLobby", false);

            servers.put(server);
        }
        cfg.put("servers", servers);

        JSONArray sample = new JSONArray();
        String[] defSample = {
                "&7»»»»»»»»»»»»»»»[&cProxyConn&7]«««««««««««««««",
                "&7~ Fast",
                "&c~ Reliable",
                "&7~ Easy to use",
                "&c~ Tons of features"};
        for(String s : defSample)
            sample.put(s);

        try{
            FileWriter writer = new FileWriter(output);
            writer.write(cfg.toString(2));
            writer.flush();
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
