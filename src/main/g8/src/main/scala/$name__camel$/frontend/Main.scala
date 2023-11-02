package $name;format="camel"$.frontend

import caos.frontend.Site.initSite
import $name;format="camel"$.syntax.Program
import $name;format="camel"$.syntax.Program.System

/** Main function called by ScalaJS' compiled javascript when loading. */
object Main {
  def main(args: Array[String]):Unit =
    initSite[System](CaosConfig)
}