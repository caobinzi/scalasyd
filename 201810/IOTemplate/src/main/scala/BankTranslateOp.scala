import org.atnos.eff._
import org.atnos.eff.all._
import cats.implicits._
import org.atnos.eff._, interpret._

object BankOpTranslate {

  type _bankOp[R] = BankOp |= R
  def purchase(bill: String, card: String) = "Ok".some
  def check(bill:    String) = bill === "1234"

  implicit class Bank[R, U, A](effect: Eff[R, A]) {

    def runBank[U: _eval](implicit sr: Member.Aux[BankOp, R, U]) =
      translate(effect)(new Translate[BankOp, U] {

        def apply[A](fa: BankOp[A]): Eff[U, A] =
          fa match {
            case Purchase(bill, card) => now(purchase(bill, card))
            case Refund(bill, card)   => now("Ok".some)
          }

      })

  }

}
