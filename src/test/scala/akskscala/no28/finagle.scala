package akskscala.no28

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import com.twitter.finagle.builder.ClientBuilder
import com.twitter.finagle.http.Http
import org.jboss.netty.util.CharsetUtil

import scala.util.control.Exception._
import com.twitter.util.Future
import org.jboss.netty.handler.codec.http.{HttpMethod, HttpVersion, DefaultHttpRequest}
import java.lang.String

class UsingFinagleClientSuite extends FunSuite with ShouldMatchers {

  test("example") {

    val server = new BlockingServer(8886)
    server.start()
    Thread.sleep(200L) // waiting for the server's starting up

    ultimately {
      server.stop()
    } apply {

      val service = ClientBuilder()
        .codec(Http())
        .hosts("127.0.0.1:8886")
        .hostConnectionLimit(5)
        .build()

      def request(uri: String) = {
        val req = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, uri)
        req.setHeader("Host", "127.0.0.1")
        req
      }

      val start = System.currentTimeMillis()
      service(request("/ids")) onSuccess {
        response => {
          val ids = response.getContent.toString(CharsetUtil.UTF_8)
          val nameFutures = (ids.split(",") map {
            id: String => service(request("/names?id=" + id))
          }).toList
          val resultFuture = Future.collect(nameFutures) map {
            fs => fs map {
              f => f.getContent.toString(CharsetUtil.UTF_8)
            }
          }
          // val result = resultFuture()
          // println("finagle-client(" + ((end - start) / 1000) + "sec):" + result.mkString(","))
          resultFuture onSuccess {
            result => {
              val end = System.currentTimeMillis()
              println("finagle-client(" + ((end - start) / 1000) + "sec):" + result.mkString(","))
            }
          }
        }

      } onFailure {
        e => e.printStackTrace()
      }
      Thread.sleep(10000L)

    }

  }

}