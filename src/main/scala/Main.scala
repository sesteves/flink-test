import java.util.concurrent.TimeUnit

import org.apache.flink.api.common.functions.JoinFunction
import org.apache.flink.streaming.api.datastream.{DataStream, MultiWindowsJoinedStreams}
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.assigners.{SlidingTimeWindows, TumblingTimeWindows}
import org.apache.flink.streaming.api.windowing.time.Time

import scala.collection.JavaConverters._

/**
 * Created by sesteves on 17-03-2016.
 */
object Main {


    def main(args: Array[String]): Unit = {
      val env = StreamExecutionEnvironment.getExecutionEnvironment

      val rawStream1 = env.socketTextStream("ginja-a1", 8800, maxRetry = -1)
      val rawStream2 = env.socketTextStream("ginja-a1", 8800, maxRetry = -1)



      val joinedStream = new MultiWindowsJoinedStreams(rawStream1, rawStream2)



      val stream = joinedStream
        .where(new MyFirstKeySelector())
        .window(SlidingTimeWindows.of(Time.of(9, TimeUnit.SECONDS), Time.of(3, TimeUnit.SECONDS)))
        .equalTo(new MySecondKeySelector())
        .window(TumblingTimeWindows.of(Time.of(3, TimeUnit.SECONDS)))
        .apply(new JoinFunction());


      env.execute("flink-test")
    }




}