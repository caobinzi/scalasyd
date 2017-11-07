import scala.language.higherKinds
import scala.language.implicitConversions
import cats._
import data._
import org.atnos.eff._
import org.atnos.eff.syntax.all._
import org.atnos.eff.all._

import cats.implicits._
import org.atnos.eff._, interpret._

sealed trait IvrOp[A]

sealed trait Result
case object Continue extends Result
case object RequestAgain extends Result
case object Stop extends Result

case class Request(prompt: String) extends IvrOp[String]
case class Response(msg:   String) extends IvrOp[Unit]
case class CheckInput(msg:  String) extends IvrOp[Result]

object IvrOp {
  import org.atnos.eff._
  type _ivrOp[R]    = IvrOp |= R
  type WriterString[A] = Writer[String, A]
  def myDate = new java.util.Date
  def readLine(): String = scala.io.StdIn.readLine()

  def runIvrOp[R, A](effect: Eff[R, A])(implicit m: IvrOp <= R): Eff[m.Out, A] =
    recurse(effect)(new Recurser[IvrOp, m.Out, A, A] {
      def onPure(a: A): A = a
      def ask(s:    String): String = {
        println(s)
        Thread.sleep(2000)
        readLine()
      }
      def tell(msg: String) = println(msg)

      def check(msg: String): Result = msg match {
        case "0" => Stop
        case "1" => RequestAgain
        case _   => Continue
      }

      def onEffect[X](i: IvrOp[X]): X Either Eff[m.Out, A] =
        i match {
          case Request(prompt) => Left(ask(prompt))
          case Response(msg)   => Left(tell(msg))
          case CheckInput(msg)  => Left(check(msg))
        }

      def onApplicative[X, T[_]: Traverse](ms: T[IvrOp[X]]): T[X] Either IvrOp[T[X]] =
        Left(ms.map {
          case Request(prompt) => ask(prompt)
          case Response(msg)   => tell(msg)
          case CheckInput(msg)  => check(msg)
        })

    })(m)

  implicit class Ivr[R, A](effect: Eff[R, A]) {
    def runIvr(implicit m:         IvrOp <= R): Eff[m.Out, A] =
      runIvrOp[R, A](effect)
  }

}
