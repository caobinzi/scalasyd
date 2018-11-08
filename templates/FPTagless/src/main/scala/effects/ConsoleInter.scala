import cats._
import data._

object ConsoleIter extends ConsoleOp[Eval] {

  override def printStrLn(s: String) = Eval.now(println(s))

}
