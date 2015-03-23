/*
Copyright (c) 2012, md_5. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

Redistributions of source code must retain the above copyright notice, this
list of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

The name of the author may not be used to endorse or promote products derived
from this software without specific prior written permission.

You may not use the software for commercial software hosting services without
written permission from the author.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
 */
package net.portalblock.configmigrator;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;

/**
 * Credit to md_5 for this code, license for this file is above.
 * This class was copied from Bungeecord with modifications to remove Bungeecord dependencies.
 * All changes have been marked.
 */
public class YamlConfig {

    /*
    List of major changes:
        Removed DefaultTabList enum
        Removed all annotations
        Removed all comments
        Removed save method/calls
        Altered getListeners method
     */

    private final Yaml yaml;
    private Map config;
    private final File file;

    //Added the File to the constructor
    public YamlConfig(File input)
    {
        this.file = input;
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle( DumperOptions.FlowStyle.BLOCK );
        yaml = new Yaml( options );
    }

    public void load()
    {
        try
        {
            file.createNewFile();

            try ( InputStream is = new FileInputStream( file ) )
            {
                config = (Map) yaml.load( is );
            }

            if ( config == null )
            {
                config = new CaseInsensitiveMap();
            } else
            {
                config = new CaseInsensitiveMap( config );
            }
        } catch ( IOException ex )
        {
            throw new RuntimeException( "Could not load configuration!", ex );
        }

        Map<String, Object> permissions = get( "permissions", new HashMap<String, Object>() );
        if ( permissions.isEmpty() )
        {
            permissions.put( "default", Arrays.asList(new String[]
                    {
                            "bungeecord.command.server", "bungeecord.command.list"
                    }) );
            permissions.put( "admin", Arrays.asList(new String[]
                    {
                            "bungeecord.command.alert", "bungeecord.command.end", "bungeecord.command.ip", "bungeecord.command.reload"
                    }) );
        }

        Map<String, Object> groups = get( "groups", new HashMap<String, Object>() );
        if ( groups.isEmpty() )
        {
            groups.put( "md_5", Collections.singletonList("admin") );
        }
    }

    private <T> T get(String path, T def)
    {
        return get( path, def, config );
    }


    private <T> T get(String path, T def, Map submap)
    {
        int index = path.indexOf( '.' );
        if ( index == -1 )
        {
            Object val = submap.get( path );
            if ( val == null && def != null )
            {
                val = def;
                submap.put(path, def);
            }
            return (T) val;
        } else
        {
            String first = path.substring( 0, index );
            String second = path.substring( index + 1, path.length() );
            Map sub = (Map) submap.get( first );
            if ( sub == null )
            {
                sub = new LinkedHashMap();
                submap.put( first, sub );
            }
            return get( second, def, sub );
        }
    }

    public int getInt(String path, int def)
    {
        return get( path, def );
    }

    public String getString(String path, String def)
    {
        return get( path, def );
    }

    public boolean getBoolean(String path, boolean def)
    {
        return get( path, def );
    }

    public Map<String, ServerDefinition> getServers()
    {
        Map<String, Map<String, Object>> base = get( "servers", (Map) Collections.singletonMap("lobby", new HashMap<>()) );
        Map<String, ServerDefinition> ret = new HashMap<>();

        for ( Map.Entry<String, Map<String, Object>> entry : base.entrySet() )
        {
            Map<String, Object> val = entry.getValue();
            String name = entry.getKey();
            String addr = get("address", "localhost:25565", val);
            //Removed chat color formatting
            String motd = get("motd", "&1Just another BungeeCord - Forced Host", val);

            //Removed restricted boolean

            //Changed method call
            InetSocketAddress address = getAddressFromString(addr);
            /*
            Use ServerDefinition rather then ServerInfo here because its more minimal.
            Change the way the object is created.
             */
            ServerDefinition info = new ServerDefinition(name, motd, address);
            ret.put(name, info);
        }
        return ret;
    }

    //Added method
    private InetSocketAddress getAddressFromString(String addr) {
        String[] val = addr.split(":");
        int def = 25565;
        if (val.length > 1) {
            def = Integer.parseInt(val[1]);
        }
        return new InetSocketAddress(val[0], def);
    }

    //Changed ListenerInfo to OtherInfo
    public Collection<OtherInfo> getListeners()
    {
        Collection<Map<String, Object>> base = get( "listeners", (Collection) Arrays.asList( new Map[]
                {
                        new HashMap()
                } ) );

        //Removed forced host related

        Collection<OtherInfo> ret = new HashSet<>();

        for ( Map<String, Object> val : base )
        {
            String motd = get( "motd", "&1Another Bungee server", val );
            //Removed ChatColor formatting

            int maxPlayers = get( "max_players", 1, val );

            //Removed default and fallback/force default options

            String host = get("host", "0.0.0.0:25577", val);

            //Removed tab list size

            //Changed method call and renamed from 'address' to 'bind'
            InetSocketAddress bind = getAddressFromString(host);

            //Removed forced hosts

            //Removed tab list related things

            //Removed bind local address boolean

            //Removed ping passthrough boolean

            //Removed query related things

            //Changed object creation
            OtherInfo info = new OtherInfo(motd, maxPlayers, bind);
            ret.add( info );
        }

        return ret;
    }

    public Collection<String> getGroups(String player)
    {
        Collection<String> groups = get( "groups." + player, null );
        Collection<String> ret = ( groups == null ) ? new HashSet<String>() : new HashSet<>( groups );
        ret.add( "default" );
        return ret;
    }

    public Collection<?> getList(String path, Collection<?> def)
    {
        return get( path, def );
    }

    public Collection<String> getPermissions(String group)
    {
        Collection<String> permissions = get( "permissions." + group, null );
        return ( permissions == null ) ? Collections.EMPTY_SET : permissions;
    }
}
