#!/bin/sh
if [ -z "$SLIZAA_ROOT" ] ; then
  SCRIPT_DIR=`dirname "$0"`
  export SLIZAA_ROOT=`cd "$SCRIPT_DIR/.." && pwd -P`
fi
LIBS_DIR=$SLIZAA_ROOT/libs
java %SLIZAA_OPTS% -cp %LIBS_DIR%\* org.slizaa.scanner.cmdline.Slizaa %*

