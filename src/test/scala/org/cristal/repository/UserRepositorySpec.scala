package org.cristal.repository

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import akka.util.Timeout
import org.cristal.model.{NewUser, User}
import org.cristal.repository.UserRepository.UserCreated
import org.cristal.repository.dao.UserDAO
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito.when
import org.scalatest.concurrent.Eventually
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import scala.concurrent.Future
import scala.concurrent.duration._

class UserRepositorySpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll with Eventually with MockitoSugar {
  implicit val duration: Timeout = 10 seconds
  def this() = this(ActorSystem("UserRepositorySpecSystem"))

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "An UserRepository Actor" should {
    "Create a new user" in {
      val userDAO = mock[UserDAO]
      when(userDAO.insert(any())).thenReturn(Future.successful(()))
      val userRepository = system.actorOf(UserRepgository.props(userDAO))
      val newUser = NewUser("name", "my_password")
      userRepository ! UserRepository.CreateUser(newUser, self)
      expectMsgPF() {
        case UserCreated(User(_, _, _)) => ()
      }
    }
  }

}
