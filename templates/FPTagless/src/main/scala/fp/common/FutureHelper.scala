package fp.common
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

object FutureHelper {

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
