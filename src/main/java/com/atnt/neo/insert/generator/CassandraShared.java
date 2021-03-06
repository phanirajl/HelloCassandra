package com.atnt.neo.insert.generator;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.SocketOptions;

@SuppressWarnings("WeakerAccess")
public class CassandraShared {

    public static final String KEYSPACE_                = "activity";
    public static final String CASSANDRA_HOST_NAME_     = "cassandra";
    public static final String T_RAW_DATA_TIME_BUCKET   = "message_info_time_bucket";
    public static final String T_STREAMS_BY_TIME        = "streams";
    public static final String T_STREAMS_LATEST         = "latest_streams_value";
    public static final String T_STREAMS_OVER_TIME      = "object_streams_by_time";
    public static final String T_STREAMS_MAP_RAW_DATA   = "data_collector";
    public static final String T_COUNTERS_RAW_DATA      = "message_info_by_type";
    public static final String T_COUNTERS_MINUTE        = "message_info_per_class_every_minute";
    public static final String T_COUNTERS_HOURLY        = "hourly_aggregator";
    public static final String T_COUNTERS_DAILY         = "daily_aggregator";
    public static final String F_VERTICAL_STREAM_NAME   = "stream_name";
    public static final String F_VERTICAL_STREAM_DOUBLE = "double_value";
    public static final String F_VERTICAL_STREAM_TEXT   = "string_value";

    public  static final int MAX_BATCH_SIZE            =   1_000;
    public  static final int MAX_PARALLELISM_CASSANDRA =      10;
    private static final int CLIENT_TIMEOUT            = 300_000;

    public static Cluster initCluster(String hostName) {
        return Cluster.builder()
                .withSocketOptions( new SocketOptions().setConnectTimeoutMillis(CLIENT_TIMEOUT) )
                .withSocketOptions( new SocketOptions().setReadTimeoutMillis(CLIENT_TIMEOUT) )
                .withSocketOptions( new SocketOptions().setTcpNoDelay(true) )
                .addContactPoint(hostName).build();
    }


}
