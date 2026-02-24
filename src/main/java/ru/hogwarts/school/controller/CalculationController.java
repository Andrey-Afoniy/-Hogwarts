package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.LongStream;
import java.util.stream.Stream;

@RestController
@RequestMapping("/calculate")
public class CalculationController {

    @GetMapping("/sum-slow")
    public long calculateSumSlow() {
        long startTime = System.currentTimeMillis();

        long sum = Stream.iterate(1L, a -> a + 1)
                .limit(10_000_000) // Уменьшил для демонстрации, можно вернуть 1_000_000
                .reduce(0L, Long::sum);

        long endTime = System.currentTimeMillis();
        System.out.println("Slow calculation took: " + (endTime - startTime) + " ms");

        return sum;
    }

    @GetMapping("/sum-fast")
    public long calculateSumFast() {
        long startTime = System.currentTimeMillis();

        long sum1 = LongStream.rangeClosed(1, 10_000_000)
                .parallel()
                .sum();

        long sum2 = Stream.iterate(1L, a -> a + 1)
                .limit(10_000_000)
                .parallel()
                .reduce(0L, Long::sum);

        long endTime = System.currentTimeMillis();
        System.out.println("Fast calculation took: " + (endTime - startTime) + " ms");

        return sum2;
    }

    @GetMapping("/sum-optimized")
    public long calculateSumOptimized() {
        long startTime = System.currentTimeMillis();

        long n = 10_000_000;
        long sum = n * (n + 1) / 2;

        long endTime = System.currentTimeMillis();
        System.out.println("Optimized calculation took: " + (endTime - startTime) + " ms");

        return sum;
    }

    @GetMapping("/sum-comparison")
    public String compareSumCalculations() {
        long n = 10_000_000;

        long start1 = System.currentTimeMillis();
        long sum1 = LongStream.rangeClosed(1, n).sum();
        long time1 = System.currentTimeMillis() - start1;

        long start2 = System.currentTimeMillis();
        long sum2 = LongStream.rangeClosed(1, n).parallel().sum();
        long time2 = System.currentTimeMillis() - start2;

        long start3 = System.currentTimeMillis();
        long sum3 = n * (n + 1) / 2;
        long time3 = System.currentTimeMillis() - start3;

        return String.format(
                "Sequential: %d ms, sum=%d<br>" +
                        "Parallel: %d ms, sum=%d<br>" +
                        "Formula: %d ms, sum=%d",
                time1, sum1, time2, sum2, time3, sum3
        );
    }
}