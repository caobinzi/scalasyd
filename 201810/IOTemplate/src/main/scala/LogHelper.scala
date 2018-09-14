import cats._
import cats.implicits._
import data._
import org.atnos.eff._
import org.atnos.eff.all._
import org.atnos.eff.interpret._
import org.atnos.eff.syntax.all._

object LogHelper {
  implicit class LogTimesOps[R, A](e: Eff[R, A]) {

    def logTimes[T[_]](
        implicit
        memberT: MemberInOut[T, R],
        log:     MemberIn[LogOp, R]
    ): Eff[R, A] =
      LogHelper.logTimes[R, T, A](e)
  }

  def logTimes[R, T[_], A](eff: Eff[R, A])(
      implicit
      memberT: MemberInOut[T, R],
      eval:    MemberIn[LogOp, R]
  ): Eff[R, A] = {

    translateInto(eff)(new Translate[T, R] {
      def apply[X](tx: T[X]): Eff[R, X] =
        (for {
          _ <- send(Info(s"${new java.util.Date} >> $tx start"))
          x <- send[T, R, X](tx)
          _ <- send(Info(s"${new java.util.Date} << $tx end"))
        } yield x)
    })
  }

}
