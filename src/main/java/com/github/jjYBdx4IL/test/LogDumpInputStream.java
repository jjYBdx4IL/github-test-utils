/*
 * Copyright (C) 2016 jjYBdx4IL (https://github.com/jjYBdx4IL)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.jjYBdx4IL.test;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

/**
 *
 * @author jjYBdx4IL
 */
public class LogDumpInputStream extends InputStream {

    private static final Logger log = LoggerFactory.getLogger(LogDumpInputStream.class);
    public static final boolean DEFAULT_USE_AVAILABLE = false;
    private final InputStream orig;
    private final BinLogger binLogger;
    private final boolean useAvailable;

    public LogDumpInputStream(Logger logger, Level logLevel, InputStream orig) {
        this(logger, logLevel, orig, BinLogger.DEFAULT_LINE_PREFIX, DEFAULT_USE_AVAILABLE);
    }

    public LogDumpInputStream(Logger logger, Level logLevel, InputStream orig, String linePrefix) {
        this(logger, logLevel, orig, linePrefix, DEFAULT_USE_AVAILABLE);
    }

    public LogDumpInputStream(Logger logger, Level logLevel, InputStream orig, boolean useAvailable) {
        this(logger, logLevel, orig, BinLogger.DEFAULT_LINE_PREFIX, useAvailable);
    }

    public LogDumpInputStream(Logger logger, Level logLevel, InputStream orig, String linePrefix, boolean useAvailable) {
        if (orig == null || logger == null || logLevel == null || linePrefix == null) {
            throw new IllegalArgumentException();
        }
        this.orig = orig;
        this.useAvailable = useAvailable;
        this.binLogger = new BinLogger(logger, logLevel, linePrefix);
    }

    @Override
    public int available() throws IOException {
        return orig.available();
    }

    @Override
    public void reset() throws IOException {
        orig.reset();
    }

    @Override
    public void mark(int pos) {
        orig.mark(pos);
    }

    @Override
    public boolean markSupported() {
        return orig.markSupported();
    }

    @Override
    public int read() throws IOException {
        int c = binLogger.append(orig.read());
        if (useAvailable && orig.available() == 0) {
            binLogger.flush("noavl");
        }
        return c;
    }

    @Override
    public void close() throws IOException {
        try {
            binLogger.close();
        } catch (Exception ex) {
            log.error("failed to close binary stream logger", ex);
        }
        orig.close();
    }

    public long getByteCount() {
        return binLogger.getTotalByteCount();
    }
}
