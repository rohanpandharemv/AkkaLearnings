package akkaSamples

import akka.actor.{ActorRef, Props, ActorSystem, Actor}

// Docs: https://doc.akka.io/docs/akka/current/scala/general/addressing.html#querying-the-logical-actor-hierarchy
// Youtube: https://www.youtube.com/watch?v=eEuAM7SJcAE

/**
 * Created by rohanp on 9/27/17.
 */
object HeirarchyOfActorExample extends App {
  case object CreateChild
  case object SignalChildren
  case object PrintMessage


  class ParentActor extends Actor {
    private var number = 0
    private val children = collection.mutable.Buffer[ActorRef]()

    def receive = {
      case CreateChild =>
        children += context.actorOf(Props[ChildActor], "Child" + number)
        number += 1
      case SignalChildren =>
        children.foreach( _ ! PrintMessage)
    }
  }

  class ChildActor extends Actor {
    def receive = {
      case  PrintMessage => println(self)
    }
  }

  val system = ActorSystem("HierarchyActor")
  val parentActor = system.actorOf(Props[ParentActor],"Parent1")

  parentActor ! CreateChild
  parentActor ! SignalChildren
  parentActor ! CreateChild
  parentActor ! CreateChild
  parentActor ! SignalChildren

  system.shutdown()
}