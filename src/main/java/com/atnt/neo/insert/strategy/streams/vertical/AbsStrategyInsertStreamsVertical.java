package com.atnt.neo.insert.strategy.streams.vertical;

import com.atnt.neo.insert.generator.CassandraShared;
import com.atnt.neo.insert.strategy.AbsStrategyInsert;

import java.util.concurrent.ThreadLocalRandom;

public abstract class AbsStrategyInsertStreamsVertical<T> extends AbsStrategyInsert implements StrategyInsertVerticalStreams<T> {

    private static final int SUFFIX = ThreadLocalRandom.current().nextInt(0, 99999);

    @Override
    public String getTableName() {
        return CassandraShared.VERTICAL_STREAMS;
    }

    @Override
    public String getDeviceId(int year, int month, int day, int deviceIndex) {
        return "device_"+deviceIndex+"_"+ SUFFIX;
    }

    @Override
    public boolean includeTimeStamp() {
        return false;
    }

    @Override
    public boolean includeTxnId() {
        return false;
    }
}