package akskscala.no28

import unfiltered.filter.Plan
import unfiltered.request._
import unfiltered.response._
import unfiltered.jetty.Http

/**
 * User entity
 */
case class User(id: String, name: String)

/**
 * Datastore
 */
object Users {

  val users = Seq(
    User("1", "Andy"),
    User("2", "Brian"),
    User("3", "Charley"),
    User("4", "Denis"),
    User("5", "Eric"),
    User("6", "Freed"),
    User("7", "George"),
    User("8", "Henry"),
    User("9", "Iris"),
    User("10", "Jack"),
    User("11", "Keith"),
    User("12", "Leah"),
    User("13", "Mick"),
    User("14", "Noel")
  )

  def getAllIds(): Seq[String] = {
    users map {
      case (user: User) => user.id
    }
  }

  def getName(id: String): Option[String] = {
    users find ((user: User) => user.id == id) map ((user: User) => user.name)
  }

}

/**
 * Unfiltered plan
 */
class BlockingServerPlan extends Plan {

  def intent = {
    case req@GET(Path("/ids")) => {
      println("/ids accepted.")
      val csv = Users.getAllIds().mkString(",")
      Thread.sleep(1000L) // blocking operation...
      ResponseString(csv)
    }
    case req@GET(Path("/names")) => {
      val Params(params) = req
      val id = params("id").head
      println("/names?id=" + id + " accepted.")
      Thread.sleep(1000L) // blocking operation...
      ResponseString(Users.getName(id).getOrElse(""))
    }
  }

}

/**
 * Unfiltered server
 */
class BlockingServer(port: Int = 8888) {

  val server: Http = unfiltered.jetty.Http.local(port).plan(new BlockingServerPlan)

  def start() = {
    server.start()
    println("BlockingServer(" + port + ") started.")
  }

  def interactive() = {
    server.run {
      s => unfiltered.util.Browser.open("http://127.0.0.1:%d/ids".format(s.port))
    }
  }

  def stop() = {
    server.stop()
    println("BlockingServer(" + port + ") stopped.")
  }

}