class TestParser extends munit.FunSuite {
  import $name;format="camel"$.syntax.*
  import Program.*
  import Term.*

  test("example test that succeeds") {
    val obtained =
      $name;format="camel"$.syntax.Parser.parseProgram("let P = a.b; in c.P")
    val expected =
      System(Map("P" -> Prefix("a", Prefix("b", End))), Prefix("c", Proc("P")), None)
    assertEquals(obtained, expected)
  }
}