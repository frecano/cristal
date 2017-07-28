package org.cristal.service

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import org.cristal.model.NewUser
import org.cristal.repository.UserRepository
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import scala.concurrent.duration._

class UserServiceSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

    def this() = this(ActorSystem("UserServiceSpecSystem"))

    override def afterAll {
      TestKit.shutdownActorSystem(system)
    }

  "A UserService Actor" should {
    "send a create message to a repository user actor" in {
      val userRepository = TestProbe()
      val userService = system.actorOf(UserService.props(userRepository.ref))
      val newUser = NewUser("name", "my_password", "my@email.com", "John", "Doe")
      userService ! UserService.CreateUser(newUser)
      userRepository.expectMsg(500 millis, UserRepository.CreateUser(newUser, self))
    }

    "send a retrieve message to a repository user actor" in {
      val userRepository = TestProbe()
      val userService = system.actorOf(UserService.props(userRepository.ref))
      userService ! UserService.RetrieveUser()
      userRepository.expectMsg(500 millis, UserRepository.RetrieveUser())
    }
  }

}
