package org.cristal.api

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._
import org.cristal.api.helper.{ApiHelper, JsonSupport}
import org.cristal.application.{RequestHandler, RequestHandlerActor}
import org.cristal.model.NewUser
import org.cristal.service.UserService



class UserApi(userService: ActorRef) extends ApiHelper with RequestHandlerActor with JsonSupport {

  val userRoute =
    pathPrefix("user") {
      get {
        path(LongNumber) { id =>
           extractActorSystem { implicit actorSystem =>
             imperativelyComplete { ctx =>
              requestHandler ! RequestHandler.ApiRequest(
                UserService.RetrieveUser(),
                userService,
                ctx
              )
             }
           }
        }
      } ~
        post {
          entity(as[NewUser]) { newUser =>
            extractActorSystem { implicit actorSystem =>
              imperativelyComplete { ctx =>
                requestHandler ! RequestHandler.ApiRequest(
                  UserService.CreateUser(newUser),
                  userService,
                  ctx
                )
              }
            }
          }
        }
    }

}
