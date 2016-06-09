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
package com.github.jjYBdx4IL.junit.runners;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author jjYBdx4IL
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface RetryRunnerConfig {
    final static int DEFAULT_RETRIES = 3;
    final static long DEFAULT_DELAY_MILLIS = 0l;
    int retries() default DEFAULT_RETRIES;
    long delayMillis() default DEFAULT_DELAY_MILLIS;
}
