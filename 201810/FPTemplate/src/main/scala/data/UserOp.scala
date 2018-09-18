import org.atnos.eff._
import cats._
import cats.data._
import cats.syntax.all._
import cats.effect.IO
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.concurrent._

sealed trait UserOp[A]

case class SendUserData(data: UserData) extends UserOp[IO[Boolean]]

object UserOp {

  type _userOp[R] = UserOp |= R
  import scala.concurrent.ExecutionContext.Implicits.global

  def uploadUser(data: UserData) = Future {
    Thread.sleep(1000)
    println(s"Updating Data now:${data}")
    true
  }

  val ntTask = new (UserOp ~> Id) {

    def apply[A](fa: UserOp[A]): A =
      fa match {
        case SendUserData(data) =>
          IO.fromFuture(IO(uploadUser(data)))
      }
  }
}
