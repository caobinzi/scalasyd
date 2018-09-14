import org.atnos.eff._
import org.atnos.eff.all._
import org.atnos.eff.syntax.all._
import org.atnos.eff.addon.cats.effect.IOEffect._
import org.atnos.eff.syntax.addon.cats.effect._
import cats.implicits._
import cats.effect.IO

object EffApp extends App {

  import IvrOp._
  import BillOp._
  import BankOp._
  import EffHelper._
  import IOHelper._
  import LogOp._
  import LogHelper._

  def program[R: _ivrOp: _billOp: _bankOp: _logOp: _io]: Eff[R, Unit] = {
    for {
      bill        <- Request("Please type in your bill reference ")
      _           <- Info("====haha===")
      _           <- Response(s"Your bill reference: ${bill}")
      card        <- Request("Please type in your credit card info ")
      _           <- Response(s"Your credit card is : ${card}, we are processing now")
      reference   <- Purchase(bill, card)
      receiptTask <- UpdateBill(bill, "Paid")
      checkTask   <- CheckBill(bill)
      result      <- fromIO((receiptTask, checkTask).parMapN((_, _) => "haha"))

      _ <- Response(s"Refrence ${result}")
    } yield ()
  }

  type Stack = Fx.fx5[IvrOp, BillOp, BankOp, LogOp, IO]

  val r = program[Stack]
    .logTimes[IvrOp]
    .logTimes[BillOp]
    .runEffect(IvrIter.nt)
    .runEffect(BankOp.nt)
    .runEffect(LogOp.nt)
    .runEffect(BillOp.ntTask)
    .unsafeRunSync

  println("Getting here")

  //   .runFuture

}
