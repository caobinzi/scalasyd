package fp.effects
import cats.Eval
import fp.data.ConsoleOp

object ConsoleIter extends ConsoleOp[Eval] {

  override def printStrLn(s: String) = Eval.now(println(s))

}
