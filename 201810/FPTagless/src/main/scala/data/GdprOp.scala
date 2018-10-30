case class UserData(info: List[String])

trait GdprOp[F[_]] {

  def deleteUser(id: Int): F[UserData]

  def retrieveUser(id: Int): F[UserData]

  def deleteUserEmail(email:   String): F[UserData]
  def retrieveUserEmail(email: String): F[UserData]
}
