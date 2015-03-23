package net.portalblock.configmigrator;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.InetSocketAddress;

/**
 * Created by portalBlock on 3/23/2015
 */
@AllArgsConstructor
public class ServerDefinition {

    @Getter
    private String name, motd;

    @Getter
    private InetSocketAddress address;

}
