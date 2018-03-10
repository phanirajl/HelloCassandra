package golan.izik.insert;

import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;

public class InsertDailyAggregatedNowSixWeeks extends AbsInsertDailyAggregated {

    private static final int SUFFIX = ThreadLocalRandom.current().nextInt(0, 99999);
    private final Boolean truncateTableBeforeStart;
    private final Integer deviceCountPerDay;

    public InsertDailyAggregatedNowSixWeeks(Boolean truncateTableBeforeStart, Integer deviceCountPerDay) {
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
            devicesPerDay = -1;
        }
        System.out.println("truncate=["+truncate+"] devicesPerDay=["+devicesPerDay+"] ");
        new InsertToAggregatedTable(new InsertDailyAggregatedNowSixWeeks(truncate, devicesPerDay)).insert();
    }

    @Override
    public boolean shouldTruncateTableBeforeStart() {
        return truncateTableBeforeStart;
    }

    @Override
    public int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    @Override
    public Calendar getFirstDay() {
        final Calendar result = (Calendar) getLastDay().clone();
        result.add(Calendar.DAY_OF_YEAR, -45);
        return result;
    }

    @Override
    public Calendar getLastDay() {
        return Calendar.getInstance();
    }

    @Override
    public int getDeviceCountPerDay(Calendar cal) {
        return ( this.deviceCountPerDay==-1 ? 70_000+cal.get(Calendar.DAY_OF_YEAR) : this.deviceCountPerDay );
    }

    @Override
    public String getDeviceId(int month, int day, int deviceIndex) {
        return "device_"+deviceIndex+"_"+ SUFFIX;
    }

    @Override
    public int getDailyRowsCountPerDevice(Calendar cal) {
        return 1;
    }


}