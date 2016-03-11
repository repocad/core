package com.repocad.web

import com.repocad.util.{Paper, PaperA, PaperA$}
import org.scalajs.dom.CanvasRenderingContext2D
import org.scalajs.dom.raw.HTMLCanvasElement
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

class CanvasPrinterTest extends FlatSpec with Matchers with MockFactory with BeforeAndAfter {

  val printer = new CanvasPrinter(mockedCanvas)

  before {
    printer.prepare()
  }

  "A canvas printer" should "initialise with a zero paper" in {
    printer.paper should equal(Paper(0, 0, 0, 0))
  }
  it should "increase paper scale" in {
    printer.line(0, 0, 10, 10)
    printer.paper should equal(Paper(0, 0, 10, 10))
  }
  it should "decrease paper" in {
    printer.prepare()
    printer.line(0, 0, 5, 5)
    printer.paper should equal(Paper(0, 0, 5, 5))
  }

}