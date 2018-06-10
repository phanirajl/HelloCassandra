package com.atnt.neo.insert.strategy.counters;

import com.atnt.neo.insert.strategy.AbsStrategyInsert;

public abstract class AbsStrategyInsertCounters extends AbsStrategyInsert {

    @SuppressWarnings("unused")
    public int getBillingPoints(int month, int day, int hour) {
        return 10;
    }

    @SuppressWarnings("unused")
    public int getDataPoints(int month, int day, int hour) {
        return 30;
    }

    @SuppressWarnings("unused")
    public long getVolumeSize(int month, int day, int hour) {
        return 40;
    }
}
