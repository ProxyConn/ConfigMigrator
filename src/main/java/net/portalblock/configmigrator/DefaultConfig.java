package net.portalblock.configmigrator;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by portalBlock on 4/1/2015.
 */
public class DefaultConfig {

    private static JSONObject cfg = new JSONObject();

    static {
        cfg.put("password", "default");
        cfg.put("bindTo", "127.0.0.1:9090");
        cfg.put("motd", "&3ProxyConn by portalBlock");
        cfg.put("maintMotd", "&4Undergoing some work, be back soon :D");
        cfg.put("prefix", "&7[&cProxyConn2.0&7]");

        JSONArray staff = new JSONArray();
        staff.put("portalBlock");
        cfg.put("staff", staff);

        JSONArray servers = new JSONArray();

        JSONObject server = new JSONObject();
        server.put("name", "Lobby");
        server.put("ip", "127.0.0.1:25566");
        server.put("isLobby", true);
        servers.put(server);
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

        cfg.put("sample", sample);

    }

    public static JSONObject getDefault() {
        return cfg;
    }
}
