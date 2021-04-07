package dev.javabuilder;

import java.io.PrintStream;
import org.code.javabuilder.OutputAdapter;

/** Intended for local testing only. Passes output to the provided PrintStream */
public class LocalOutputAdapter implements OutputAdapter {
  private final PrintStream outputStream;

  public LocalOutputAdapter(PrintStream outputStream) {
    this.outputStream = outputStream;
  }

  @Override
  public void sendMessage(String message) {
    outputStream.print(message);
  }
}
