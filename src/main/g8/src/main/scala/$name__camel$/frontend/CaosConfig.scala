package $name;format="camel"$.frontend

import caos.frontend.Configurator.*
import caos.frontend.{Configurator, Documentation}
import caos.view.*
import $name;format="camel"$.backend.*
import $name;format="camel"$.syntax.Program.System
import $name;format="camel"$.syntax.{Program, Show}

/** Object used to configure which analysis appear in the browser */
object CaosConfig extends Configurator[System]:
  val name = "Animator of $name$"
  override val languageName: String = "Input program"

  /** Parser, converting a string into a System in $name$ */
  val parser =
    $name;format="camel"$.syntax.Parser.parseProgram

  /** Examples of programs that the user can choose from. The first is the default one. */
  val examples = List(
    "example" -> "let P = a.b.P + c.Q;\n    Q = a.Q + d.P; \nin P" -> "Example of a program in $name$",
    "coffee" -> "let\n CM = coin.coffee.CM;\n CS = pub.coin.coffee.CS;\nin\n CM|CS",
    "a.0" -> "a.0",
    "a.b" -> "a.b",
    "+" -> "a.b + c.d",
    "|" -> "a.b | a",
    "loop" -> "let P = a.b.P;\nin P",
    "bisim-1" -> "a.b+b.a ~ a|b" -> "Simple example to check that 2 processes are bisimilar",
    "bisim-2" -> "a.(b.c+b.d) ~ a.b.(c+d)" -> "Simple example of 2 non-bisimilar processes",
  )

  /** Description of the widgets that appear in the dashboard. */
  val widgets = List(
    "View pretty data" -> view[System](Show.apply, Code("haskell")).moveTo(1),
    "View structure" -> view(Show.mermaid, Mermaid),
     "Run semantics" -> steps(e=>e, Semantics, Show.justTerm, _.toString, Text),
     "Build LTS" -> lts((e:System)=>e, Semantics, Show.justTerm, _.toString).expand,
     "Build LTS (explore)" -> ltsExplore(e=>e, Semantics, x=>Show(x.main), _.toString),
    "Find strong bisimulation (given a program \"A ~ B\")" ->
      compareStrongBisim(Semantics, Semantics,
        (e: System) => System(e.defs, e.main, None),
        (e: System) => System(e.defs, e.toCompare.getOrElse(Program.Term.End), None),
        Show.justTerm, Show.justTerm, _.toString),
  )

  //// Documentation below

  override val footer: String =
    """Simple animator of $name$, meant to exemplify the
      | CAOS libraries, used to generate this website. Source code available online:
      | <a target="_blank" href="https://github.com/arcalab/CAOS">
      | https://github.com/arcalab/CAOS</a> (CAOS).""".stripMargin

  private val sosRules: String =
    """The operational rules that we use to reduce programs are provided below.
      | An extended version can also be found, for example, in the slides available at
      | <a target="_blank" href="https://lmf.di.uminho.pt/CyPhyComp2223/slides/2-behaviour.pdf#page=27">
      | https://lmf.di.uminho.pt/CyPhyComp2223/slides/2-behaviour.pdf#page=27</a>.
      |
      |<pre>
      |  --------------------(act)
      |  label.P --label--> P
      |
      |    P1 --label--> P'
      |  --------------------(sum-1)
      |  P1 + P2 --label-> P'
      |
      |    P2 --label--> P'
      |  --------------------(sum-2)
      |  P1 + P2 --label-> P'
      |
      |      P1 --label--> P'
      |  -------------------------(com-1)
      |  P1 | P2 --label-> P' | P2
      |
      |      P2 --label--> P'
      |  -------------------------(com-2)
      |  P1 | P2 --label-> P1 | P'
      |</pre>""".stripMargin

  override val documentation: Documentation = List(
    languageName -> "More information on the syntax of $name$" ->
      """A program <code>prog</code> in $name$ is given by the following grammar:
        |<pre>
        |  prog ::= term
        |         | "let" (ProcName "=" term ";")* "in" term
        |  term ::= 0
        |         | ProcName
        |         | LabelName
        |         | LabelName "." term
        |         | term "+" term
        |         | term "|" term
        |  compare ::= prog "~" prog
        |</pre>
        |
        |In the input field you can either provide a program `prog` or a comparison of two programs `compare`.
        |The latter is used to check if they are bisimilars, using the corresponding widget.
        |
        |The pseudo-terminal <code>ProcName</code> is a string that starts with an upper-case letter,
        |<code>ActionName</code> is a string that starts with a lower-case letter, and
        |<code>LabelName</code> is an <code>ActionName</code>.
        |""".stripMargin,
    "Build LTS" -> "More information on the operational rules used here" -> sosRules,
    "Build LTS (explore)" -> "More information on the operational rules used here" -> sosRules,
    "Run semantics" -> "More information on the operational rules used here" -> sosRules,
    "Find strong bisimulation (given a program \"A ~ B\")" -> "More information on this widget" ->
      ("<p>When the input consists of the comparison of 2 programs separated by <code>~</code>, this widget " +
        "searches for a (strong) bisimulation between these 2 programs, providing either a " +
        "concrete bisimulation or an explanation of where it failed.</p>" +
        "<p>When only a program is provided, it compares it against the empty process <code>0</code>.</p>"),
  )
