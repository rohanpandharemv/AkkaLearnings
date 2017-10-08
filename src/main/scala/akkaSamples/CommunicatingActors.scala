package akkaSamples

import akka.actor.{Props, ActorSystem, ActorRef, Actor}

/**
 * Created by rohanp on 9/24/17.
 */
object CommunicatingActors extends App {

  case class StartCounting(n : Int, other : ActorRef)
  case class CountDown(n : Int)

  class CountDownActor extends Actor {

    override  def receive = {
      case StartCounting(n , other) =>
        println(n)
        other ! CountDown(n-1)
      case CountDown(n) =>
        println(self)
        n > 0 match {
          case true =>
            println(n)
            sender ! CountDown(n-1)
          case false =>
            context.system.shutdown()
        }
    }
  }

  val system = ActorSystem("CommunicatingActorSystem")
  val actor1 = system.actorOf(Props[CountDownActor],"countDownActor1")
  val actor2 = system.actorOf(Props[CountDownActor],"countDownActor2")

  actor1 ! StartCounting(10, actor2)
}
