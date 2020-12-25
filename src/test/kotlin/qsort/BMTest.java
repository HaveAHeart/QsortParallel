package qsort;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.List;
import java.util.concurrent.Executors;

@BenchmarkMode(Mode.All)
@Warmup(iterations = 5)
@Measurement(iterations = 5, batchSize = 5)
public class BMTest {

    @State(Scope.Benchmark)
    public static class BenchMarkState {
        Integer itemsAmount = 1000000;
        Qsort inst = new Qsort(itemsAmount);
        List<Integer> b = inst.getA();
    }

    @Benchmark
    public void twoThreadQSort(Blackhole blackhole, BenchMarkState state) {
        Boolean res = state.inst.mtSorting(state.b, Executors.newFixedThreadPool(2), state.itemsAmount);
        blackhole.consume(res);

    }

    @Benchmark
    public void fourThreadQSort(Blackhole blackhole, BenchMarkState state) {
        Boolean res = state.inst.mtSorting(state.b, Executors.newFixedThreadPool(4), state.itemsAmount);
        blackhole.consume(res);

    }

    @Benchmark
    public void sixThreadQSort(Blackhole blackhole, BenchMarkState state) {
        Boolean res = state.inst.mtSorting(state.b, Executors.newFixedThreadPool(6), state.itemsAmount);
        blackhole.consume(res);

    }

    @Benchmark
    public void eightThreadQSort(Blackhole blackhole, BenchMarkState state) {
        Boolean res = state.inst.mtSorting(state.b, Executors.newFixedThreadPool(8), state.itemsAmount);
        blackhole.consume(res);
    }

    @Benchmark
    public void oneThreadQSort(Blackhole blackhole, BenchMarkState state) {
        Boolean res = state.inst.stSorting(state.b);
        blackhole.consume(res);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BMTest.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opt).run();
    }
}
