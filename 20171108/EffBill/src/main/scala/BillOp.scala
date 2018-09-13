import scala.language.higherKinds
import scala.language.implicitConversions
import cats._
import data._
import org.atnos.eff._
import org.atnos.eff.all._
import org.atnos.eff.syntax.all._
import cats.implicits._
import org.atnos.eff._, interpret._

sealed trait BillOp[A]

case class CheckBill(bill: String) extends BillOp[Boolean]

case class UpdateBill(bill: String, status: String) extends BillOp[Option[String]]

object BillOp {
  import org.atnos.eff._

  type _billOp[R] = BillOp |= R
  def runBillOp[R, A](effect: Eff[R, A])(implicit m: BillOp <= R): Eff[m.Out, A] = {
    val memDataSet = new scala.collection.mutable.ListBuffer[String]

    recurse(effect)(new Recurser[BillOp, m.Out, A, A] {
      def onPure(a:     A): A = a
      def payBill(bill: String, card: String) = "Ok".some
      def check(bill:   String) = bill === "1234"

      def onEffect[X](i: BillOp[X]): X Either Eff[m.Out, A] = Left {
        i match {
          case UpdateBill(bill, card) => payBill(bill, card)
          case CheckBill(bill)        => check(bill)
        }
      }

      def onApplicative[X, T[_]: Traverse](ms: T[BillOp[X]]): T[X] Either BillOp[T[X]] =
        Left(ms.map {
          case UpdateBill(bill, card) => payBill(bill, card)
          case CheckBill(bill)        => check(bill)
        })
    })(m)
  }

  implicit class Bill[R, A](effect: Eff[R, A]) {
    def runBill(implicit m:         BillOp <= R): Eff[m.Out, A] =
      runBillOp[R, A](effect)

  }
}
