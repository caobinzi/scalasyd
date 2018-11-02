import cats._
import data._
import org.atnos.eff._
import cats.implicits._
import cats.effect._

object LogIter {

  val nt = new (LogOp ~> IO) {

    def apply[A](fa: LogOp[A]): IO[A] =
      fa match {
        case Info(s)  => IO(println(s))
        case Warn(s)  => IO(println(s))
        case Debug(s) => IO(println(s))
      }
  }

}
