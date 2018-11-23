package fp.data
import org.atnos.eff._

trait UserOp[F]
case class SendUserData(email: String, data: UserData) extends UserOp[Boolean]

object UserOp {
  type _userOp[R] = UserOp |= R
}
