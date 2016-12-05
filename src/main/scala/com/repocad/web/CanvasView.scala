package com.repocad.web

import com.repocad.geom.{TransformationMatrix, Vector2D}
import com.repocad.view.View
import com.repocad.view.event.{Event, _}
import org.scalajs.dom.MouseEvent
import org.scalajs.dom.raw.HTMLCanvasElement

import scala.scalajs.js.annotation.JSExport

class CanvasView(canvas: HTMLCanvasElement, renderer: CanvasRenderer) extends View {

  private val events: EndlessIterator[Event] = new EndlessIterator[Event]()

  canvas.onmousedown = (e: MouseEvent) => enqueueMouseEvent(e, MouseDown)
  canvas.onmousemove = (e: MouseEvent) => enqueueMouseEvent(e, MouseMove)
  canvas.onmouseleave = (e: MouseEvent) => enqueueMouseEvent(e, MouseLeave)
  canvas.onmouseup = (e: MouseEvent) => enqueueMouseEvent(e, MouseUp)

  canvas.onkeydown = (e: org.scalajs.dom.KeyboardEvent) => enqueueKeyboardEvent(e, KeyDown)
  canvas.onkeyup = (e: org.scalajs.dom.KeyboardEvent) => enqueueKeyboardEvent(e, KeyUp)
  canvas.onkeypress = (e: org.scalajs.dom.KeyboardEvent) => enqueueKeyboardEvent(e, KeyDown)

  private def enqueue(event: Event): Unit = events.enqueue(event)

  private def enqueueKeyboardEvent(e: org.scalajs.dom.KeyboardEvent, f: (String, ModifierKeys) => Event): Unit =
    enqueue(f(e.key, ModifierKeys(e)))

  private def enqueueMouseEvent(e: MouseEvent, f: Vector2D => Event): Unit = {
    //go from screen coordinates to canvas coordinates
    val clientRec = canvas.getBoundingClientRect
    val canvasPos = Vector2D(clientRec.left, clientRec.top)
    val posRelativeToCanvas = Vector2D(e.clientX, e.clientY) - canvasPos

    val posInModelSpace = renderer.canvasToModelTransform.applyToPoint(posRelativeToCanvas)
    enqueue(f(posInModelSpace))
  }

  @JSExport
  def canvasCenter = Vector2D(canvas.width / 2, canvas.height / 2)

  def windowCenter = Vector2D(canvas.getBoundingClientRect().left, canvas.getBoundingClientRect().top) + canvasCenter

  def state: (View, Option[Event]) = {
    (this, events.next)
  }

}
