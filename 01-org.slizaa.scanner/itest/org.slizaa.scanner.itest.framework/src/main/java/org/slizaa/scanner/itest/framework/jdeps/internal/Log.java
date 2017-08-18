package org.slizaa.scanner.itest.framework.jdeps.internal;

public interface Log {

    public void debug(CharSequence message);
    public void info(CharSequence message);
    public void error(CharSequence message);
}