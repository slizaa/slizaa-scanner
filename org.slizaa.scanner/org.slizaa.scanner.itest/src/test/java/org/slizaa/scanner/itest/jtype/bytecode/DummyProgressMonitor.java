package org.slizaa.scanner.itest.jtype.bytecode;


import org.eclipse.core.runtime.IProgressMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DummyProgressMonitor implements IProgressMonitor {

  //
  Logger         logger = LoggerFactory.getLogger(DummyProgressMonitor.class);

  private double _totalWork;

  private double _worked;

  @Override
  public void beginTask(String name, int totalWork) {
    logger.info("beginTask({}{})", name, totalWork);
    _totalWork = totalWork;
  }

  @Override
  public void done() {
    logger.info("done()");
  }

  @Override
  public void internalWorked(double work) {
    logger.info("internalWorked({})", work);
  }

  @Override
  public boolean isCanceled() {
    return false;
  }

  @Override
  public void setCanceled(boolean value) {
  }

  @Override
  public void setTaskName(String name) {
    logger.info("setTaskName()");
  }

  @Override
  public void subTask(String name) {
    logger.info("subTask({})", name);
  }

  @Override
  public void worked(int work) {
    // logger.info("worked({})", work);
    _worked = _worked + work;
    double r = (_worked / _totalWork) * 100;
    logger.info("{}%)", r);
  }
}
