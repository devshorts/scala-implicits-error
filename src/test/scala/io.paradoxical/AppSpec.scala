package io.paradoxical.macrotest

import org.scalatest._

class Tests extends FlatSpec with Matchers {
  "A test" should "run" in {
    implicit object priorityConverter extends Conversion[Long] {
      override def from(a: Long) = "abc"
    }

    MacroImplicit.convert[Long].from(1) shouldEqual "abc"
  }
}
