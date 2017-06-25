package org.cristal.repository.dao

import org.cristal.model.User
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.BSONDocument
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}


trait UserDAO {
  def insert(user: User): Future[Unit]
}

class UserDAOImpl(implicit executor: ExecutionContext)  extends UserDAO with UserCollection {
  override def insert(user: User) = handleInsertResult(
    usersCollection.flatMap(_.insert(userToDocument(user))))

  private def handleInsertResult(result: Future[WriteResult])  = {
    result.onComplete {
      case failure @ Failure(e) => e.printStackTrace(); failure
      case Success(_) => println(s"User successfully inserted.")
    }

    result.map(_ => ())
  }

  def usersCollection : Future[BSONCollection] = db.map(_.collection("users"))

  def userToDocument(user: User) = BSONDocument(
    "username" -> user.username,
    "password" -> user.password
  )
}

trait UserCollection extends DBConnection {

}





