package org.cristal.repository.dao

import reactivemongo.api.{DefaultDB, MongoDriver}
import reactivemongo.core.nodeset.Authenticate
import scala.concurrent.ExecutionContext
//import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future //FIXME change for the actor execution context


trait DBConnection {
  val dbName = "cristal"
  val userName = "cristal"
  val password = "CristalRW2017"
  val driver = new MongoDriver
  val credentials = List(Authenticate(dbName, userName, password))
  val connection = driver.connection(List("localhost"), authentications = credentials)
  def db(implicit executor: ExecutionContext): Future[DefaultDB] = connection.database("cristal")
}
