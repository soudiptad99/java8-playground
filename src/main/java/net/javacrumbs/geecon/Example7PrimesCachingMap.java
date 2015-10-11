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

import static java.lang.Math.sqrt;
import static java.util.stream.IntStream.range;
import static java.util.stream.LongStream.rangeClosed;
import static net.javacrumbs.common.Utils.log;
import static net.javacrumbs.common.Utils.measure;
import static net.javacrumbs.common.Utils.sleep;

import java.util.BitSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Example7PrimesCachingMap {
    private final ConcurrentMap<Integer, Boolean> isPrime = new ConcurrentHashMap<>();


    public static void main(String[] args) throws InterruptedException, ExecutionException {
        new Example7PrimesCachingMap().doRun();
    }

    private void doRun() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newCachedThreadPool();
        // warm up
        Future<?> future = executor.submit(() -> measure(() -> log(countPrimes(1, 2_000_000))));
        future.get();

        measure(() -> {
            executor.submit(() -> measure(() -> log(countPrimes(1_000_000, 2_000_000))));
            executor.submit(() -> measure(() -> log(countPrimes(1_000_000, 2_000_000))));
            executor.submit(() -> measure(() -> log(countPrimes(1_000_000, 2_000_000))));
            executor.submit(() -> measure(() -> log(countPrimes(1_000_000, 2_000_000))));
            executor.submit(() -> measure(() -> log(countPrimes(1_000_000, 2_000_000))));
            executor.submit(() -> measure(() -> log(countPrimes(1_000_000, 2_000_000))));
            executor.submit(() -> measure(() -> log(countPrimes(1_000_000, 2_000_000))));

            executor.shutdown();
            try {
                executor.awaitTermination(60, TimeUnit.SECONDS);
            } catch (InterruptedException ignore) {
            }
        }, time -> System.out.println("Total time: " + time));
    }

    private long countPrimes(int from, int to) {
        return range(from, to)
                .filter(this::isPrime)
                .parallel()
                .count();
    }

    public boolean isPrime(int i) {
        return isPrime.computeIfAbsent(i, n -> n > 1 && rangeClosed(2, (long) sqrt(n)).noneMatch(divisor -> n % divisor == 0));
    }
}