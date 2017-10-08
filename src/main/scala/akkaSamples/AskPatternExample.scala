package akkaSamples

import akka.actor.{Props, ActorSystem, Actor}
import akka.pattern._
import akka.util._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

// Docs: https://doc.akka.io/docs/akka/current/scala/actors.html#ask-send-and-receive-future
// Youtube: https://www.youtube.com/watch?v=pW_q315FChc

/**
 * Created by rohanp on 9/27/17.
 */
object AskPatternExample extends App {
  //case object AskName
  case class TellMeYourName()
  case class NameResponse(name : String)

  class AskActor(name : String) extends Actor {
    def receive = {
      case _ =>
        //Thread.sleep(10000)
        sender ! NameResponse(name)
    }
  }

  val system = ActorSystem("AskPattenSystem")
  val actor = system.actorOf(Props(new AskActor("Rohan")),"AskActors")

  implicit val timeout = Timeout(1.second)
  val answer = actor ? TellMeYourName

  answer.foreach( n => println("Name is : " + n))

  system.shutdown()
}

/**
 * Points to remember in code
 * line 13 & 14 : case object & case class with null arguments are same. Also follows object pooling
 * line 26 : we are passing new object of AskActor to Props in parenthesis
 * like 28 : Timeout is used to stop asking process from running indefinitly
 * line 29 : Ask uses '?' insted of '!', ask method always returns Future[Any] object
 * line 33 : System.shutdown() method doesn't wait for all waiting processes to complete
 */