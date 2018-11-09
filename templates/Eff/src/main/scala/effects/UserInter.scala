import cats._
import data._
import org.atnos.eff._
import cats.implicits._
import cats.effect.IO
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.concurrent._

object UserIter {
  import scala.concurrent.ExecutionContext.Implicits.global

  def uploadUser(data: UserData) = Future {
    Thread.sleep(10000)
    println(s"Updating Data now:${data}")
    true
  }

  val nt = new (UserOp ~> Id) {

    def apply[A](fa: UserOp[A]): A =
      fa match {
        case SendUserData(data) =>
          IO.fromFuture(IO(uploadUser(data)))
      }
  }
}
