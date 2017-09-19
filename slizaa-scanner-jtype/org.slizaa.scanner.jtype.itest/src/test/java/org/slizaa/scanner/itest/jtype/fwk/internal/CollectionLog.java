package org.slizaa.scanner.itest.jtype.fwk.internal;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class CollectionLog implements Log {

  private static final String       THREE_SPACES           = "\\u0020\\u0020\\u0020";

  private static final String       PATTERN_STRING_CLASS   = THREE_SPACES + "([\\w\\.$]*)(.*)";

  private static final String       REFERENCE_STRING_CLASS = THREE_SPACES + THREE_SPACES
      + "\\u002D\\u003E\\u0020([\\w\\.$]*)(.*)";

  static final Pattern              PATTERN_CLASS          = Pattern.compile(PATTERN_STRING_CLASS);

  static final Pattern              REFERENCE_CLASS        = Pattern.compile(REFERENCE_STRING_CLASS);

  /** - */
  private Map<String, List<String>> _result                = new HashMap<>();

  /** - */
  private List<String>              _currentReferences     = null;

  @Override
  public void info(CharSequence message) {
    //
  }

  @Override
  public void error(CharSequence message) {
    //
  }

  @Override
  public void debug(CharSequence message) {

    // class?
    Matcher matcher = REFERENCE_CLASS.matcher(message);
    if (matcher.matches() && _currentReferences != null) {
      _currentReferences.add(matcher.group(1));
      return;
    }

    // reference?
    matcher = PATTERN_CLASS.matcher(message);
    if (matcher.matches()) {
      _currentReferences = _result.computeIfAbsent(matcher.group(1), key -> new LinkedList<>());
    }
  }

  public Map<String, List<String>> getResult() {
    return _result;
  }
}