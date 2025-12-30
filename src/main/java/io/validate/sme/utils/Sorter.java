package io.validate.sme.utils;

import io.validate.sme.entity.LoanOption;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Sorter {

    public static List<LoanOption> sortByRanking(List<LoanOption> options, List<Integer> ranking) {
        Map<Integer, Integer> rankIndex = new HashMap<>();
        for (int i = 0; i < ranking.size(); i++) {
            rankIndex.put(ranking.get(i), i);
        }

        return options.stream().sorted(Comparator.comparingInt( o -> rankIndex.getOrDefault(o.id(), Integer.MAX_VALUE))).collect(Collectors.toList());
    }
}
