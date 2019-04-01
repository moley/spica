package org.spica.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;



public abstract class AbstractStrategyFactory<T>  {

  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractStrategyFactory.class);

  @Autowired
  private ApplicationContext applicationContext;

  public T create (String implementation) {
    Class clazzBuildEnvironment = null;
    try {
      LOGGER.debug("Create object from " + implementation);
      clazzBuildEnvironment = getClass().getClassLoader().loadClass(implementation);

      T bean = (T) applicationContext.getBean(clazzBuildEnvironment);

      return bean;
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException(e);
    }
  }

}
