import scala.language.higherKinds
import scala.language.implicitConversions
import cats._
import data._
import org.atnos.eff._
import org.atnos.eff.all._
import org.atnos.eff.syntax.all._

object EffHelper {
  implicit def liftEff[A, F[_], R: F |= ?](s: F[A]): Eff[R, A] = {
    Eff.send[F, R, A](s)
  }
}
