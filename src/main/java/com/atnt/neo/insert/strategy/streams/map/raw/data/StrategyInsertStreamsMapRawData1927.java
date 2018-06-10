package com.atnt.neo.insert.strategy.streams.map.raw.data;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.generator.InsertToStreamsMapTable;
import com.atnt.neo.insert.strategy.time.TimePeriod;
import com.atnt.neo.insert.strategy.time.EveryTwoMinutesEveryHour;
import com.atnt.neo.insert.strategy.time.SingleDay;
import com.atnt.neo.insert.strategy.time.TxnPerDay;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class StrategyInsertStreamsMapRawData1927 extends AbsStrategyInsertStreamsMapRawData {

    private final Boolean truncateTableBeforeStart;
    private final Integer deviceCountPerDay;

    private StrategyInsertStreamsMapRawData1927(Boolean truncateTableBeforeStart, Integer deviceCountPerDay) {
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
        new InsertToStreamsMapTable(new StrategyInsertStreamsMapRawData1927(truncate, devicesPerDay)).insert();
    }

    @Override
    public TimePeriod getTimePeriod() {
        return new SingleDay(getYear());
    }

    @Override
    public TxnPerDay getTxnPerDay() {
        return new EveryTwoMinutesEveryHour();
    }

    @Override
    public boolean shouldTruncateTableBeforeStart() {
        return truncateTableBeforeStart;
    }

    @Override
    public int getYear() {
        return 1927;
    }

    @Override
    public int getDeviceCountPerDay(Calendar cal) {
        return this.deviceCountPerDay;
    }

    @Override
    public String getTableName() {
        return CassandraShared.T_STREAMS_MAP_RAW_DATA;
    }

    @Override
    public boolean includeTimeStamp() {
        return false;
    }

    @Override
    public boolean includeTxnId() {
        return false;
    }

    @Override
    public Map<String, Double> createDoubleStreamMap(int deviceIndex, int year, int month, int day, int hour) {
        final HashMap<String, Double> result = new HashMap<>();
        result.put("days_level", (double) (year*365*12+month*12+day+deviceIndex));
        result.put("hours_level", (double) hour);
        return result;
    }


}
