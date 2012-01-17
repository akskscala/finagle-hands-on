package akskscala.no28

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

import scala.util.control.Exception._
import io.Source
import java.net.URL

class InOrderRequestsSuite extends FunSuite with ShouldMatchers {

  test("example") {

    val server = new BlockingServer(8887)
    server.start()
    Thread.sleep(200L) // waiting for the server's starting up

    ultimately {
      server.stop()
    } apply {

      val start = System.currentTimeMillis()
      val ids: Seq[String] = Source.fromURL(new URL("http://127.0.0.1:8887/ids")).mkString.split(",")
      val result = ids map {
        id => Source.fromURL(new URL("http://127.0.0.1:8887/names?id=" + id)).mkString
      }
      val end = System.currentTimeMillis()
      println("in-order(" + ((end - start) / 1000) + "sec):" + result.mkString(","))
    }

  }

}