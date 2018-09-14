import cats._
import data._
import org.atnos.eff._
import cats.implicits._
sealed trait IvrOp[A]

sealed trait Result
case object Continue extends Result
case object RequestAgain extends Result
case object Stop extends Result

case class Request(prompt: String) extends IvrOp[String]
case class Response(msg:   String) extends IvrOp[Unit]
case class CheckInput(msg: String) extends IvrOp[Result]

object IvrOp {
  type _ivrOp[R] = IvrOp |= R
}
