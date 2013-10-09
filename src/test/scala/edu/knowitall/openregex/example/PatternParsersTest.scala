package edu.knowitall.openregex.example

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import org.specs2.ScalaCheck
import org.scalacheck.Gen
import org.scalacheck.Prop
import org.scalacheck.Arbitrary

/**
  * Real functionality tests are in openregex itself.
  *
  * @author schmmd
  */
@RunWith(classOf[JUnitRunner])
class PatternParsersTest extends Specification with ScalaCheck {
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

  val pointGen = for {
    n <- Gen.choose(0, 10)
    m <- Gen.choose(0, 10)
  } yield Point(n, m)

  implicit def arbPoint: Arbitrary[List[Point]] = {
    Arbitrary {
      Gen.listOf(pointGen)
    }
  }

  "<x='1' | x='2'>" should {
    "be the same as <x=/[12]/>" in {

      val parser = PatternParsers.reflectionWithLogic[Point]
      val regex1 = parser("<x='1' | x='2'>")
      val regex2 = parser("<x=/[12]/>")

      Prop.forAll(pointGen) { point: Point =>
        regex1(Seq(point)) == regex2(Seq(point))
      }
    }
  }

  "<x='1' | x='2'> <x='3' & y='3'>" should {
    "work" in {
      val parser = PatternParsers.reflectionWithLogic[Point]
      val regex = parser("<x='1' | x='2'> <x='3' & y='3'>")

      Prop.forAll { points: List[Point] =>
        regex.matches(points) ||
          !(points.size == 2 &&
          (points.head.x == 1 || points.head.x == 2) &&
          points.last.x == 3 && points.last.y == 3)
      }
    }
  }
}
