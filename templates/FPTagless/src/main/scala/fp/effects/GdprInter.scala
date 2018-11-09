package fp.effects

import scala.concurrent._
import cats.effect.IO
import cats.data.NonEmptyList
import cats.syntax.all._
import fp.data._

object GdprIter extends GdprOp[IO] {

  import scala.concurrent.ExecutionContext.Implicits.global
  import fp.common.IOHelper._

  override def deleteUser(id: Int): IO[UserData] =
    for {
      info <- retrieveUser(id)
      _    <- NonEmptyList.of(deleteUserEmail(id), deleteUserAddress(id)).parSequence

    } yield info

  override def deleteUserEmail(id: Int) =
    Future {
      (1 to 10).foreach { x =>
        println(s"Deleting user email for ${id} ");
        Thread.sleep(1000)

      }
    }
  override def deleteUserAddress(id: Int) =
    Future {
      (1 to 10).foreach { x =>
        println(s"Deleting user Address for ${id} ");
        Thread.sleep(1000)
      }
    }

  override def retrieveUser(id: Int) =
    Future {

      println(s"retrieve user data for ${id} and sleeping 2 seconds"); Thread.sleep(2000);
      UserData(id, "dummy data")
    }

  override def retrieveUserEmail(id: Int) =
    Future {
      println(s"retrieve user email for ${id} and sleeping 2"); Thread.sleep(2000); "dummyuser@aaaaa.com"
    }

}
