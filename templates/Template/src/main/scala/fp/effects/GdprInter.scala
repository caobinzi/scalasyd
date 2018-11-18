package fp.effects

import scala.concurrent._
import cats.effect.IO
import cats.data.NonEmptyList
import cats.syntax.all._
import fp.data._
import cats._

object GdprIter extends GdprOp[IO] {

  import scala.concurrent.ExecutionContext.Implicits.global
  import fp.common.IOHelper._

  override val ioNt = new (Gdpr ~> IO) {

    def apply[A](fa: Gdpr[A]): IO[A] =
      fa match {
        case DeleteUser(id)        => deleteUser(id)
        case RetrieveUser(id)      => retrieveUser(id)
        case RetrieveUserEmail(id) => retrieveUserEmail(id)
        case DeleteUserEmail(id)   => deleteUserEmail(id)
        case DeleteUserAddress(id) => deleteUserAddress(id)
      }
  }

  def deleteUser(id: Int): IO[UserData] =
    for {
      info <- retrieveUser(id): IO[UserData]
      _    <- NonEmptyList.of(deleteUserEmail(id), deleteUserAddress(id)).parSequence

    } yield info

  def deleteUserEmail(id: Int): IO[Unit] =
    Future {
      (1 to 10).foreach { x =>
        println(s"Deleting user email for ${id} ");
        Thread.sleep(1000)

      }
    }

  def deleteUserAddress(id: Int): IO[Unit] =
    Future {
      (1 to 10).foreach { x =>
        println(s"Deleting user Address for ${id} ");
        Thread.sleep(1000)
      }
    }

  def retrieveUser(id: Int): IO[UserData] =
    Future {

      println(s"retrieve user data for ${id} and sleeping 2 seconds"); Thread.sleep(2000);
      UserData(id, "dummy data")
    }

  def retrieveUserEmail(id: Int): IO[String] =
    Future {
      println(s"retrieve user email for ${id} and sleeping 2"); Thread.sleep(2000); "dummyuser@aaaaa.com"
    }

}
