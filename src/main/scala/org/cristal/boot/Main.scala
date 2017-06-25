package org.cristal.boot

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.RouteConcatenation
import akka.stream.ActorMaterializer
import org.cristal.api.UserApi
import org.cristal.repository.UserRepository
import org.cristal.repository.dao.UserDAOImpl
import org.cristal.service.UserService

object Main extends App with BootCore with CoreActors with Api with Rest

trait Core {
  implicit def system: ActorSystem
}

trait BootCore extends Core {
  override lazy val system = ActorSystem("cristal-actor-system")
  sys.addShutdownHook(system.terminate())
}

trait CoreActors {
  this: Core =>

  val userDAO = new UserDAOImpl()(system.dispatcher)
  val userWriter = system.actorOf(UserRepository.props(userDAO))
  val userService = system.actorOf(UserService.props(userWriter))
}

trait Api extends RouteConcatenation {
  this: CoreActors with Core =>

  implicit val executionContext = system.dispatcher

  val routes = new UserApi(userService).userRoute
}

trait Rest {
  this: Api with CoreActors with Core =>
  implicit val materializer = ActorMaterializer()

  val bindingFuture = Http().bindAndHandle(routes, "localhost", 8080)

}

