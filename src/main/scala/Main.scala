import java.util.concurrent.TimeUnit

import org.apache.flink.api.common.functions.JoinFunction
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.datastream.{DataStream, MultiWindowsJoinedStreams}
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.assigners.{SlidingTimeWindows, TumblingTimeWindows}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer082
import org.apache.flink.streaming.util.serialization.SimpleStringSchema


/**
 * Created by sesteves on 17-03-2016.
 */
object Main {


    def main(args: Array[String]): Unit = {

      // Based on https://github.com/yahoo/streaming-benchmarks/blob/master/flink-benchmarks/src/main/java/flink/benchmark/AdvertisingTopologyNative.java

      val parameterTool = ParameterTool.fromArgs(args);




      val kafkaPartitions = 1
      val hosts = 1
      val cores = 8

      val params = ParameterTool.fromMap(getFlinkConfs(conf))

      val env = StreamExecutionEnvironment.getExecutionEnvironment

      val messageStream = env
        .addSource(new FlinkKafkaConsumer082("ad-events", new SimpleStringSchema(), params.getProperties()))
        .setParallelism(Math.min(hosts * cores, kafkaPartitions))




      val rawStream1 = env.socketTextStream("ginja-a1", 8800, maxRetry = -1)
      val rawStream2 = env.socketTextStream("ginja-a1", 8800, maxRetry = -1)

      val joinedStream = new MultiWindowsJoinedStreams(rawStream1.getJavaStream, rawStream2.getJavaStream)


      val stream = joinedStream
        .where(new MyFirstKeySelector())
        .window(SlidingTimeWindows.of(Time.of(9, TimeUnit.SECONDS), Time.of(3, TimeUnit.SECONDS)))
        .equalTo(new MySecondKeySelector())
        .window(TumblingTimeWindows.of(Time.of(3, TimeUnit.SECONDS)))
        .apply(new JoinFunction());


      env.execute("flink-test")
    }




}