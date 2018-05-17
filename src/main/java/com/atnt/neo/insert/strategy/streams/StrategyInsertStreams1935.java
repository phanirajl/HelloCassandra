package com.atnt.neo.insert.strategy.streams;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.generator.streams.InsertToStreamsTable;

import java.util.Calendar;

/**
 * Insert 3 months of data (Jan01 - Feb01)
 *  - devices count - program argument
 *  - Each device reports every 2 minutes
 *  - Single stream: "bogus_stream"
 *
 */
public class StrategyInsertStreams1935 extends AbsStrategyInsertVerticalStreams<Double> {
    private final Boolean truncateTableBeforeStart;
    private final Integer deviceCountPerDay;

    private StrategyInsertStreams1935(Boolean truncate, Integer devicesPerDay) {
        this.truncateTableBeforeStart = truncate;
        this.deviceCountPerDay = devicesPerDay;
    }

    public static void main(String[] args) throws InterruptedException {
        Boolean truncate;
        Integer devicesPerDay;
        try {
            truncate = Boolean.parseBoolean(args[0]);
            devicesPerDay = Integer.parseInt(args[1]);
        } catch (Exception e) {
            truncate = false;
            devicesPerDay = 1_000;
        }
        System.out.println("truncate=["+truncate+"] devicesPerDay=["+devicesPerDay+"] ");
        new InsertToStreamsTable<Double>(new StrategyInsertStreams1935(truncate, devicesPerDay)).insert();
    }

    @Override
    public boolean shouldTruncateTableBeforeStart() {
        return this.truncateTableBeforeStart;
    }


    @Override
    public String getStreamName() {
        return "bogus_stream";
    }

    @Override
    public String getStreamColumnName() {
        return CassandraShared.getNumberStreamFieldName();
    }

    @Override
    public Double getStreamValue(int year, int month, int day, int hour, int deviceIndex) {
        return (double) ((month * 31 + day) * 24 + hour + deviceIndex);
    }

    @Override
    public int getYear() {
        return 1935;
    }

    @Override
    public Calendar getLastDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(getYear(), Calendar.FEBRUARY, 1);
        return cal;
    }

    @Override
    public int getDeviceCountPerDay(Calendar cal) {
        return this.deviceCountPerDay;
    }
}
