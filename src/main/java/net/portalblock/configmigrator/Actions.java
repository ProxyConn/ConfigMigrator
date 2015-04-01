package net.portalblock.configmigrator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;

/**
 * Created by portalBlock on 4/1/2015.
 */
public class Actions {

    public static void translate(File input, File output) {
        YamlConfig conf = new YamlConfig(input);

        conf.load();

        JSONObject cfg = DefaultConfig.getDefault();

        OtherInfo defaultListener = conf.getListeners().toArray(new OtherInfo[conf.getListeners().size()])[0];

        InetSocketAddress addr = defaultListener.getBind();
        cfg.put("bindTo", addr.getHostString() + ":" + addr.getPort());
        cfg.put("motd", defaultListener.getMotd());

        JSONArray servers = cfg.getJSONArray("servers");
        for (Map.Entry<String, ServerDefinition> entry : conf.getServers().entrySet()) {
            JSONObject server = new JSONObject();
            server.put("name", entry.getKey());
            server.put("ip", entry.getValue().getAddress().getHostString() + ":" + entry.getValue().getAddress().getPort());
            server.put("isLobby", false);

            servers.put(server);
        }
        cfg.put("servers", servers);

        writeToFile(cfg.toString(2), output);
    }

    public static void generate(GenerateSide side, File output){
        switch (side){
            case CONTROLLER:
                writeToFile(DefaultConfig.getDefault().toString(2), output);
                break;
            case CLIENT:
                JSONObject cfg = new JSONObject();
                cfg.put("proxyName", "ProxyConnDefault");
                cfg.put("password", "default");
                cfg.put("controllerAddress", "127.0.0.1:9090");
                cfg.put("pingServers", true);
                writeToFile(cfg.toString(2), output);
                break;
        }
    }


    private static void writeToFile(String text, File output){
        try {
            FileWriter writer = new FileWriter(output);
            writer.write(text);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public enum GenerateSide{
        CLIENT,
        CONTROLLER
    }

}
