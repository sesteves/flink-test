import java.io.PrintWriter
import java.net.ServerSocket
import java.util.concurrent.ConcurrentHashMap

import scala.collection.convert.decorateAsScala._

/**
  * Created by sesteves on 20-04-2016.
  */

object DataGeneratorSocket {


  def main(args: Array[String]): Unit = {

    if (args.length != 4) {
      System.err.println("Usage: DataGenerator <portAds> <portClicks> <maxLateness> <sleepMillis>")
      System.exit(1)
    }
    val (portAds, portClicks, maxLateness, sleepMillis) = (args(0).toInt, args(1).toInt, args(2).toLong, args(3).toLong)


    val map = new ConcurrentHashMap[String, (Long, Int)]().asScala

    // thread that generates ads
    new Thread(new Runnable {
      override def run(): Unit = {

        val rnd = scala.util.Random
        val serverSocket = new ServerSocket(portAds)
        while (true) {
          val socket = serverSocket.accept()
          val out = new PrintWriter(socket.getOutputStream)
          println("Got a new connection on ads server")

          try {
            while (true) {
              def id = java.util.UUID.randomUUID.toString
              val ts = System.currentTimeMillis()
              map.put(id, (ts, rnd.nextInt(100) + 1))
              out.println(id + " " + ts)

              Thread.sleep(sleepMillis)
            }
          } catch {
            case ex: Exception => ex.printStackTrace
          } finally {
            out.close()
            socket.close()
          }
        }

      }
    }).start()

    // thread that generates clicks on ads
    new Thread(new Runnable {
      override def run(): Unit = {
        val serverSocket = new ServerSocket(portClicks)
        while (true) {
          val socket = serverSocket.accept()
          val out = new PrintWriter(socket.getOutputStream)
          println("Got a new connection on clicks server")

          try {

            while (true) {
              map.foreach({ case (id, (ts, n)) =>
                if (n > 1) {
                  while (n > 1) {
                    out.println(id + " " + System.currentTimeMillis())
                    n -= 1
                  }
                  out.flush
                  map.put(id, (ts, 1))
                } else {
                  val now = System.currentTimeMillis()
                  if (now - ts > maxLateness) {
                    out.println(id + " " + now)
                    out.flush
                  }
                  map.remove(id)
                }
              })

              Thread.sleep(sleepMillis)
            }
          } catch {
            case ex: Exception => ex.printStackTrace
          } finally {
            out.close()
            socket.close()
          }
        }

      }
    }).start()
  }
}