package io.validate.sme.entity;

import java.util.List;

public record AIDecision(int id, String reason, List<Integer> ranking) {}