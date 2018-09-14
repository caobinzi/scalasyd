import cats._
import data._
import org.atnos.eff._
import cats.implicits._
import org.atnos.eff._, interpret._

sealed trait BankOp[A]

case class Purchase(bill: String, card: String) extends BankOp[Option[String]]
case class Refund(bill:   String, card: String) extends BankOp[Option[String]]

object BankOp {

  type _bankOp[R] = BankOp |= R
  def purchase(bill: String, card: String) = "Ok".some
  def check(bill:    String) = bill === "1234"

  implicit val nt = new (BankOp ~> Id) {

    def apply[A](fa: BankOp[A]): Id[A] =
      fa match {
        case Purchase(bill, card) => purchase(bill, card)
        case Refund(bill, card)   => "Ok".some
      }
  }

  def runUnSafe[T[_], R, A, U](
      effects: Eff[R, A]
  )(
      implicit m: Member.Aux[T, R, U],
      haha:       T ~> Id
  ): Eff[U, A] = {

    val sideEffect = new SideEffect[T] {
      def apply[X](fsc: T[X]): X = haha(fsc)

      def applicative[X, Tr[_]: Traverse](ms: Tr[T[X]]): Tr[X] =
        ms.map(apply)
    }
    Interpret.interpretUnsafe(effects)(sideEffect)(m)
  }
}
