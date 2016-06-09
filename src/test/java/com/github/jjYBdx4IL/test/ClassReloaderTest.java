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

import static com.github.jjYBdx4IL.test.Assert.waitUntilFileContentEquals;

import java.io.File;

import org.apache.commons.lang3.SystemUtils;
import org.apache.log4j.Logger;
import static org.junit.Assert.*;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author jjYBdx4IL
 */
public class ClassReloaderTest extends Compile {

    @SuppressWarnings("unused")
	private final static Logger log = Logger.getLogger(ClassReloaderTest.class.getName());
    private final static File tempDir = FileUtil.createMavenTestDir(ClassReloaderTest.class);
    private final static File compileTempDir = new File(tempDir, "compile");

    @Before
    public void before() {
    	// run tests on Linux only
    	Assume.assumeTrue(SystemUtils.IS_OS_LINUX);
        FileUtil.provideCleanDirectory(tempDir);
        setClassOutputDir(compileTempDir);
    }

    @Test
    public void testLoadClass() throws Exception {
        writeClass("pkg", "public class A", "public static String get() {return \"one\";}");
        compile();
        assertEquals("one", new ClassReloader(compileTempDir.getCanonicalPath()).
                loadClass("pkg.A").getDeclaredMethods()[0].invoke(null));

        writeClass("pkg", "public class A", "public static String get() {return \"two\";}");
        compile();
        assertEquals("two", new ClassReloader(compileTempDir.getCanonicalPath()).
                loadClass("pkg.A").getDeclaredMethods()[0].invoke(null));
    }

    @Test(expected = ClassNotFoundException.class)
    public void testLoadClassNotFound() throws Exception {
        new ClassReloader(compileTempDir.getCanonicalPath()).loadClass("pkg.A");
    }

    private final static long TIMEOUT = 60000L;
    @Test(timeout = TIMEOUT*5/6)
    public void testWatchLoadAndRun() throws Exception {
        File output = new File(tempDir, "output");
        String quoted = output.getCanonicalPath().replace("\"", "\\\"");
        writeClass("pkg", new String[]{"java.io.*"}, "public class A implements Runnable", ""
                + " public void run() {\n"
                + "     try(OutputStream os = new FileOutputStream(\"" + quoted + "\")) {\n"
                + "         os.write(\"Hello!\".getBytes());\n"
                + "     } catch (IOException ex){}\n"
                + " }");
        compile();
        Thread t = ClassReloader.watchLoadAndRun(compileTempDir.getCanonicalPath(), "pkg.A");
        compile();
        waitUntilFileContentEquals("Hello!", output, TIMEOUT);

        writeClass("pkg", new String[]{"java.io.*"}, "public class A implements Runnable", ""
                + " public void run() {\n"
                + "     try(OutputStream os = new FileOutputStream(\"" + quoted + "\")) {\n"
                + "         os.write(\"ByeBye!\".getBytes());\n"
                + "     } catch (IOException ex){}\n"
                + " }");
        Thread.sleep(ClassReloader.NO_REPEAT_MILLIS);
        compile();
        waitUntilFileContentEquals("ByeBye!", output, TIMEOUT);

        writeClass("pkg", "public class A implements Runnable", "public void run() {throw new Error();}");
        Thread.sleep(ClassReloader.NO_REPEAT_MILLIS);
        compile();
        t.join(TIMEOUT);
    }

}
