package fp.data
import cats._
import org.atnos.eff._

case class UserData(id: Int, data: String)

trait GdprOp[A]
case class DeleteUser(id: Int) extends GdprOp[UserData]

case class RetrieveUser(id:      Int) extends GdprOp[UserData]
case class RetrieveUserEmail(id: Int) extends GdprOp[String]

case class DeleteUserEmail(id: Int) extends GdprOp[Unit]

case class DeleteUserAddress(id: Int) extends GdprOp[Unit]

object GdprOp {
  type _gdprOp[R] = GdprOp |= R
}
