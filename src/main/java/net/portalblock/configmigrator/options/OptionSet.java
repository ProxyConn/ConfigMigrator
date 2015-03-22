package net.portalblock.configmigrator.options;

import java.util.HashMap;

/**
 * Created by portalBlock on 3/22/2015.
 */
public class OptionSet {

    private HashMap<String, String> options;
    
    OptionSet(HashMap<String, String> options){
        this.options = options;
    }

    public boolean hasOption(String option){
        return options.containsKey(option);
    }

    public boolean hasArgument(String option){
        return (options.get(option) != null);
    }

    public boolean hasOptions(){
        return (options != null && options.size() > 0);
    }

    public String getArgument(String option){
        return options.get(option);
    }

}
