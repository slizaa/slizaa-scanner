package org.slizaa.scanner.itest.jtype.jdeps.internal.process;

public interface Log {

    public void debug(CharSequence message);
    public void info(CharSequence message);
    public void error(CharSequence message);
}