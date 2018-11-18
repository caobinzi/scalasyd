package fp.data
import cats._
case class UserData(id: Int, data: String)
trait Gdpr[A]
case class DeleteUser(id: Int) extends Gdpr[UserData]

case class RetrieveUser(id:      Int) extends Gdpr[UserData]
case class RetrieveUserEmail(id: Int) extends Gdpr[String]

case class DeleteUserEmail(id: Int) extends Gdpr[Unit]

case class DeleteUserAddress(id: Int) extends Gdpr[Unit]

trait GdprOp[F[_]] {

  val ioNt: (Gdpr ~> F)
}
