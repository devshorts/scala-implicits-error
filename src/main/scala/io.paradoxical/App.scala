package io.paradoxical.macrotest

import scala.reflect.macros.blackbox
import com.github.ghik.silencer.silent

/**
 * Bijections example
 */
object AsWrapper {
  implicit def wrapped[A](a: A) = As(a)
}

case class As[A](a: A) {
  implicit def as(implicit c: Conversion[A]): String = c.from(a)
}

/**
 * Typeclass we want to convert to
 * @tparam A
 */
trait Conversion[A] {
  def from(a: A): String
}

/**
 * Some default conversions
 */
trait LowPri {
  implicit val longConvert: Conversion[Long] = new Conversion[Long] {
    override def from(a: Long) = a.toString
  }
}

object DefaultConversions extends LowPri

@silent
object MacroImplicit {
  def convert[A]: Conversion[A] = macro convertImpl[A]

  def convertImpl[A : c.WeakTypeTag](c: blackbox.Context): c.Expr[Conversion[A]] = {
    import c.universe._

    reify {
      {
        import DefaultConversions._
        import AsWrapper._

        new Conversion[A] {
          override def from(m: A): String = {
            createConvertMethod[A](c).splice
          }
        }
      }
    }
  }

  private def createConvertMethod[A](c: blackbox.Context): c.Expr[String] = {
    import c.universe._

    // force an implicit usage
    c.Expr[String](q"m.as")
  }
}
