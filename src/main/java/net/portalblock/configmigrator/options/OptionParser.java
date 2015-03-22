package net.portalblock.configmigrator.options;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by portalBlock on 3/22/2015.
 */
public class OptionParser {

    private HashMap<String, String[]> accepts = new HashMap<String, String[]>();
    private boolean acceptOther = false;
    private HashMap<String, String[]> depends = new HashMap<String, String[]>();

    public OptionParser depends(String value, String... dependsOn){
        depends.put(value, dependsOn);
        return this;
    }

    public OptionParser accepts(String rootKey, String... aliases){
        accepts.put(rootKey, aliases);
        return this;
    }

    public OptionParser acceptOther(boolean acceptOther){
        this.acceptOther = acceptOther;
        return this;
    }

    public OptionSet parse(String[] args){
        HashMap<String, String> values = new HashMap<String, String>();
        for(int i = 0; i < args.length; i++){
            String key, value = null;
            key = args[i];
            if(!isKey(key)) continue;
            if(i != args.length-1) {
                value = args[i + 1];
            }
            key = getForAlias(key.substring(1, key.length()));
            values.put(key, value);
        }
        return new OptionSet(values);
    }

    private boolean isKey(String s){
        if(!s.startsWith("-") || !acceptKey(s)) return false;
        return true;
    }

    private boolean acceptKey(String key){
        if(acceptOther) return true;
        if(key.startsWith("-")) key = key.substring(1, key.length());
        if(accepts.containsKey(key)) return true;
        for(Map.Entry<String, String[]> entry : accepts.entrySet())
            if(Arrays.asList(entry.getValue()).contains(key)) return true;
        return false;
    }

    private String getForAlias(String key){
        if(accepts.containsKey(key)) return key;
        for(Map.Entry<String, String[]> entry : accepts.entrySet())
            if(Arrays.asList(entry.getValue()).contains(key)) return entry.getKey();
        return null;
    }

}
