interp.configureCompiler(_.settings.YpartialUnification.value = true)
import $ivy.`org.typelevel::cats-core:1.0.1`, cats.implicits._
import $ivy.`org.typelevel::cats-effect:0.10.1`, cats.effect.IO
import $ivy.`co.fs2::fs2-core:0.10.4`, fs2._, fs2.Stream
import $ivy.`org.http4s::http4s-blaze-server:0.18.14`
import $ivy.`org.http4s::http4s-circe:0.18.14`
import $ivy.`org.http4s::http4s-dsl:0.18.14`
import $ivy.`io.circe::circe-core:0.9.3`
import $ivy.`io.circe::circe-generic:0.9.3`
import $ivy.`io.circe::circe-parser:0.9.3`
import $ivy.`io.taig::circe-validation:0.1.1`
