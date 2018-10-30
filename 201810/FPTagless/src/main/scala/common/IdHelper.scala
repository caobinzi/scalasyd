import cats._
import data._
import cats.implicits._
import org.atnos.eff._
import org.atnos.eff.all._
import org.atnos.eff.syntax.all._

object IdHelper {

  implicit class SideEffectHelper[T[_], R, A, U](effects: Eff[R, A]) {

    def runEffect(haha: T ~> Id)(
        implicit m:     Member.Aux[T, R, U]
    ): Eff[U, A] = {
      val sideEffect = new SideEffect[T] {
        def apply[X](fsc: T[X]): X = haha(fsc)

        def applicative[X, Tr[_]: Traverse](ms: Tr[T[X]]): Tr[X] =
          ms.map(x => apply(x))

      }
      Interpret.interpretUnsafe(effects)(sideEffect)(m)

    }

  }

}
