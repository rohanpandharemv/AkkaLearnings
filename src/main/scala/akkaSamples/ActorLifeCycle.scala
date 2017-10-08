package akkaSamples

import akka.actor.SupervisorStrategy.{Restart, Resume}
import akka.actor.{ActorSystem, OneForOneStrategy, Props, Actor}
import akkaSamples.SupervisionExample.{NumberDivision, PrintMessage, SignalChildren}

/**
 * Created by rohanp on 10/8/17.
 */
object ActorLifeCycle extends App {

  case object CreateChild
  case class SignalChildren(order : Int)
  case class PrintMessage(order : Int)
  case class NumberDivision(n : Int, d : Int)
  case object BadStuff

  class ParentActor extends Actor {
    private var number = 0
    def receive = {
      case CreateChild =>
        context.actorOf(Props[ChildActor], "Child" + number)
        number += 1
      case SignalChildren(n) =>
        context.children.foreach( _ ! PrintMessage(n))
    }

    override val supervisorStrategy = OneForOneStrategy(loggingEnabled = false) {
      case ae : ArithmeticException =>
        println("Exception : ArithMaticExeption found!!!, Resuming the Actor")
        Resume
      case e : Exception =>
        println("Exception : "+e.getMessage+" Exception found, Restarting actor")
        Restart
    }
  }

  class ChildActor extends Actor {
    println("Creating actor........")

    override  def preStart() = {
      super.preStart()
      println("Prestart actor")
    }

    def receive = {
      case  PrintMessage(n) => println(n +" "+self)
      case NumberDivision(n,d) => println(n/d)
      case BadStuff => throw new RuntimeException("Badstuff happend.....")
    }

    override def postStop() = {
      super.postStop()
      println("Poststop actor")
    }

    override def preRestart(reason: Throwable, message: Option[Any]) = {
      super.preRestart(reason, message)
      println("Prerestart actor")
    }

    override def postRestart(reason : Throwable) = {
      super.postRestart(reason)
      println("Postrestart actor")
    }
  }

  val system = ActorSystem("superVisionActor")
  val actor = system.actorOf(Props[ParentActor],"Parent1")

  actor ! CreateChild
  //actor ! CreateChild
  val child0 = system.actorSelection("akka://superVisionActor/user/Parent1/Child0")
  child0 ! NumberDivision(4,2)
  //child0 ! NumberDivision(4,0)
  //Thread.sleep(1000)
  child0 ! BadStuff


  Thread.sleep(5000)

  system.shutdown()
}
