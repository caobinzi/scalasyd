import cats._
import data._
import org.atnos.eff._
import cats.implicits._
import org.atnos.eff._, interpret._
import scala.concurrent._
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

object IOHelper {

  implicit class SideEffectHelper[T[_], R, A, U](effects: Eff[R, A]) {

    def runEffect(haha: T ~> Id)(
        implicit m:     Member.Aux[T, R, U]
    ): Eff[U, A] = {
      val sideEffect = new SideEffect[T] {
        def apply[X](fsc: T[X]): X = haha(fsc)

        def applicative[X, Tr[_]: Traverse](ms: Tr[T[X]]): Tr[X] =
          ms.map(apply)
      }
      Interpret.interpretUnsafe(effects)(sideEffect)(m)
    }

  }
  implicit class FutureHelperp[A](job: Future[A]) {

    def runFuture: A = {
      import scala.util._
      Await.ready(job, Duration.Inf).value.get match {
        case Success(l) =>
          l
        case Failure(e) =>
          println(s"Erro found: ${e}")
          throw e
      }
    }
  }

}
