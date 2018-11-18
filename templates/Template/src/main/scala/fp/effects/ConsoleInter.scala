package fp.effects
import cats.Eval
import fp.data._
import cats.effect.IO
import cats.syntax.all._
import cats._

object ConsoleIter {

  def printStrLn(s: String) = println(s)

  val ioNt = new (ConsoleOp ~> IO) {

    def apply[A](fa: ConsoleOp[A]): IO[A] =
      fa match {
        case PrintStrLn(s) => IO(println(s))
      }
  }

  val evalNt = new (ConsoleOp ~> Eval) {

    def apply[A](fa: ConsoleOp[A]): Eval[A] =
      fa match {
        case PrintStrLn(s) => Eval.now(println(s))
      }
  }

}
