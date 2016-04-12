import java.util.concurrent.TimeUnit
import java.util.Properties

import org.apache.flink.streaming.api.scala._
import org.apache.flink.api.common.functions.JoinFunction
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.datastream.MultiWindowsJoinedStreams
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.assigners.{SlidingTimeWindows, TumblingTimeWindows}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaConsumer082}
import org.apache.flink.streaming.util.serialization.SimpleStringSchema
import org.apache.flink.api.java.functions.KeySelector



/**
 * Created by sesteves on 17-03-2016.
 */
object Main {


    def main(args: Array[String]): Unit = {

      // Based on https://github.com/yahoo/streaming-benchmarks/blob/master/flink-benchmarks/src/main/java/flink/benchmark/AdvertisingTopologyNative.java


      val kafkaPartitions = 1
      val hosts = 1
      val cores = 8

      // val params = ParameterTool.fromArgs(args)
      // val params = ParameterTool.fromSystemProperties()
      val properties = new Properties()
      properties.setProperty("bootstrap.servers", "ginja-a1:9092")
      properties.setProperty("zookeeper.connect", "ginja-a1:2181")
      properties.setProperty("group.id", "test")
      properties.put("topic", "ad-events");
      properties.put("auto.offset.reset", "smallest");
//      props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//      props.put(ProducerConfig.RETRIES_CONFIG, "3");
//      props.put(ProducerConfig.ACKS_CONFIG, "all");
//      props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "none");
//      props.put(ProducerConfig.BATCH_SIZE_CONFIG, 200);
//      props.put(ProducerConfig.BLOCK_ON_BUFFER_FULL_CONFIG, true);
//      props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
//      props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");


      val env = StreamExecutionEnvironment.getExecutionEnvironment
      env.setParallelism(hosts * cores)



      val messageStream = env
        .addSource(new FlinkKafkaConsumer082[String]("ad-events", new SimpleStringSchema(), properties))
        .setParallelism(Math.min(hosts * cores, kafkaPartitions))

      messageStream.rebalance


//      val rawStream1 = env.socketTextStream("ginja-a1", 8800, maxRetry = -1)
//      val rawStream2 = env.socketTextStream("ginja-a1", 8800, maxRetry = -1)


      // Based on https://github.com/wangyangjun/flink-stream-join/blob/master/src/test/java/fi/aalto/dmg/KafkaWindowJoinTest.java

      env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);


      val joinedStream = new MultiWindowsJoinedStreams(messageStream.getJavaStream, messageStream.getJavaStream)

      val keySelector = new KeySelector[String, String] {
        override def getKey(value: String): String = return value
      }

      joinedStream.where(keySelector)
        .window(SlidingTimeWindows.of(Time.of(25, TimeUnit.SECONDS), Time.of(5, TimeUnit.SECONDS)))
        .equalTo(keySelector)
        .window(TumblingTimeWindows.of(Time.of(5, TimeUnit.SECONDS)))


        .apply(new JoinFunction<Tuple2<String, Long>, Tuple2<String, Long>, Tuple3<String, Long, Long>>() {
          private static final long serialVersionUID = -3625150954096822268L;

          @Override
          public Tuple3<String, Long, Long> join(Tuple2<String, Long> first, Tuple2<String, Long> second) throws Exception {
            return new Tuple3<>(first.f0, first.f1, second.f1);
          }
        });



//      val stream = joinedStream
//        .where(new MyFirstKeySelector())
//        .window(SlidingTimeWindows.of(Time.of(9, TimeUnit.SECONDS), Time.of(3, TimeUnit.SECONDS)))
//        .equalTo(new MySecondKeySelector())
//        .window(TumblingTimeWindows.of(Time.of(3, TimeUnit.SECONDS)))
//        .apply(new JoinFunction());


      env.execute("flink-test")
    }


}