package golan.izik.insert.strategy.usage;

import golan.izik.insert.strategy.AbsInsertAggregated;
import golan.izik.insert.usage.InsertToUsageDailyTable;

import java.util.Calendar;
import java.util.Collections;
import java.util.Set;


//1964-1965
public class InsertUsageDailyAggregated1964_65 extends AbsInsertAggregated {

    public static void main(String[] args) throws InterruptedException {
        new InsertToUsageDailyTable(new InsertUsageDailyAggregated1964_65()).insert();
    }


    @Override
    public String getTableName() {
        return "usage_daily";
    }

    @Override
    public int getYear() {
        return 1965;
    }

    @Override
    public Calendar getFirstDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(getYear()-1, Calendar.DECEMBER, 15);
        return cal;
    }

    @Override
    public String getDeviceId(int year, int month, int day, int deviceIndex) {
        return String.format("device_%4d_%2d_%d", year, month, deviceIndex);
    }

    @Override
    public Calendar getLastDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(getYear(), Calendar.MARCH, 15);
        return cal;
    }

    @Override
    public int getDeviceCountPerDay(Calendar cal) {
        return 50_000;
    }

    @Override
    public Set<Integer> getHoursArray() {
        return Collections.singleton(1);
    }

    @Override
    public boolean isHourExist() {
        return false;
    }
}
