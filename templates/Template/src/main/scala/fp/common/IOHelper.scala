package fp.common
import cats._
import data._
import cats.effect.IO

import scala.concurrent._

object IOHelper {

  //Make Future an IO implicitly
  implicit def fromScalaFuture[A](f: => Future[A]): IO[A] = {
    IO.fromFuture(IO(f))
  }
}
