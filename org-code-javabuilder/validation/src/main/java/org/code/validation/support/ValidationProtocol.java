package org.code.validation.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.code.validation.NeighborhoodLog;

public class ValidationProtocol {
  private static ValidationProtocol instance;
  private Method mainMethod;

  // TODO: also initialize with NeighborhoodTracker once it exists
  public static void create(Method mainMethod) {
    instance = new ValidationProtocol(mainMethod);
  }

  public static ValidationProtocol getInstance() {
    return instance;
  }

  protected ValidationProtocol(Method mainMethod) {
    this.mainMethod = mainMethod;
  }

  public NeighborhoodLog getNeighborhoodLog() {
    // TODO: return neighborhood log generated by NeighborhoodTracker
    return null;
  }

  public void invokeMainMethod() {
    try {
      this.mainMethod.invoke(null, new Object[] {null});
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new ValidationRuntimeException(ExceptionKey.ERROR_RUNNING_MAIN, e);
    } catch (NullPointerException e) {
      throw new ValidationRuntimeException(ExceptionKey.NO_MAIN_METHOD, e);
    }
  }
}
