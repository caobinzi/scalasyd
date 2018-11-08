import cats._
import data._
import org.atnos.eff._
import cats.implicits._
case class UserData(info: List[String])
sealed trait GdprOp[A]

case class DeleteUser(id:   Int) extends GdprOp[UserData]
case class RetrieveUser(id: Int) extends GdprOp[UserData]

case class DeleteUserEmail(email:   String) extends GdprOp[UserData]
case class RetrieveUserEmail(email: String) extends GdprOp[UserData]

object GdprOp {
  type _gdprOp[R] = GdprOp |= R
}
