import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.MultiWindowsJoinedStreams;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.TimestampExtractor;
import org.apache.flink.streaming.api.windowing.assigners.SlidingTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.concurrent.TimeUnit;

/**
 * Created by sesteves on 12-04-2016.
 */
public class MainJava {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // Window base on event time
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
//        env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);

        final KeySelector<Tuple3<Long, String, Integer>, String> keySelector1 = new KeySelector<Tuple3<Long, String, Integer>, String>() {
            private static final long serialVersionUID = -2331085130954253915L;

            @Override
            public String getKey(Tuple3<Long, String, Integer> value) throws Exception {
                return value.f1;
            }
        };

        DataStream<Tuple3<Long, String, Integer>> age =
                env.fromCollection(EventTimeDataJava.personWithAge)
                        .assignTimestamps(new TimestampExtractor<Tuple3<Long, String, Integer>>() {
                            private static final long serialVersionUID = 4792756542078075273L;
                            private static final long MAX_DELAY = 3000;
                            private long currentEventTimestamp = 0;

                            @Override
                            public long extractTimestamp(Tuple3<Long, String, Integer> element, long currentTimestamp) {
                                currentEventTimestamp = element.f0;
                                return element.f0;
                            }

                            @Override
                            public long extractWatermark(Tuple3<Long, String, Integer> element, long currentTimestamp) {
                                return currentEventTimestamp - MAX_DELAY;
                            }

                            @Override
                            public long getCurrentWatermark() {
                                return currentEventTimestamp - MAX_DELAY;
                            }
                        });

        final KeySelector<Tuple3<Long, String, String>, String> keySelector2 = new KeySelector<Tuple3<Long, String, String>, String>() {
            private static final long serialVersionUID = -1496992413438410386L;

            @Override
            public String getKey(Tuple3<Long, String, String> value) throws Exception {
                return value.f1;
            }
        };

        DataStream<Tuple3<Long, String, String>> interest =
                env.fromCollection(EventTimeDataJava.personWithInterest)
                        .assignTimestamps(new TimestampExtractor<Tuple3<Long, String, String>>() {
                            private static final long serialVersionUID = -8573146434706396157L;
                            private static final long MAX_DELAY = 3000;
                            private long currentEventTimestamp = 0;

                            @Override
                            public long extractTimestamp(Tuple3<Long, String, String> element, long currentTimestamp) {
                                currentEventTimestamp = element.f0;
                                return element.f0;
                            }

                            @Override
                            public long extractWatermark(Tuple3<Long, String, String> element, long currentTimestamp) {
                                return currentEventTimestamp - MAX_DELAY;
                            }

                            @Override
                            public long getCurrentWatermark() {
                                return currentEventTimestamp - MAX_DELAY;
                            }
                        });

        MultiWindowsJoinedStreams<Tuple3<Long, String, Integer>, Tuple3<Long, String, String>> joinedStreams =
                new MultiWindowsJoinedStreams<>(age, interest);
        joinedStreams.where(keySelector1)
                .window(SlidingTimeWindows.of(Time.of(25, TimeUnit.SECONDS), Time.of(5, TimeUnit.SECONDS)))
                .equalTo(keySelector2)
                .window(TumblingTimeWindows.of(Time.of(5, TimeUnit.SECONDS)))
                .apply(new JoinFunction<Tuple3<Long,String,Integer>, Tuple3<Long,String,String>, Tuple3<String, Integer, String>>() {
                    private static final long serialVersionUID = 4218145254060450452L;

                    @Override
                    public Tuple3<String, Integer, String> join(Tuple3<Long, String, Integer> first, Tuple3<Long, String, String> second) throws Exception {
                        return new Tuple3<>(first.f1, first.f2, second.f2);
                    }
                }).print();

        env.execute("Window Join");
    }
}
