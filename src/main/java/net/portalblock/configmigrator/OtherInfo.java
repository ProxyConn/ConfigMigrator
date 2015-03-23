package net.portalblock.configmigrator;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.InetSocketAddress;

/**
 * Created by portalBlock on 3/23/2015.
 */
@AllArgsConstructor
public class OtherInfo {

    @Getter
    private String motd;

    @Getter
    private int maxPlayers;

    @Getter
    private InetSocketAddress bind;

}
