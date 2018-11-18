package fp.common
import scala.concurrent._
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

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
