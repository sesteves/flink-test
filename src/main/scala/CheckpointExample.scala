import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

/**
  * Created by sesteves on 20-04-2016.
  */
object CheckpointExample {



  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    // env.setParallelism()

    // checkpoint every 1 sec
    env.enableCheckpointing(1000)

    // val adStream = env.socketTextStream("localhost", 8800, maxRetry = -1)
    // val clickStream = env.socketTextStream("localhost", 8801, maxRetry = -1)

    val adStream = env.addSource(new EventsGeneratorSource(true))
    val clickStream = env.addSource(new EventsGeneratorSource(true))








    env.execute("checkpoint-example")
  }

  /**
    * The function that maintains the per-IP-address state machines and verifies that the
    * events are consistent with the current state of the state machine. If the event is not
    * consistent with the current state, the function produces an alert.
    */
  class StateMachineMapper extends FlatMapFunction[Event, Alert] with Checkpointed[mutable.HashMap[Int, State]] {

    private[this] val states = new mutable.HashMap[Int, State]()

    override def flatMap(t: Event, out: Collector[Alert]): Unit = {

      // get and remove the current state
      val state = states.remove(t.sourceAddress).getOrElse(InitialState)

      val nextState = state.transition(t.event)
      if (nextState == InvalidTransition) {
        out.collect(Alert(t.sourceAddress, state, t.event))
      }
      else if (!nextState.terminal) {
        states.put(t.sourceAddress, nextState)
      }
    }

    /**
      * Draws a snapshot of the function's state.
      *
      * @param checkpointId The ID of the checkpoint.
      * @param timestamp The timestamp when the checkpoint was instantiated.
      * @return The state to be snapshotted, here the hash map of state machines.
      */
    override def snapshotState(checkpointId: Long, timestamp: Long): mutable.HashMap[Int, State] = {
      states
    }

    /**
      * Restores the state.
      *
      * @param state The state to be restored.
      */
    override def restoreState(state: mutable.HashMap[Int, State]): Unit = {
      states ++= state
    }

}