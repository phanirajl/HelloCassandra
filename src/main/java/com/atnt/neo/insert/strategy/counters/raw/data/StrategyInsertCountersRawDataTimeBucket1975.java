package com.atnt.neo.insert.strategy.counters.raw.data;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.generator.InsertCountersWithTimeBucketToTable;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.EveryDaySeveralDaysEndOfYear;
import com.atnt.neo.insert.strategy.time.EveryTwoMinutesEveryHour;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

import java.util.Calendar;

/**
 * Insert data to {@link CassandraShared#T_COUNTERS_RAW_DATA} for several days in 1975
 * A lot of data for several days
 * Every 2 minutes
 * 24 hours a day
 */
public class StrategyInsertCountersRawDataTimeBucket1975 extends AbsStrategyInsertCountersRawData {

    private final Boolean truncateTableBeforeStart;
    private final Integer deviceCountPerDay;

    private StrategyInsertCountersRawDataTimeBucket1975(Boolean truncateTableBeforeStart, Integer deviceCountPerDay) {
        this.truncateTableBeforeStart = truncateTableBeforeStart;
        this.deviceCountPerDay = deviceCountPerDay;
    }

    public static void main(String[] args) throws InterruptedException {
        Boolean truncate;
        Integer devicesPerDay;
        try {
            truncate = Boolean.parseBoolean(args[0]);
            devicesPerDay = Integer.parseInt(args[1]);
        } catch (Exception e) {
            truncate = false;
            devicesPerDay = 1;
            System.out.println("Missing command-line-argument. Setting devicesPerDay to ["+devicesPerDay+"]");
        }
        System.out.println("truncate=["+truncate+"] devicesPerDay=["+devicesPerDay+"] ");
        new InsertCountersWithTimeBucketToTable(new StrategyInsertCountersRawDataTimeBucket1975(truncate, devicesPerDay)).insert();
    }

    @Override
    public TimePeriod getTimePeriod() {
        return new EveryDaySeveralDaysEndOfYear(getYear(), 8);
    }

    @Override
    public TxnPerDay getTxnPerDay() {
        return new EveryTwoMinutesEveryHour();
    }

    @Override
    public int getYear() {
        return 1975;
    }

    @Override
    public int getDeviceCountPerDay(Calendar cal) {
        return this.deviceCountPerDay;
    }

    @Override
    public String getTableName() {
        return CassandraShared.RAW_DATA_TIME_BUCKET;
    }

    @Override
    public boolean shouldTruncateTableBeforeStart() {
        return this.truncateTableBeforeStart;
    }
}
