import scala.language.higherKinds
import scala.language.implicitConversions
import cats._
import cats.implicits._
import data._
import org.atnos.eff._
import org.atnos.eff.all._
import org.atnos.eff.interpret._
import org.atnos.eff.syntax.all._

object EffBasicApp extends App {

  import org.atnos.eff._
  import IvrOp._
  import BillOp._
  import BankOp._
  import EffHelper._
  import LogHelper._

  def program[R: _ivrOp: _billOp: _bankOp]: Eff[R, Unit] =
    for {
      bill      <- Request("Please type in your bill reference ")
      _         <- Response(s"Your bill reference: ${bill}")
      card      <- Request("Please type in your credit card info ")
      _         <- Response(s"Your credit card is : ${card}, we are processing now")
      reference <- Purchase(bill, card)
      receipt   <- UpdateBill(bill, "Paid")
      _         <- Response(s"Your payment refrence is ${receipt}")
    } yield ()

  type Stack = Fx.fx3[IvrOp, BillOp, BankOp]
  program[Stack].runBill.runIvr.runBank.run

  // Please type in your bill reference
  //1
  //Your bill reference: 1
  //Please type in your credit card info
  //2
  //Your credit card is : 2, we are processing now
  //Your payment refrence is Some(Ok)

}
