package golan.izik.query.recently.inactive;

import com.atnt.neo.insert.generator.CassandraShared;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

import java.util.Calendar;

class InsertSmallData {
    private static final String[] ACTIVE_DEVICES   = {"active1", "active2", "active3"      };
    private static final String[] INACTIVE_DEVICES = {"inactive1", "inactive2", "inactive3"};

    private static final String SELECT_QUERY_TEMPLATE =
            "SELECT device_id from activity.data_collector WHERE year=%d and month=%d and day=%d and hour=%d AND org_bucket='org_bucket' and project_bucket='project_bucket' GROUP BY year,month,day,hour,org_bucket,project_bucket,org_id,project_id,environment,device_id;";
    @SuppressWarnings("SpellCheckingInspection")
    private static final String INSERT_QUERY_TEMPLATE =
            "INSERT INTO activity." + CassandraShared.T_COUNTERS_RAW_DATA + " " +
                    "(year, month, day, hour, minutes, seconds, org_bucket,   project_bucket,   org_id,   project_id,   environment, device_id, timestamp, device_firmware,   device_type,   user_param) " +
                    "VALUES " +
                    "(%d,   %d,    %d,  %d,   %d,      %d,      'org_bucket', 'project_bucket', 'org_id', 'project_id', 'environment',   '%s',      %d,    'device_firmware', 'device_type', {'eventType': 'Flow','name': 'Calamp'}  );";


    public static void main(String[] args) {
        insertSmallData();
    }

    private static void insertSmallData() {
        long startTime = System.nanoTime();

        try (Cluster cluster = CassandraShared.initCluster("cassandra")) {

            Session session = cluster.connect("activity");
            Calendar now = Calendar.getInstance();

            long milliseconds;

            session.execute("truncate table "+CassandraShared.T_COUNTERS_RAW_DATA +" ;");

            //Events for current time
            milliseconds = now.getTimeInMillis();
            for (int i = 0; i < ACTIVE_DEVICES.length; i++) {
                final String deviceId = ACTIVE_DEVICES[i%(ACTIVE_DEVICES.length )];
                final String cql = formatInsertQuery(++milliseconds, deviceId);
                session.execute(cql);
            }
            System.out.println(formatSelectQuery(milliseconds));

            //Events for 2 weeks back (for both active and inactive)
            now.add(Calendar.DATE, -14);
            milliseconds = now.getTimeInMillis();
            for (int i = 0; i < INACTIVE_DEVICES.length; i++) {
                final String deviceId = INACTIVE_DEVICES[i%(INACTIVE_DEVICES.length)];
                final String cql = formatInsertQuery(++milliseconds, deviceId);
                session.execute(cql);
            }
            for (int i = 0; i < ACTIVE_DEVICES.length; i++) {
                final String deviceId = ACTIVE_DEVICES[i%(ACTIVE_DEVICES.length)];
                final String cql = formatInsertQuery(++milliseconds, deviceId);
                session.execute(cql);
            }
            System.out.println(formatSelectQuery(milliseconds));


                System.out.println("insertion took "+((System.nanoTime() - startTime)/ 1_000_000_000) + " seconds ...");

        }
    }

    private static String formatInsertQuery(long milliseconds, String deviceId) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(milliseconds);
        return formatInsertQuery(c, deviceId);
    }

    private static String formatInsertQuery(Calendar c, String deviceId) {
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH)+1;
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int hr = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        int sec = c.get(Calendar.SECOND);

        return String.format(INSERT_QUERY_TEMPLATE, mYear, mMonth, mDay, hr, min, sec, deviceId, c.getTimeInMillis());
    }

    private static String formatSelectQuery(long milliseconds) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(milliseconds);
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH)+1;
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int hr = c.get(Calendar.HOUR_OF_DAY);

        return String.format(SELECT_QUERY_TEMPLATE, mYear, mMonth, mDay, hr);
    }

}


