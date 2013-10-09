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
class PatternParsersTest extends Specification {
  case class Point(x: Int, y: Int)
  "<x='1'>" should {
    "match Seq(Point(1, 2))" in {
      val parser = PatternParsers.reflection[Point]
      val regex = parser("<x='1'>")
      regex.matches(Seq(Point(1, 2))) must beTrue
      regex.matches(Seq(Point(2, 2))) must beFalse
    }
  }

  "<x=/1/>" should {
    "match Seq(Point(1, 2))" in {
      val parser = PatternParsers.reflection[Point]
      val regex = parser("<x=/1/>")
      regex.matches(Seq(Point(1, 2))) must beTrue
      regex.matches(Seq(Point(2, 2))) must beFalse
    }
  }

  "<x=/[0-9]/>" should {
    "match Seq(Point(1, 2))" in {
      val parser = PatternParsers.reflection[Point]
      val regex = parser("<x=/[0-9]/>")
      regex.matches(Seq(Point(1, 2))) must beTrue
      regex.matches(Seq(Point(2, 2))) must beTrue
      regex.matches(Seq(Point(10, 2))) must beFalse
    }
  }
}
