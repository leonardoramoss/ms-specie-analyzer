package io.species.analyzer.domain.species.stats;

public interface StatsExecutor<T extends StatsResult> {

    T execute();
}
