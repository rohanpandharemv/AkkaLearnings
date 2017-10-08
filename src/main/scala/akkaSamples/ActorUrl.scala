package akkaSamples

import akka.actor.{ActorSystem, Props,  Actor}

/**
 * Created by rohanp on 10/4/17.
 */
object ActorUrl extends App {

  case object CreateChild
  case class SignalChildren(order : Int)
  case class PrintMessage(order : Int)


  class ParentActor extends Actor {
    private var number = 0
    def receive = {
      case CreateChild =>
        context.actorOf(Props[ChildActor], "Child" + number)
        number += 1
      case SignalChildren(n) =>
        context.children.foreach( _ ! PrintMessage(n))
    }
  }

  class ChildActor extends Actor {
    def receive = {
      case  PrintMessage(n) => println(n +" "+self)
    }
  }

  val system = ActorSystem("HierarchyActor")
  val parentActor = system.actorOf(Props[ParentActor],"Parent1")
  val parentActor2 = system.actorOf(Props[ParentActor],"Parent2")

  parentActor ! CreateChild
  parentActor ! SignalChildren(1)
  parentActor ! CreateChild
  parentActor ! CreateChild
  parentActor ! SignalChildren(2)

  parentActor2 ! CreateChild
  parentActor2 ! CreateChild
  val child0 =  system.actorSelection("akka://HierarchyActor/user/Parent2/Child0")
  val child1 =  system.actorSelection("akka://HierarchyActor/user/Parent2/Child1")
  child0 ! PrintMessage(3)
  child1 ! PrintMessage(3)


  Thread.sleep(5000)

  system.shutdown()
}