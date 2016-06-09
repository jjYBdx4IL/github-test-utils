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

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

/**
 *
 * @author jjYBdx4IL
 */
public class InteractiveTestBaseBug2Test extends InteractiveTestBase {

    @SuppressWarnings("unused")
	private static int c = 0;

    @Test(timeout = 5000l)
    public void test1() throws InterruptedException, InvocationTargetException {
        test();
    }

    @Test(timeout = 5000l)
    public void test2() throws InterruptedException, InvocationTargetException {
        test();
    }

    public void test() throws InterruptedException, InvocationTargetException {
        c++;

        openWindow(true);
        waitForWindowClosing();
    }

}
