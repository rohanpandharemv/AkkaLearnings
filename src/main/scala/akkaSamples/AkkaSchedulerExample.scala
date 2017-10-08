package akkaSamples

import akka.actor.{Props, ActorSystem, Actor}
import scala.concurrent.duration._

/**
 * Created by rohanp on 10/8/17.
 */
object AkkaSchedulerExample extends  App {
  case object Count
  class ScheduleActor extends Actor {
      var number = 0
      def receive = {
        case Count =>
          number += 1
          println(number)
      }
  }

  val system = ActorSystem("SchedulerSystem")
  val actor = system.actorOf(Props[ScheduleActor],"actor")
  implicit val ec = system.dispatcher

  actor ! Count

  system.scheduler.scheduleOnce(1.second)(actor ! Count)
  val can = system.scheduler.schedule(0.second,100.millis,actor, Count)

  Thread.sleep(2000)

  can.cancel

  system.shutdown
}
