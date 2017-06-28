/*
 * Copyright © 2016 jjYBdx4IL (https://github.com/jjYBdx4IL)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.jjYBdx4IL.test;

import java.io.File;

import org.apache.commons.lang3.SystemUtils;
import org.apache.log4j.Logger;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author jjYBdx4IL
 */
public class CompileTest extends Compile {
	
    @SuppressWarnings("unused")
	private final static Logger log = Logger.getLogger(CompileTest.class.getName());
    private final static File tempDir = FileUtil.createMavenTestDir(CompileTest.class);

    @Before
    public void before() {
    	// run tests on Linux only
    	Assume.assumeTrue(SystemUtils.IS_OS_LINUX);
        FileUtil.provideCleanDirectory(tempDir);
        setClassOutputDir(tempDir);
    }

    @Test
    public void test1() {
        writeClass("pkg", "public class A", "public static int main(String[] args) {}");
        assertNotCompile();
        writeClass("pkg", "public class A", "public static void main(String[] args) {}");
        assertCompile();
        assertRun("pkg.A");
    }

}
