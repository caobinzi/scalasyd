import cats._
import data._
import cats.effect._
import org.atnos.eff._
import cats.implicits._
import org.atnos.eff._, interpret._
import scala.concurrent._
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import org.atnos.eff.addon.cats.effect.IOEffect._

object IOHelper {

  implicit class SideEffectHelper[F[_], G[_], R, A, U](effects: Eff[R, A]) {

    def runIO(haha: F ~> G)(
        implicit m: Member.Aux[F, R, U],
        io:         MemberIn[G, U]
    ): Eff[U, A] = {
      translate(effects)(new Translate[F, U] {
        def apply[X](ax: F[X]): Eff[U, X] =
          Eff.send(haha(ax))
      })
    }

  }

}
