package qsort;

import kotlin.random.Random;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@BenchmarkMode(Mode.All)
@Warmup(iterations = 5)
@Measurement(iterations = 1, batchSize = 1)
public class BMTest {
    public static Integer itemsAmount = 100000;
    public static Qsort inst = new Qsort();

    public static List<Integer> generateData(Integer size) {
        List<Integer> a = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            a.add(Random.Default.nextInt(-1000, 1000));
        }
        return a;
    }

    @State(Scope.Benchmark)
    public static class BenchMarkState {
        public List<Integer> dataset = generateData(itemsAmount);


    }

    @Benchmark
    public void oneThreadQSort(Blackhole blackhole, BenchMarkState state) {
        Boolean res = inst.stSorting(state.dataset);
        blackhole.consume(res);
    }

    @Benchmark
    public void twoThreadQSort(Blackhole blackhole, BenchMarkState state) {
        Boolean res = inst.mtSorting(state.dataset, Executors.newFixedThreadPool(2));
        blackhole.consume(res);
    }

    @Benchmark
    public void fourThreadQSort(Blackhole blackhole, BenchMarkState state) {
        Boolean res = inst.mtSorting(state.dataset, Executors.newFixedThreadPool(4));
        blackhole.consume(res);

    }

    @Benchmark
    public void sixThreadQSort(Blackhole blackhole, BenchMarkState state) {
        Boolean res = inst.mtSorting(state.dataset, Executors.newFixedThreadPool(6));
        blackhole.consume(res);

    }

    @Benchmark
    public void eightThreadQSort(Blackhole blackhole, BenchMarkState state) {
        Boolean res = inst.mtSorting(state.dataset, Executors.newFixedThreadPool(8));
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
