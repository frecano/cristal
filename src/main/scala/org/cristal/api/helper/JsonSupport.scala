package org.cristal.api.helper

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import org.cristal.model.{NewUser, User}
import spray.json.DefaultJsonProtocol

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val newUserFormat = jsonFormat5(NewUser)
  implicit val userFormat = jsonFormat5(User)
}
