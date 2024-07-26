/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.impl;

// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
// ...

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.UUID;

import org.jooq.Configuration;
import org.jooq.DataType;
import org.jooq.JSON;
import org.jooq.JSONB;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.RowId;
import org.jooq.SQLDialect;
import org.jooq.types.DayToSecond;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UShort;
import org.jooq.types.YearToMonth;
import org.jooq.types.YearToSecond;
// ...
// ...
// ...
// ...
import org.jooq.util.cubrid.CUBRIDDataType;
// ...
import org.jooq.util.derby.DerbyDataType;
import org.jooq.util.firebird.FirebirdDataType;
import org.jooq.util.h2.H2DataType;
// ...
import org.jooq.util.hsqldb.HSQLDBDataType;
// ...
// ...
import org.jooq.util.mariadb.MariaDBDataType;
// ...
import org.jooq.util.mysql.MySQLDataType;
// ...
import org.jooq.util.postgres.PostgresDataType;
// ...
// ...
import org.jooq.util.sqlite.SQLiteDataType;
// ...
// ...
// ...
// ...



/**
 * The SQL standard data types, as described in {@link Types}.
 * <p>
 * These types are usually the ones that are referenced by generated source
 * code. Most RDBMS have an almost 1:1 mapping between their vendor-specific
 * types and the ones in this class (except Oracle). Some RDBMS also have
 * extensions, e.g. for geospacial data types. See the dialect-specific data
 * type classes for more information.
 *
 * @author Lukas Eder
 */
@SuppressWarnings("deprecation")
public final class SQLDataType {

    // -------------------------------------------------------------------------
    // String types
    // -------------------------------------------------------------------------

    /**
     * The {@link Types#VARCHAR} type.
     */
    public static final DataType<String> VARCHAR = new DefaultDataType<>(null, String.class, "varchar");

    /**
     * The {@link Types#VARCHAR} type.
     */
    public static final DataType<String> VARCHAR(int length) {
        return VARCHAR.length(length);
    }

    /**
     * The {@link Types#CHAR} type.
     */
    public static final DataType<String> CHAR = new DefaultDataType<>(null, String.class, "char");

    /**
     * The {@link Types#CHAR} type.
     */
    public static final DataType<String> CHAR(int length) {
        return CHAR.length(length);
    }

    /**
     * The {@link Types#LONGVARCHAR} type.
     */
    public static final DataType<String> LONGVARCHAR = new DefaultDataType<>(null, String.class, "longvarchar");

    /**
     * The {@link Types#LONGVARCHAR} type.
     */
    public static final DataType<String> LONGVARCHAR(int length) {
        return LONGVARCHAR.length(length);
    }

    /**
     * The {@link Types#CLOB} type.
     */
    public static final DataType<String> CLOB = new DefaultDataType<>(null, String.class, "clob");

    /**
     * The {@link Types#CLOB} type.
     */
    public static final DataType<String> CLOB(int length) {
        return CLOB.length(length);
    }

    /**
     * The {@link Types#NVARCHAR} type.
     */
    public static final DataType<String> NVARCHAR = new DefaultDataType<>(null, String.class, "nvarchar");

    /**
     * The {@link Types#NVARCHAR} type.
     */
    public static final DataType<String> NVARCHAR(int length) {
        return NVARCHAR.length(length);
    }

    /**
     * The {@link Types#NCHAR} type.
     */
    public static final DataType<String> NCHAR = new DefaultDataType<>(null, String.class, "nchar");

    /**
     * The {@link Types#NCHAR} type.
     */
    public static final DataType<String> NCHAR(int length) {
        return NCHAR.length(length);
    }

    /**
     * The {@link Types#LONGNVARCHAR} type.
     */
    public static final DataType<String> LONGNVARCHAR = new DefaultDataType<>(null, String.class, "longnvarchar");

    /**
     * The {@link Types#LONGNVARCHAR} type.
     */
    public static final DataType<String> LONGNVARCHAR(int length) {
        return LONGNVARCHAR.length(length);
    }

    /**
     * The {@link Types#NCLOB} type.
     */
    public static final DataType<String> NCLOB = new DefaultDataType<>(null, String.class, "nclob");

    /**
     * The {@link Types#NCLOB} type.
     */
    public static final DataType<String> NCLOB(int length) {
        return NCLOB.length(length);
    }

    // -------------------------------------------------------------------------
    // Boolean types
    // -------------------------------------------------------------------------

    /**
     * The {@link Types#BOOLEAN} type.
     */
    public static final DataType<Boolean> BOOLEAN = new DefaultDataType<>(null, Boolean.class, "boolean");

    /**
     * The {@link Types#BIT} type.
     */
    public static final DataType<Boolean> BIT = new DefaultDataType<>(null, Boolean.class, "bit");

    // -------------------------------------------------------------------------
    // Integer types
    // -------------------------------------------------------------------------

    /**
     * The {@link Types#TINYINT} type.
     */
    public static final DataType<Byte> TINYINT = new DefaultDataType<>(null, Byte.class, "tinyint");

    /**
     * The {@link Types#SMALLINT} type.
     */
    public static final DataType<Short> SMALLINT = new DefaultDataType<>(null, Short.class, "smallint");

    /**
     * The {@link Types#INTEGER} type.
     */
    public static final DataType<Integer> INTEGER = new DefaultDataType<>(null, Integer.class, "integer");

    /**
     * The {@link Types#BIGINT} type.
     */
    public static final DataType<Long> BIGINT = new DefaultDataType<>(null, Long.class, "bigint");

    /**
     * The zero-scale {@link Types#DECIMAL} type.
     */
    public static final DataType<BigInteger> DECIMAL_INTEGER = new DefaultDataType<>(null, BigInteger.class, "decimal_integer");

    /**
     * The zero-scale {@link Types#DECIMAL} type.
     */
    public static final DataType<BigInteger> DECIMAL_INTEGER(int precision) {
        return DECIMAL_INTEGER.precision(precision);
    }

    // -------------------------------------------------------------------------
    // Unsigned integer types
    // -------------------------------------------------------------------------

    /**
     * The unsigned {@link Types#TINYINT} type.
     */
    public static final DataType<UByte> TINYINTUNSIGNED = new DefaultDataType<>(null, UByte.class, "tinyint unsigned");

    /**
     * The unsigned {@link Types#SMALLINT} type.
     */
    public static final DataType<UShort> SMALLINTUNSIGNED = new DefaultDataType<>(null, UShort.class, "smallint unsigned");

    /**
     * The unsigned {@link Types#INTEGER} type.
     */
    public static final DataType<UInteger> INTEGERUNSIGNED = new DefaultDataType<>(null, UInteger.class, "integer unsigned");

    /**
     * The unsigned {@link Types#BIGINT} type.
     */
    public static final DataType<ULong> BIGINTUNSIGNED = new DefaultDataType<>(null, ULong.class, "bigint unsigned");

    // -------------------------------------------------------------------------
    // Floating point types
    // -------------------------------------------------------------------------

    /**
     * The {@link Types#DOUBLE} type.
     */
    public static final DataType<Double> DOUBLE = new DefaultDataType<>(null, Double.class, "double");

    /**
     * The {@link Types#FLOAT} type.
     */
    public static final DataType<Double> FLOAT = new DefaultDataType<>(null, Double.class, "float");

    /**
     * The {@link Types#REAL} type.
     */
    public static final DataType<Float> REAL = new DefaultDataType<>(null, Float.class, "real");

    // -------------------------------------------------------------------------
    // Numeric types
    // -------------------------------------------------------------------------

    /**
     * The {@link Types#NUMERIC} type.
     */
    public static final DataType<BigDecimal> NUMERIC = new DefaultDataType<>(null, BigDecimal.class, "numeric");

    /**
     * The {@link Types#NUMERIC} type.
     */
    public static final DataType<BigDecimal> NUMERIC(int precision) {
        return NUMERIC.precision(precision);
    }

    /**
     * The {@link Types#NUMERIC} type.
     */
    public static final DataType<BigDecimal> NUMERIC(int precision, int scale) {
        return NUMERIC.precision(precision, scale);
    }

    /**
     * The {@link Types#DECIMAL} type.
     */
    public static final DataType<BigDecimal> DECIMAL = new DefaultDataType<>(null, BigDecimal.class, "decimal");

    /**
     * The {@link Types#DECIMAL} type.
     */
    public static final DataType<BigDecimal> DECIMAL(int precision) {
        return DECIMAL.precision(precision);
    }

    /**
     * The {@link Types#DECIMAL} type.
     */
    public static final DataType<BigDecimal> DECIMAL(int precision, int scale) {
        return DECIMAL.precision(precision, scale);
    }

    // -------------------------------------------------------------------------
    // Datetime types
    // -------------------------------------------------------------------------

    /**
     * The {@link Types#DATE} type.
     */
    public static final DataType<java.util.Date> DATE = new DefaultDataType<>(null, java.util.Date.class, "date");

    public static final DataType<Date> JAVA_DATE = new DefaultDataType<>(null, Date.class, "date");

    /**
     * The {@link Types#TIMESTAMP} type.
     */
    public static final DataType<Timestamp> TIMESTAMP = new DefaultDataType<>(null, Timestamp.class, "timestamp");

    /**
     * The {@link Types#TIMESTAMP} type.
     */
    public static final DataType<Timestamp> TIMESTAMP(int precision) {
        return TIMESTAMP.precision(precision);
    }

    /**
     * The {@link Types#TIME} type.
     */
    public static final DataType<Time> TIME = new DefaultDataType<>(null, Time.class, "time");

    /**
     * The {@link Types#TIME} type.
     */
    public static final DataType<Time> TIME(int precision) {
        return TIME.precision(precision);
    }

    /**
     * The SQL standard <code>INTERVAL YEAR TO SECOND</code> data type.
     */
    public static final DataType<YearToSecond> INTERVAL = new DefaultDataType<>(null, YearToSecond.class, "interval");

    /**
     * The SQL standard <code>INTERVAL YEAR TO MONTH</code> data type.
     */
    public static final DataType<YearToMonth> INTERVALYEARTOMONTH = new DefaultDataType<>(null, YearToMonth.class, "interval year to month");

    /**
     * The SQL standard <code>INTERVAL DAY TO SECOND</code> data type.
     */
    public static final DataType<DayToSecond> INTERVALDAYTOSECOND = new DefaultDataType<>(null, DayToSecond.class, "interval day to second");


    // -------------------------------------------------------------------------
    // JSR310 types
    // -------------------------------------------------------------------------

    /**
     * The {@link Types#DATE} type.
     */
    public static final DataType<LocalDate> LOCALDATE = new DefaultDataType<>(null, LocalDate.class, "date");

    /**
     * The {@link Types#TIME} type.
     */
    public static final DataType<LocalTime> LOCALTIME = new DefaultDataType<>(null, LocalTime.class, "time");

    /**
     * The {@link Types#TIMESTAMP} type.
     */
    public static final DataType<LocalDateTime> LOCALDATETIME = new DefaultDataType<>(null, LocalDateTime.class, "timestamp");

    /**
     * The {@link Types#TIME_WITH_TIMEZONE} type.
     * <p>
     * The behaviour of this data type is influenced by the JDBC driver and the
     * database that is used. Some databases support actual time zones (as in
     * {@link ZonedDateTime}), other databases support only offsets (as in
     * {@link OffsetDateTime}). Some databases retain the actual time zone
     * information that is stored and reproduce it with every fetch (e.g.
     * {@link SQLDialect#ORACLE}), others use this type as a synonym for a
     * timestamp in UTC (e.g. {@link SQLDialect#POSTGRES}), producing possibly a
     * value in the current time zone of the database or the client. Please
     * refer to your database for more information about the behaviour of this
     * data type.
     */
    public static final DataType<OffsetTime> OFFSETTIME = new DefaultDataType<>(null, OffsetTime.class, "time with time zone");

    /**
     * The {@link Types#TIME_WITH_TIMEZONE} type.
     * <p>
     * The behaviour of this data type is influenced by the JDBC driver and the
     * database that is used. Some databases support actual time zones (as in
     * {@link ZonedDateTime}), other databases support only offsets (as in
     * {@link OffsetDateTime}). Some databases retain the actual time zone
     * information that is stored and reproduce it with every fetch (e.g.
     * {@link SQLDialect#ORACLE}), others use this type as a synonym for a
     * timestamp in UTC (e.g. {@link SQLDialect#POSTGRES}), producing possibly a
     * value in the current time zone of the database or the client. Please
     * refer to your database for more information about the behaviour of this
     * data type.
     */
    public static final DataType<OffsetTime> OFFSETTIME(int precision) {
        return OFFSETTIME.precision(precision);
    }

    /**
     * The {@link Types#TIMESTAMP_WITH_TIMEZONE} type.
     * <p>
     * The behaviour of this data type is influenced by the JDBC driver and the
     * database that is used. Some databases support actual time zones (as in
     * {@link ZonedDateTime}), other databases support only offsets (as in
     * {@link OffsetDateTime}). Some databases retain the actual time zone
     * information that is stored and reproduce it with every fetch (e.g.
     * {@link SQLDialect#ORACLE}), others use this type as a synonym for a
     * timestamp in UTC (e.g. {@link SQLDialect#POSTGRES}), producing possibly a
     * value in the current time zone of the database or the client. Please
     * refer to your database for more information about the behaviour of this
     * data type.
     */
    public static final DataType<OffsetDateTime> OFFSETDATETIME = new DefaultDataType<>(null, OffsetDateTime.class, "timestamp with time zone");

    /**
     * The {@link Types#TIMESTAMP_WITH_TIMEZONE} type.
     * <p>
     * The behaviour of this data type is influenced by the JDBC driver and the
     * database that is used. Some databases support actual time zones (as in
     * {@link ZonedDateTime}), other databases support only offsets (as in
     * {@link OffsetDateTime}). Some databases retain the actual time zone
     * information that is stored and reproduce it with every fetch (e.g.
     * {@link SQLDialect#ORACLE}), others use this type as a synonym for a
     * timestamp in UTC (e.g. {@link SQLDialect#POSTGRES}), producing possibly a
     * value in the current time zone of the database or the client. Please
     * refer to your database for more information about the behaviour of this
     * data type.
     */
    public static final DataType<OffsetDateTime> OFFSETDATETIME(int precision) {
        return OFFSETDATETIME.precision(precision);
    }

    /**
     * The {@link Types#TIME_WITH_TIMEZONE} type.
     * <p>
     * An alias for {@link #OFFSETTIME}
     */
    public static final DataType<OffsetTime> TIMEWITHTIMEZONE = OFFSETTIME;

    /**
     * The {@link Types#TIME_WITH_TIMEZONE} type.
     * <p>
     * An alias for {@link #OFFSETTIME}
     */
    public static final DataType<OffsetTime> TIMEWITHTIMEZONE(int precision) {
        return TIMEWITHTIMEZONE.precision(precision);
    }

    /**
     * The {@link Types#TIMESTAMP_WITH_TIMEZONE} type.
     * <p>
     * An alias for {@link #OFFSETDATETIME}
     */
    public static final DataType<OffsetDateTime> TIMESTAMPWITHTIMEZONE = OFFSETDATETIME;

    /**
     * The {@link Types#TIMESTAMP_WITH_TIMEZONE} type.
     * <p>
     * An alias for {@link #OFFSETDATETIME}
     */
    public static final DataType<OffsetDateTime> TIMESTAMPWITHTIMEZONE(int precision) {
        return TIMESTAMPWITHTIMEZONE.precision(precision);
    }

    /**
     * A {@link Types#TIMESTAMP_WITH_TIMEZONE} type that uses UTC as time zone.
     * <p>
     * Neither JDBC, nor most SQL databases support the <code>INSTANT</code>
     * data type, which is often the only kind of timestamp which can be
     * expected to behave across all server and client time zone settings. This
     * implementation is backed by the database vendor's
     * <code>TIMESTAMP WITH TIME ZONE</code> data type implementation, which may
     * (e.g. Oracle) or may not (e.g. PostgreSQL) store the timestamp
     * information. Irrespective of that storage, this type will always produce
     * time zone agnostic instants in client code.
     */
    public static final DataType<Instant> INSTANT = new DefaultDataType<>(null, Instant.class, "instant");

    /**
     * A {@link Types#TIMESTAMP_WITH_TIMEZONE} type that uses UTC as time zone.
     * <p>
     * Neither JDBC, nor most SQL databases support the <code>INSTANT</code>
     * data type, which is often the only kind of timestamp which can be
     * expected to behave across all server and client time zone settings. This
     * implementation is backed by the database vendor's
     * <code>TIMESTAMP WITH TIME ZONE</code> data type implementation, which may
     * (e.g. Oracle) or may not (e.g. PostgreSQL) store the timestamp
     * information. Irrespective of that storage, this type will always produce
     * time zone agnostic instants in client code.
     */
    public static final DataType<Instant> INSTANT(int precision) {
        return INSTANT.precision(precision);
    }


    // -------------------------------------------------------------------------
    // Binary types
    // -------------------------------------------------------------------------

    /**
     * The {@link Types#BINARY} type.
     */
    public static final DataType<byte[]> BINARY = new DefaultDataType<>(null, byte[].class, "binary");

    /**
     * The {@link Types#BINARY} type.
     */
    public static final DataType<byte[]> BINARY(int length) {
        return BINARY.length(length);
    }

    /**
     * The {@link Types#VARBINARY} type.
     */
    public static final DataType<byte[]> VARBINARY = new DefaultDataType<>(null, byte[].class, "varbinary");

    /**
     * The {@link Types#VARBINARY} type.
     */
    public static final DataType<byte[]> VARBINARY(int length) {
        return VARBINARY.length(length);
    }

    /**
     * The {@link Types#LONGVARBINARY} type.
     */
    public static final DataType<byte[]> LONGVARBINARY = new DefaultDataType<>(null, byte[].class, "longvarbinary");

    /**
     * The {@link Types#LONGVARBINARY} type.
     */
    public static final DataType<byte[]> LONGVARBINARY(int length) {
        return LONGVARBINARY.length(length);
    }

    /**
     * The {@link Types#BLOB} type.
     */
    public static final DataType<byte[]> BLOB = new DefaultDataType<>(null, byte[].class, "blob");

    /**
     * The {@link Types#BLOB} type.
     */
    public static final DataType<byte[]> BLOB(int length) {
        return BLOB.length(length);
    }

    // -------------------------------------------------------------------------
    // Other types
    // -------------------------------------------------------------------------

    /**
     * The {@link Types#OTHER} type.
     */
    public static final DataType<Object> OTHER = new DefaultDataType<>(null, Object.class, "other");

    /**
     * The {@link Types#ROWID} type.
     */
    public static final DataType<RowId> ROWID = new DefaultDataType<>(null, RowId.class, "rowid");

    /**
     * The {@link Types#STRUCT} type.
     */
    public static final DataType<Record> RECORD = new DefaultDataType<>(null, Record.class, "record");

    /**
     * The {@link ResultSet} type.
     * <p>
     * This is not a SQL or JDBC standard. This type emulates REF CURSOR types
     * and similar constructs
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static final DataType<Result<Record>> RESULT = new DefaultDataType<>(null, (Class) Result.class, "result");

    /**
     * The {@link UUID} type.
     * <p>
     * This is not a SQL or JDBC standard. This type handles UUID types where
     * they are supported
     */
    public static final DataType<UUID> UUID = new DefaultDataType<>(null, UUID.class, "uuid");

    /**
     * The {@link JSON} type.
     * <p>
     * This is not a SQL or JDBC standard. This type handles JSON types where
     * they are supported.
     */
    public static final DataType<JSON> JSON = new DefaultDataType<>(null, JSON.class, "json");

    /**
     * The {@link JSONB} type.
     * <p>
     * This is not a SQL or JDBC standard. This type handles JSONB types where
     * they are supported
     */
    public static final DataType<JSONB> JSONB = new DefaultDataType<>(null, JSONB.class, "jsonb");

    // -------------------------------------------------------------------------
    // Static initialisation of dialect-specific data types
    // -------------------------------------------------------------------------

    static {
        // Load all dialect-specific data types
        // TODO [#5713] Make this more reliable using a data type registry

        try {


















































            Class.forName(CUBRIDDataType.class.getName());
            initJSR310Types(CUBRID);

            Class.forName(DerbyDataType.class.getName());
            initJSR310Types(DERBY);

            Class.forName(FirebirdDataType.class.getName());
            initJSR310Types(FIREBIRD);

            Class.forName(H2DataType.class.getName());
            initJSR310Types(H2);

            Class.forName(HSQLDBDataType.class.getName());
            initJSR310Types(HSQLDB);

            Class.forName(MariaDBDataType.class.getName());
            initJSR310Types(MARIADB);

            Class.forName(MySQLDataType.class.getName());
            initJSR310Types(MYSQL);

            Class.forName(PostgresDataType.class.getName());
            initJSR310Types(POSTGRES);

            Class.forName(SQLiteDataType.class.getName());
            initJSR310Types(SQLITE);

        } catch (Exception ignore) {}
    }

    private static final void initJSR310Types(SQLDialect family) {

        Configuration configuration = new DefaultConfiguration(family);

        // [#8561] Register JSR-310 types according to their matching JDBC
        //         type configuration
        new DefaultDataType<>(family, SQLDataType.LOCALDATE, DATE.getTypeName(configuration), DATE.getCastTypeName(configuration));
        new DefaultDataType<>(family, SQLDataType.LOCALTIME, TIME.getTypeName(configuration), TIME.getCastTypeName(configuration));
        new DefaultDataType<>(family, SQLDataType.LOCALDATETIME, TIMESTAMP.getTypeName(configuration), TIMESTAMP.getCastTypeName(configuration));

    }

    /**
     * No instances
     */
    private SQLDataType() {}
}
