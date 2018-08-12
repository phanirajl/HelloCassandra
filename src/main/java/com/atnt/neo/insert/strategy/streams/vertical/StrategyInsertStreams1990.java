package com.atnt.neo.insert.strategy.streams.vertical;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.generator.InsertVerticalStreamsOverTime;
import com.atnt.neo.insert.strategy.time.EveryDaySeveralDaysEndOfYear;
import com.atnt.neo.insert.strategy.time.EveryTwoMinutesEveryHour;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

import java.util.Collections;
import java.util.Map;

public class StrategyInsertStreams1990 extends AbsStrategyInsertStreams {
    private StrategyInsertStreams1990(String[] args) {
        super(args);
    }

    public static void main(String[] args) throws InterruptedException {
        new InsertVerticalStreamsOverTime(new StrategyInsertStreams1990(args)).insert();
    }

    @Override
    public Map<String, Double> createDoubleStreamMap(int deviceIndex, int year, int month, int day, int hour) {
        return generateDoubleStreamMap(getConfig().getStreamCount(), deviceIndex, year, month, day);
    }

    @Override
    public Map<String, String> createGeoLocationStreamMap(int deviceIndex, int year, int month, int day, int hour) {
        return Collections.emptyMap();
    }

    @Override
    public int getYear() {
        return 1990;
    }

    @Override
    public String getTableName() {
        return CassandraShared.T_STREAMS_OVER_TIME;
    }

    @Override
    public TimePeriod getTimePeriod() {
        return new EveryDaySeveralDaysEndOfYear(getYear(), 10);
    }

    @Override
    public TxnPerDay getTxnPerDay() {
        return new EveryTwoMinutesEveryHour();
    }
}
