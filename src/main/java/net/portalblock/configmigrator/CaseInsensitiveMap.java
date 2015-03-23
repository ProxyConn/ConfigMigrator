package net.portalblock.configmigrator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by portalBlock on 3/23/2015.
 */
public class CaseInsensitiveMap<V> extends HashMap<String, V> {

    public CaseInsensitiveMap(){}

    public CaseInsensitiveMap(Map<String, V> existing){
        for(Map.Entry<String, V> entry : existing.entrySet()){
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public V put(String key, V value) {
        return super.put(key.toLowerCase(), value);
    }

    public V get(String key) {
        return super.get(key.toLowerCase());
    }
}
