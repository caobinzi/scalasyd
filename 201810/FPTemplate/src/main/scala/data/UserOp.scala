import org.atnos.eff._
import cats._
import cats.data._
import cats.syntax.all._
import cats.effect.IO
sealed trait UserOp[A]

case class SendUserData(data: UserData) extends UserOp[IO[Boolean]]

object UserOp {

  type _userOp[R] = UserOp |= R
}
