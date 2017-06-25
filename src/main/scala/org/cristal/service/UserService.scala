package org.cristal.service

import akka.actor.{Actor, ActorRef, Props}
import org.cristal.api.ApiMessage
import org.cristal.api.helper.JsonSupport
import org.cristal.model.{NewUser, User}
import org.cristal.repository.UserRepository
import org.cristal.service.UserService.{CreateUser, RetrieveUser}


class UserService(userRepository: ActorRef) extends Actor with JsonSupport {
  override def receive: Receive = {
    case CreateUser(newUser) =>
      userRepository ! UserRepository.CreateUser(newUser, sender)
    case RetrieveUser() =>
      userRepository ! UserRepository.RetrieveUser()


  }
}

object UserService {
  def props(userRepository: ActorRef) = Props(new UserService(userRepository))

  case class RetrieveUser() extends ApiMessage
  case class CreateUser(newUser: NewUser) extends ApiMessage
  case class UserCreated(user: User) extends ApiMessage
}
