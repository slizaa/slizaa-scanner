package org.slizaa.scanner.jtype.jdeps.internal;

public interface Log {

    public void debug(CharSequence message);
    public void info(CharSequence message);
    public void error(CharSequence message);
}