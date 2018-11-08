package fp.effects
import cats._
import data._
import fp.data.ConsoleOp

object ConsoleIter extends ConsoleOp[Eval] {

  override def printStrLn(s: String) = Eval.now(println(s))

}
