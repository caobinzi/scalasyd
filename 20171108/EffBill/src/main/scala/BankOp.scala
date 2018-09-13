import scala.language.higherKinds
import scala.language.implicitConversions
import cats._
import data._
import org.atnos.eff._
import org.atnos.eff.all._
import org.atnos.eff.syntax.all._
import cats.implicits._
import org.atnos.eff._, interpret._

sealed trait BankOp[A]

case class Purchase(bill: String, card: String) extends BankOp[Option[String]]
case class Refund(bill:   String, card: String) extends BankOp[Option[String]]

object BankOp {
  import org.atnos.eff._

  type _bankOp[R] = BankOp |= R
  def purchase[R: _bankOp](bill: String, card: String): Eff[R, Option[String]] =
    Eff.send[BankOp, R, Option[String]](Purchase(bill, card))

  def runBankOp[R, A](effect: Eff[R, A])(implicit m: BankOp <= R): Eff[m.Out, A] = {
    val memDataSet = new scala.collection.mutable.ListBuffer[String]

    recurse(effect)(new Recurser[BankOp, m.Out, A, A] {
      def onPure(a:      A): A = a
      def purchase(bill: String, card: String) = "Ok".some
      def check(bill:    String) = bill === "1234"

      def onEffect[X](i: BankOp[X]): X Either Eff[m.Out, A] = Left {
        i match {
          case Purchase(bill, card) => purchase(bill, card)
          case Refund(bill, card)   => "Ok".some
        }
      }

      def onApplicative[X, T[_]: Traverse](ms: T[BankOp[X]]): T[X] Either BankOp[T[X]] =
        Left(ms.map {
          case Purchase(bill, card) => purchase(bill, card)
          case Refund(bill, card)   => "Ok".some
        })
    })(m)
  }
  implicit class Bank[R, A](effect: Eff[R, A]) {
    def runBank(implicit m:         BankOp <= R): Eff[m.Out, A] =
      runBankOp[R, A](effect)

  }
}
