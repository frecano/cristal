package org.cristal.model

final case class User(
  username: String,
  password: String,
  email: String,
  firstName: String,
  lastName: String)

final case class NewUser(
  username: String,
  password: String,
  email: String,
  firstName: String,
  lastName: String) {

  private def isValidEmail() = {
    val emailRegex = """(?i)\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}\b""".r
    emailRegex.findFirstMatchIn(email).isDefined
  }

  require(isValidEmail(), "invalid email")

}
