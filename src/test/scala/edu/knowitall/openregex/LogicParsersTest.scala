package edu.knowitall.openregex

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

import edu.washington.cs.knowitall.regex.Expression.BaseExpression
import edu.washington.cs.knowitall.logic.Expression.Arg

/**
  * Real functionality tests are in openregex itself.
  *
  * @author schmmd
  */
@RunWith(classOf[JUnitRunner])
class LogicParsersTest extends Specification {
  case class Point(x: Int, y: Int)
  "x='1'" should {
    "match Point(1, 2)" in {
      val parser = LogicParsers.reflection[Point]
      val logic = parser("x='1'")
      logic(Point(1, 2)) must beTrue
      logic(Point(2, 3)) must beFalse
    }
  }

  "x='1' | x='2'" should {
    "match Point(1, 2)" in {
      val parser = LogicParsers.reflection[Point]
      val logic = parser("x='1' | x='2'")
      logic(Point(1, 2)) must beTrue
      logic(Point(2, 3)) must beTrue
      logic(Point(3, 4)) must beFalse
    }
  }
}