package io.bit3.mgpm.cli;

import org.slf4j.LoggerFactory;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;



public class AnsiOutput {
  private static final Logger LOGGER = LoggerFactory.getLogger(AnsiOutput.class);
  private static final char ESCAPE = '\u001b';
  private static final boolean DECORATED;
  private static final AnsiOutput instance;
  private static final String[] spinnerCharacters = new String[]{"|", "/", "-", "\\"};

  static {
    DECORATED = true;
    instance = new AnsiOutput();
  }

  private Map<String, String> activeWorkers = new LinkedHashMap<>();
  private int spinnerIndex = 0;
  private int writtenLines = 0;

  private AnsiOutput() {
  }

  public static AnsiOutput getInstance() {
    return instance;
  }

  public AnsiOutput println() {
    LOGGER.info("");
    return this;
  }

  public AnsiOutput println(String msg, Object... arguments) {
    print(msg, arguments);
    LOGGER.info("");
    return this;
  }

  public AnsiOutput println(Color foregroundColor, String msg, Object... arguments) {
    print(foregroundColor, msg, arguments);
    LOGGER.info("");
    return this;
  }

  public AnsiOutput print(String msg,Object... arguments) {
    if (LOGGER.isInfoEnabled()){
      LOGGER.info(String.format(msg, arguments));
    }
    return this;
  }

  public AnsiOutput print(Color foregroundColor, String msg, Object... arguments) {
    color(foregroundColor.foregroundCode, foregroundColor.foregroundIntensity);
    print(msg, arguments);
    reset();
    return this;
  }

  public AnsiOutput print(Color foregroundColor, Color backgroundColor, String msg, Object... arguments) {
    color(foregroundColor.foregroundCode, foregroundColor.foregroundIntensity);
    color(foregroundColor.backgroundCode, backgroundColor.backgroundIntensity);
    print(msg, arguments);
    reset();
    return this;
  }

  public AnsiOutput print(int integer) {
    if(LOGGER.isInfoEnabled()){
      LOGGER.info(String.valueOf(integer));
    }
    return this;
  }

  public AnsiOutput print(Color foregroundColor, int integer) {
    color(foregroundColor.foregroundCode, foregroundColor.foregroundIntensity);
    print(integer);
    reset();
    return this;
  }

  public AnsiOutput print(Color foregroundColor, Color backgroundColor, int integer) {
    color(foregroundColor.foregroundCode, foregroundColor.foregroundIntensity);
    color(backgroundColor.backgroundCode, backgroundColor.backgroundIntensity);
    print(integer);
    reset();
    return this;
  }

  public synchronized void addActiveWorker(String label, String activity) {
    activeWorkers.put(label, activity);
  }

  public synchronized void removeActiveWorker(String label) {
    activeWorkers.remove(label);
  }

  public synchronized AnsiOutput rotateSpinner() {
    deleteSpinner();

    if (!DECORATED || activeWorkers.isEmpty()) {
      return this;
    }

    int localSpinnerIndex = spinnerIndex;
    for (Map.Entry<String, String> entry : activeWorkers.entrySet()) {
      String label = entry.getKey();
      String activity = entry.getValue();
      if(LOGGER.isInfoEnabled()){
        LOGGER.info(String.format(" (%s) %s: %s", spinnerCharacters[localSpinnerIndex], label, activity));
      }
      localSpinnerIndex = (localSpinnerIndex + 1) % spinnerCharacters.length;
      writtenLines++;
    }

    spinnerIndex = (spinnerIndex + 1) % spinnerCharacters.length;

    return this;
  }

  public synchronized AnsiOutput deleteSpinner() {
    if (!DECORATED || 0 == writtenLines) {
      return this;
    }

    // Se podría limpiar la salida del logger aquí, pero depende de cómo quieras manejarlo.
    // No hay una forma directa de borrar líneas ya registradas en un logger.

    writtenLines = 0;
    return this;
  }

  private void color(int code, int intensity) {
    if (!DECORATED) {
      return;
    }
    else if(LOGGER.isInfoEnabled()){
    LOGGER.info(String.format("%c[%d;%dm", ESCAPE, intensity, code));
    }
  }

  private void reset() {
    if (!DECORATED) {
      return;
    }
    else if(LOGGER.isInfoEnabled()){
      LOGGER.info(String.format("%c[0m", ESCAPE));
    }
  }
}
