package org.spica.commons;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;



@Slf4j
public abstract class AbstractStrategyFactory<T>  {

  @Autowired
  private ApplicationContext applicationContext;

  public T create (String implementation) {
    Class clazzBuildEnvironment = null;
    try {
      log.debug("Create object from " + implementation);
      clazzBuildEnvironment = getClass().getClassLoader().loadClass(implementation);

      T bean = (T) applicationContext.getBean(clazzBuildEnvironment);

      return bean;
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException(e);
    }
  }

}
