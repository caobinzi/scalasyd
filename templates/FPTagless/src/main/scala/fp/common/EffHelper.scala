package fp.common
import cats._
import data._
import org.atnos.eff._
import org.atnos.eff.all._
import org.atnos.eff.syntax.all._
import org.atnos.eff.interpret._

object EffHelper {

  implicit def liftEff[A, F[_], R: F |= ?](s: F[A]): Eff[R, A] = {
    Eff.send[F, R, A](s)
  }

  implicit class EffectHelper[F[_], G[_], R, A, U](effects: Eff[R, A]) {

    def runEffect(nt: F ~> G)(
        implicit m:   Member.Aux[F, R, U],
        io:           MemberIn[G, U]
    ): Eff[U, A] = {
      translate(effects)(new Translate[F, U] {
        def apply[X](ax: F[X]): Eff[U, X] =
          Eff.send(nt(ax))
      })
    }

  }

}
