/**
 * Copyright 2009-2013 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.javacrumbs.geecon;

import static java.util.stream.IntStream.range;
import static net.javacrumbs.common.Utils.measure;

import java.util.concurrent.ThreadLocalRandom;

public class Example4Nested {

    private static final int SIZE = 10_000;

    public static void main(String[] args) throws InterruptedException {
        new Example4Nested().generateRandomMatrix();
    }

    public byte[][] generateRandomMatrix() throws InterruptedException {
        byte[][] results = new byte[SIZE][SIZE];
        measure(() -> {
            range(0, SIZE).parallel().forEach(i -> {
                range(0, SIZE).forEach(j -> {
                    results[i][j] = randomByte();
                });
            });

        });
        return results;
    }

    private byte randomByte() {
        return (byte) Math.round(Math.random() * 100);
    }


}
