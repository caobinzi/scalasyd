import cats._
import data._
import org.atnos.eff._
import org.atnos.eff.all._
import org.atnos.eff.syntax.all._
import org.atnos.eff.interpret._
import cats.effect.IO
import org.atnos.eff.addon.cats.effect.IOEffect._
import org.atnos.eff.syntax.addon.cats.effect._

object EffHelper {
  implicit def liftEff[A, F[_], R: F |= ?](s: F[A]): Eff[R, A] = {
    Eff.send[F, R, A](s)
  }

}
