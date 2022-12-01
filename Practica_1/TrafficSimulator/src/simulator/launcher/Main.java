package simulator.launcher;

import java.io.*;
import java.io.IOException;
import java.util.*;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import simulator.control.Controller;
import simulator.factories.*;
import simulator.model.*;

public class Main {

	private final static Integer _timeLimitDefaultValue = 10;
        private static Integer _timeLimitValue = _timeLimitDefaultValue;
	private static String _inFile = null;
	private static String _outFile = null;
	private static Factory<Event> _eventsFactory = null;

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseHelpOption(line, cmdLineOptions);
			parseInFileOption(line);
                        parseTicksOption(line);
			parseOutFileOption(line);

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Events input file").build());
                cmdLineOptions.addOption(Option.builder("t").longOpt("ticks").hasArg().desc("Ticks to the simulator’s main loop (default value is 10).").build());
		cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg().desc("Output file, where reports are written.").build());
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message").build());

		return cmdLineOptions;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		if (_inFile == null) {
			throw new ParseException("An events file is missing");
		}
	}

	private static void parseOutFileOption(CommandLine line) throws ParseException {
		_outFile = line.getOptionValue("o");
	}
        
        private static void parseTicksOption(CommandLine line) throws ParseException {
                String value=line.getOptionValue("t");
                if (value == null) {
			_timeLimitValue=_timeLimitDefaultValue;
		}
                else{
                        _timeLimitValue=Integer.parseInt(value);
                }
	}

	private static void initFactories() {
            List<Builder<LightSwitchingStrategy>> lsbs = new ArrayList<>();
            lsbs.add( new RoundRobinStrategyBuilder() );
            lsbs.add( new MostCrowdedStrategyBuilder() );
            Factory<LightSwitchingStrategy> lssFactory = new BuilderBasedFactory<>(lsbs);
            List<Builder<DequeuingStrategy>> dqbs = new ArrayList<>();
            dqbs.add( new MoveFirstStrategyBuilder() );
            dqbs.add( new MoveAllStrategyBuilder() );
            Factory<DequeuingStrategy> dqsFactory = new BuilderBasedFactory<>(dqbs);
            List<Builder<Event>> ebs = new ArrayList<>();
            ebs.add( new NewJunctionEventBuilder(lssFactory,dqsFactory) );
            ebs.add( new NewCityRoadEventBuilder() );
            ebs.add( new NewInterCityRoadEventBuilder() );
            ebs.add( new NewVehicleEventBuilder() );
            ebs.add( new SetContClassEventBuilder() );
            ebs.add( new SetWeatherEventBuilder() );
            _eventsFactory = new BuilderBasedFactory<>(ebs);
        }

	private static void startBatchMode() throws IOException {
            Controller c=new Controller(new TrafficSimulator(),_eventsFactory);
            c.loadEvents(new FileInputStream(_inFile));
            OutputStream os;
            if(_outFile!=null) os=new FileOutputStream(_outFile);
            else os=null;
            c.run(_timeLimitValue, os);
	}

	private static void start(String[] args) throws IOException {
		initFactories();
		parseArgs(args);
		startBatchMode();
	}

	// example command lines:
	//
	// -i resources/examples/ex1.json
	// -i resources/examples/ex1.json -t 300
	// -i resources/examples/ex1.json -o resources/tmp/ex1.out.json
	// --help

	public static void main(String[] args) {
		try {
			start(args);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
