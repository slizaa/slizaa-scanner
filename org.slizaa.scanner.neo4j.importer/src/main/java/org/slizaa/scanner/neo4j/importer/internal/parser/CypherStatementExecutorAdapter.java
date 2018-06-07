package org.slizaa.scanner.neo4j.importer.internal.parser;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.slizaa.scanner.core.spi.parser.ICypherStatementExecutor;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class CypherStatementExecutorAdapter implements ICypherStatementExecutor {

  /** - */
  private GraphDatabaseService _graphDatabaseService;

  /**
   * <p>
   * Creates a new instance of type {@link CypherStatementExecutorAdapter}.
   * </p>
   *
   * @param graphDatabaseService
   */
  public CypherStatementExecutorAdapter(GraphDatabaseService graphDatabaseService) {
    _graphDatabaseService = checkNotNull(graphDatabaseService);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IResult executeCypherStatement(String cypherStatement) {

    //
    try (Transaction transaction = _graphDatabaseService.beginTx()) {
      org.neo4j.graphdb.Result result = _graphDatabaseService.execute(checkNotNull(cypherStatement));
      return new ResultAdapter(result);
    }

  }

  /**
   * <p>
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  private static class ResultAdapter implements IResult {

    /** - */
    private org.neo4j.graphdb.Result _result;

    /**
     * <p>
     * Creates a new instance of type {@link ResultAdapter}.
     * </p>
     *
     * @param result
     */
    public ResultAdapter(org.neo4j.graphdb.Result result) {
      _result = checkNotNull(result);
    }

    @Override
    public List<String> keys() {
      return _result.columns();
    }

    @Override
    public Map<String, Object> single() {

      //
      Map<String, Object> result = null;

      //
      if (_result.hasNext()) {
        result = _result.next();
      }
      //
      else {
        // TODO
        throw new RuntimeException();
      }

      //
      if (_result.hasNext()) {
        // TODO
        throw new RuntimeException();
      }

      //
      return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Map<String, Object>> list() {
      return _result.stream().collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> List<T> list(Function<Map<String, Object>, T> mapFunction) {
      return _result.stream().map(mapFunction).collect(Collectors.toList());
    }
  }
}
