package org.cristal.repository

import akka.actor.{Actor, ActorRef, Props}
import org.cristal.api.ApiMessage
import org.cristal.model.{NewUser, User}
import org.cristal.repository.UserRepository.{CreateUser, RetrieveUser, UserCreated}
import org.cristal.repository.dao.UserDAO
import scala.util.{Failure, Success}


class UserRepository(userDAO: UserDAO) extends Actor {
  override def receive: Receive = {
    case CreateUser(newUser, requester) =>
      implicit val _ = context.dispatcher
      val user = User(1, newUser.username, newUser.password)
      userDAO.insert(user) onComplete {
        case Success(_) => requester ! UserCreated(user)
        case Failure(_) =>
      }
    case RetrieveUser() =>
  }
}


object UserRepository {
  def props(userDAO: UserDAO) = Props(new UserRepository(userDAO))

  case class CreateUser(newUser: NewUser, requester: ActorRef)
  case class UserCreated(user: User)
  case class RetrieveUser()
}
