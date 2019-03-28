package org.spica.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractFactory<T> {

  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFactory.class);

  public T create (String implementation) {
    Class clazzBuildEnvironment = null;
    try {
      LOGGER.debug("Create object from " + implementation);
      clazzBuildEnvironment = getClass().getClassLoader().loadClass(implementation);
      T buildEnvironment = (T) clazzBuildEnvironment.newInstance();

      return buildEnvironment;
    } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
      throw new IllegalStateException(e);
    }
  }

}
