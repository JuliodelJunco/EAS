package io.bit3.mgpm;

import io.bit3.mgpm.cli.AnsiOutput;
import io.bit3.mgpm.cli.CliApplication;
import io.bit3.mgpm.cmd.Args;
import io.bit3.mgpm.cmd.ArgsLoader;
import io.bit3.mgpm.cmd.LogLevel;
import io.bit3.mgpm.config.Config;
import io.bit3.mgpm.config.ConfigLoader;
import io.bit3.mgpm.gui.GuiApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileNotFoundException;



public class InterruptedNewException extends RuntimeException {
  public MyParseException(String message) {
    super(message);
  }
}
public class App {
  private static final Logger logger = LoggerFactory.getLogger(App.class);
  private final ConfigLoader loader;
  private final Args args;
  private final Config config = new Config();

  public App(ConfigLoader loader, Args args) throws FileNotFoundException, InterruptedException {
    this.loader = loader;
    this.args = args;

    this.init();
  }

  public static void main(String[] cliArguments) throws InterruptedException {
    System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", LogLevel.TRACE.toString().toLowerCase());

    ArgsLoader argsLoader = new ArgsLoader();
    Args args = argsLoader.load(cliArguments);

    if (null == args) {
      return;
    }

    System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", args.getLoggerLevel().toString().toLowerCase());
    System.setProperty("org.slf4j.simpleLogger.log.io.bit3.mgpm", args.getLoggerLevel().toString().toLowerCase());

    ConfigLoader configLoader = new ConfigLoader();
    try {
      App app = new App(configLoader, args);

      if (args.isShowGui()) {
        app.runGui();
      } else {
        app.runCli();
      }
    } catch (FileNotFoundException e) {
      logger.error(e.getMessage()); // Utilizando el Logger para errores de archivo no encontrado
    }
  }

  public void init() throws FileNotFoundException, InterruptedException {
    AnsiOutput output = AnsiOutput.getInstance();
    output.addActiveWorker("MGPM", "loading configuration");
    SpinnerRotator rotator = new SpinnerRotator(output);
    rotator.start();

    try {
      if (args.hasConfig()) {
        loader.load(config, args.getConfig());
      } else {
        loader.load(config);
      }
    } finally {
      rotator.finish();
      output.removeActiveWorker("MGPM");
    }
  }

  public void runGui() {
    GuiApplication guiApplication = new GuiApplication(args, config);
    guiApplication.run();
  }

  public void runCli() throws InterruptedException {
    CliApplication cliApplication = new CliApplication(args, config);
    cliApplication.run();
  }

  private class SpinnerRotator extends Thread {
    private final AnsiOutput output;
    private boolean running = true;

    private SpinnerRotator(AnsiOutput output) {
      this.output = output;
      setDaemon(true);
    }

    @Override
    public void run() {
      while (running) {
        output.rotateSpinner();
          try {
              Thread.sleep(200);
          } catch (InterruptedException e) {
              throw new InterruptedNewException("Error wtih the time wait limit, process was interupted",e);
          }
      }
    }

    public void finish() throws InterruptedException {
      running = false;

      while (isAlive()) {
          Thread.sleep(50);
      }

      output.deleteSpinner();
    }
  }
}
