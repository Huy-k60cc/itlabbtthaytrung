package com.huy.java8demo;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Java8Demo {
    public static void main(String[] args) {
        Java8Demo demo = new Java8Demo();

        demo.demoSortJava5();
        demo.demoMaxMinAndSortJava8();
        demo.demoMapToListJava8();
        demo.demoParallelStream();
    }

    private void demoParallelStream() {
        List<Integer> integerList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 1000000; i++) {
            integerList.add(random.nextInt(1000));
        }

        Long begin = System.nanoTime();
        for (int i = 0; i < integerList.size(); i++) {
            //do something
        }
        System.out.println("normal loop : " + (System.nanoTime() - begin));
        begin = System.nanoTime();
        integerList.parallelStream().forEach((integer -> {
            //do something
        }));
        System.out.println("parallel loop : " + (System.nanoTime() - begin));
    }

    private void demoSortJava5() {
        List<MyNumber> integerList = Arrays.asList(
                new MyNumber("so 2", 2),
                new MyNumber("so 1", 1),
                new MyNumber("so 3", 3),
                new MyNumber("so 4", 4)
        );
        integerList.sort(new Comparator<MyNumber>() {
            @Override
            public int compare(MyNumber o1, MyNumber o2) {
                if (o1.value < o2.value) return 1;
                if (o1.value > o2.value) return -1;
                return 0;
            }
        });
    }

    private void demoMapToListJava8() {
        Map<String, MyNumber> map = new HashMap<>();
        List<MyNumber> integerList = Arrays.asList(
                new MyNumber("so 2", 2),
                new MyNumber("so 1", 1),
                new MyNumber("so 3", 3),
                new MyNumber("so 4", 4)
        );
        //list to map
        System.out.println("list to map:");
        map = integerList.stream().collect(Collectors.toMap(MyNumber::getName, Function.identity()));
        map.forEach((s, myNumber) -> System.out.println(s + " : " + myNumber.getValue()));
        //map to list
        System.out.println("map to list:");
        List<MyNumber> result = new ArrayList<>();
        result = map.values().stream().collect(Collectors.toList());
        result.forEach(myNumber -> System.out.println(myNumber.getValue()));
    }

    private static void demoMaxMinAndSortJava8() {
        List<MyNumber> integerList = Arrays.asList(
                new MyNumber("so 2", 2),
                new MyNumber("so 1", 1),
                new MyNumber("so 3", 3),
                new MyNumber("so 4", 4)
        );
        //max min and average
        IntSummaryStatistics stats = integerList.stream().mapToInt(MyNumber::getValue).summaryStatistics();
        System.out.println("average : " + stats.getAverage());
        System.out.println("max : " + stats.getMax());
        System.out.println("min : " + stats.getMin());
        //java8demo
        integerList.sort(Comparator.comparing(MyNumber::getValue)
                .thenComparing(MyNumber::getName));
        integerList.forEach(myNumber -> System.out.println(myNumber.getValue()));
    }

    static class MyNumber {
        public String name;
        public Integer value;

        public MyNumber(String name, Integer value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }
    }
}
