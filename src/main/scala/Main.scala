import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

/**
 * Created by sesteves on 17-03-2016.
 */
object Main {


    def main(args: Array[String]): Unit = {
      val env = StreamExecutionEnvironment.getExecutionEnvironment

      val rawStream1 = env.socketTextStream("ginja-a1", 8800, maxRetry = -1)
      val rawStream2 = env.socketTextStream("ginja-a1", 8800, maxRetry = -1)




      env.execute("flink-test")
    }





}