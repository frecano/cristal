package org.cristal.api.helper

import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.{RequestContext, Route, RouteResult, StandardRoute}
import scala.concurrent.Promise


final class ImperativeRequestContext(ctx: RequestContext, promise: Promise[RouteResult]) {
  private implicit val ec = ctx.executionContext
  def complete(obj: ToResponseMarshallable): Unit = ctx.complete(obj).onComplete(promise.complete)
  def fail(error: Throwable): Unit = ctx.fail(error).onComplete(promise.complete)
}

trait ApiHelper {

  def notImplementedResponse(msg: String = ""): StandardRoute =
    complete(HttpEntity(ContentTypes.`text/html(UTF-8)`,
      s"<h1>We plan to support this resource soon.</h1>"))

  def imperativelyComplete(inner: ImperativeRequestContext => Unit): Route = { ctx: RequestContext =>
    val p = Promise[RouteResult]()
    inner(new ImperativeRequestContext(ctx, p))
    p.future
  }

}


