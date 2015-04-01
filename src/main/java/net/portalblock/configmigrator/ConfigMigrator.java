package net.portalblock.configmigrator;

import net.portalblock.configmigrator.options.OptionParser;
import net.portalblock.configmigrator.options.OptionSet;

import java.io.File;

/**
 * Created by portalBlock on 3/22/2015.
 */
public class ConfigMigrator {

    private static final String
            HELP = "help",
            INPUT = "input",
            OUTPUT = "output",
            GENERATE = "generate";

    public static void main(String[] args){

        OptionParser parser = new OptionParser()
                .acceptOther(false)
                .accepts(HELP, "h")
                .accepts(INPUT, "i", "in")
                .accepts(OUTPUT, "o", "out")
                .accepts(GENERATE, "g", "gen");
        OptionSet options = parser.parse(args);

        if(options.hasOption(HELP) || !options.hasOptions())
            printHelp();

        if((options.hasArgument(INPUT) || options.hasArgument(GENERATE)) && !options.hasArgument(OUTPUT))
            printHelp();

        if((options.hasOption(INPUT) && !options.hasArgument(INPUT)) || (options.hasOption(OUTPUT) && !options.hasArgument(OUTPUT))
                || (options.hasOption(GENERATE) && !options.hasArgument(GENERATE)))
            printHelp();

        File output = new File(options.getArgument(OUTPUT));

        if(options.hasArgument(INPUT)){
            File input = new File(options.getArgument(INPUT));
            Actions.translate(input, output);
        }
        else if(options.hasArgument(GENERATE))
            Actions.generate(Actions.GenerateSide.valueOf(options.getArgument(GENERATE).toUpperCase()), output);

    }


    private static void printHelp(){
        System.out.println("ConfigMigrator.jar [-generate <client|controller> | -input <file name>] -output <file name>");
        System.exit(0);
    }

}
