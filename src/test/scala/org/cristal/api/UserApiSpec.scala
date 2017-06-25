package org.cristal.api

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import akka.testkit.TestProbe
import org.cristal.api.helper.JsonSupport
import org.cristal.application.RequestHandler
import org.cristal.model.{NewUser, User}
import org.cristal.service.UserService
import org.scalatest.concurrent._
import org.scalatest.{Matchers, WordSpec}
import scala.concurrent.duration._


class UserApiSpec extends WordSpec with Matchers with ScalatestRouteTest with Eventually
  with JsonSupport {

  def getUserRoute(handlerActor: ActorRef) = {
    val userApi = new UserApi(handlerActor)
    userApi.userRoute
  }

  "An UserApi" should {
    "response with an OK and a new created user to a POST to /user" in {
      val requestJson =
        """{
          |"username": "name",
          |"password": "my_password"
          |}""".stripMargin

      val responseJson = "{" +
        "\"id\":1," +
        "\"username\":\"name\"," +
        "\"password\":\"my_password\"" +
        "}"

      val handler = TestProbe()
      val newUser = NewUser("name", "my_password")
      val userCreated = User(1, "name", "my_password")

      Post("/user", HttpEntity(MediaTypes.`application/json`, requestJson)) ~>
        getUserRoute(handler.ref) ~> check {
          handler.expectMsg(500 millis, UserService.CreateUser(newUser))
          handler.reply(UserService.UserCreated(userCreated))
          eventually {
            status shouldEqual StatusCodes.OK
            responseAs[String] shouldEqual responseJson
          }
        }
    }

    "response with a successful response to the /user/id path" in {
      val handler = TestProbe()
      Get("/user/1") ~> getUserRoute(handler.ref) ~> check {
        handler.expectMsg(500 millis, UserService.RetrieveUser())
        handler.reply(RequestHandler.ApiResponse())
        eventually {
          status shouldEqual StatusCodes.OK
        }
      }
    }
  }


}
