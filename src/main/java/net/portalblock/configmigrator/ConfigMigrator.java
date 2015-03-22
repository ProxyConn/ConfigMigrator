package net.portalblock.configmigrator;

import net.portalblock.configmigrator.options.OptionParser;
import net.portalblock.configmigrator.options.OptionSet;

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

        if(options.hasOption(HELP) || options.hasOptions()){
            System.out.println("ConfigMigrator.jar -input <file name> -output <file name>");
            System.exit(0);
        }
    }

}
