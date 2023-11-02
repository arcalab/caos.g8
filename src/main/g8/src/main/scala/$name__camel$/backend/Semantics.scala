package $name;format="camel"$.backend

import caos.sos.SOS
import $name;format="camel"$.backend.Semantics.St
import $name;format="camel"$.syntax.Program
import $name;format="camel"$.syntax.Program.*
import $name;format="camel"$.syntax.Program.Term.*

/** Small-step semantics for both commands and boolean+integer expressions.  */
object Semantics extends SOS[String,St]:

  type St = System

  /** What are the set of possible evolutions (label and new state) */
  def next[A>:String](st: St): Set[(A, St)] = st.main match
    case End => Set()
    case Proc(p) => next(st(st.defs.getOrElse(p,End)))
    case Prefix(act,t) => Set(act -> st(t))
    case Choice(t1,t2) =>
      next(st(t1)) ++ next(st(t2))
    case Par(t1, t2) =>
      val nx1 = next(st(t1))
      val nx2 = next(st(t2))
      (for (n,s)<-nx1 yield n->st(Par(s.main,t2))) ++
      (for (n,s)<-nx2 yield n->st(Par(t1,s.main)))
