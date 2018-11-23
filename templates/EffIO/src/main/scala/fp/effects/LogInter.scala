package fp.effects
import cats._
import cats.effect.IO

import fp.data._

object LogIter {

  val ioNt = new (LogOp ~> IO) {

    def apply[A](fa: LogOp[A]): IO[A] =
      fa match {
        case Info(s)  => IO(println(s"INFO: ${s}"))
        case Warn(s)  => IO(println(s"WARN: ${s}"))
        case Debug(s) => IO(println(s"DEBUG: ${s}"))
        case Error(s) => IO(println(s"ERROR: ${s}"))
      }
  }

  val evalNt = new (LogOp ~> Eval) {

    def apply[A](fa: LogOp[A]): Eval[A] =
      fa match {
        case Info(s)  => Eval.now(println(s"TEST_INFO: ${s}"))
        case Warn(s)  => Eval.now(println(s"TEST_WARN: ${s}"))
        case Debug(s) => Eval.now(println(s"TEST_DEBUG: ${s}"))
        case Error(s) => Eval.now(println(s"TEST_ERROR: ${s}"))

      }

  }

}
