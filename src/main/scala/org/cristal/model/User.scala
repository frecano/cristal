package org.cristal.model

final case class User(id: Long, username: String, password: String)
final case class NewUser(username: String, password: String)
