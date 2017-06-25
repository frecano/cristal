package org.cristal.application

import akka.actor.{Actor, ActorRef, ActorSystem, Kill, Props}
import akka.http.scaladsl.model.StatusCodes
import org.cristal.api.ApiMessage
import org.cristal.api.helper.{ImperativeRequestContext, JsonSupport}
import org.cristal.application.RequestHandler.{ApiRequest, ApiResponse}
import org.cristal.repository.UserRepository
import org.cristal.service.UserService

trait RequestHandlerActor {
  def requestHandler(implicit system: ActorSystem) = system.actorOf(RequestHandler.props())
}


class RequestHandler extends Actor with JsonSupport {

  var ctx: Option[ImperativeRequestContext] = None;

  override def receive = {
    case ApiRequest(msg, actor, impCtx) =>
      ctx = Option(impCtx)
      actor ! msg
    case ApiResponse() =>
      ctx.map(_.complete("OK"))
      self ! Kill
    case UserRepository.UserCreated(user) =>
      ctx.map(_.complete(StatusCodes.OK, user))
      self ! Kill
    case UserService.UserCreated(user) =>
      ctx.map(_.complete(StatusCodes.OK, user))
      self ! Kill
    case s: String =>
      ctx.map(_.complete(s))
      self ! Kill
    case _ => throw new Exception("NO CONTEXT FOR COMPLETE THE REQUEST!!!!")
  }

}


object RequestHandler {
  def props() = Props(new RequestHandler)

  case class ApiRequest(msg: ApiMessage, actor: ActorRef, ctx: ImperativeRequestContext)
  case class ApiResponse()
}