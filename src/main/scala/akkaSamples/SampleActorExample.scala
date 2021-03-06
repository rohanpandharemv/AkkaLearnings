package akkaSamples

import akka.actor.{Props, ActorSystem, Actor}

// Docs: https://doc.akka.io/docs/akka/current/scala/actors.html#creating-actors-with-props
// Youtube: https://www.youtube.com/watch?v=ZNaeMf7X6xU

/**
 * Created by rohanp on 9/24/17.
 */
object ActorExample extends App {

  class SampleActor extends Actor {
    override def receive: Receive = {
      case s:String => println("String : " + s)
      case n:Int => println("Number : " + n)
    }

    def foo() = println("foo")
  }

  val system = ActorSystem("SampleActorSystem")
  val actor = system.actorOf(Props[SampleActor],"SampleActor")

  println("Before message")
  actor ! "Hi actor"
  println("After String")
  actor ! 42
  println("After Number")
  actor ! 'z'
  println("After Char")

}
