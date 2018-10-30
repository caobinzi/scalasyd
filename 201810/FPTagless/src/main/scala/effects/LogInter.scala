import cats._
import data._
import org.atnos.eff._
import cats.implicits._
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.concurrent._

object LogIter extends LogOp[Eval] {
  import scala.concurrent.ExecutionContext.Implicits.global

  override def info(s: String) = Eval.later(println(s))

}
