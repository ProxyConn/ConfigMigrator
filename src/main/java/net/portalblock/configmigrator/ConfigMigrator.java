package net.portalblock.configmigrator;

import net.portalblock.configmigrator.options.OptionParser;
import net.portalblock.configmigrator.options.OptionSet;

import java.io.File;
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

        for(Map.Entry<String, ServerDefinition> entry : conf.getServers().entrySet()){
            System.out.println(entry.getValue().getName());
            System.out.println(entry.getValue().getMotd());
            System.out.println(entry.getValue().getAddress().getHostString() + ":" + entry.getValue().getAddress().getPort());
        }

    }

}
