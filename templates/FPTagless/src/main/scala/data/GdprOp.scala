case class UserData(id: Int, data: String)

trait GdprOp[F[_]] {

  def deleteUser(id: Int): F[UserData]

  def retrieveUser(id:      Int): F[UserData]
  def retrieveUserEmail(id: Int): F[String]

  def deleteUserEmail(id: Int): F[Unit]

  def deleteUserAddress(id: Int): F[Unit]
}
