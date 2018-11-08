package fp.effects
import cats._
import cats.effect.IO

import fp.data._

object LogIter {

  val ioNt = new (LogOp ~> IO) {

    def apply[A](fa: LogOp[A]): IO[A] =
      fa match {
        case Info(s)  => IO(println(s))
        case Warn(s)  => IO(println(s))
        case Debug(s) => IO(println(s))
      }
  }

}
