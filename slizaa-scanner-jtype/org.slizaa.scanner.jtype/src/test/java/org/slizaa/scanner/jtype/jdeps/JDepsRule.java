package org.slizaa.scanner.jtype.jdeps;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slizaa.scanner.jtype.jdeps.internal.JDepsWrapper;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JDepsRule implements TestRule {

  /** - */
  private Map<String, List<String>> _jdepAnalysis;

  /** - */
  private Supplier<File>            _fileSupplier;

  /**
   * <p>
   * Creates a new instance of type {@link JDepsRule}.
   * </p>
   *
   * @param fileSupplier
   */
  public JDepsRule(Supplier<File> fileSupplier) {
    _fileSupplier = checkNotNull(fileSupplier);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Statement apply(Statement base, Description description) {

    return new Statement() {

      @Override
      public void evaluate() throws Throwable {

        _jdepAnalysis = new JDepsWrapper().analyze(_fileSupplier.get().getAbsolutePath());

        base.evaluate();

        //
        _jdepAnalysis.clear();
        _jdepAnalysis = null;
      }
    };
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public Map<String, List<String>> getJdepAnalysis() {
    return _jdepAnalysis;
  }
}
