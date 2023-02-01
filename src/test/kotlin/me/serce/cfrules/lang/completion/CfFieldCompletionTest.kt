package me.serce.cfrules.lang.completion

class CfFieldCompletionTest : CfRulesCompletionTestBase() {
  fun testAllPossibleFields() = checkCompletion(
    hashSetOf(
      "cookie",
      "host",
      "referer",
      "request.full_uri",
    ),
    """
    not ssl
    and http./*caret*/
    """, strict = false
  )

  fun testCompletionInsideAFunction() = checkCompletion(
    hashSetOf(
      "lookup_json_string",
      "lower",
    ),
    """
    not ssl
    and lo/*caret*/(http.host) == "example.com"
    """, strict = true
  )

  fun testCompletionWithANewExpression() = checkCompletion(
    hashSetOf(
      "http.cookie",
      "http.host",
      "http.referer",
//      "any", TODO
//      "lower",
    ),
    """
    not ssl
    and /*caret*/
    """, strict = false
  )

  fun testASpecificFiniteSet() = checkCompletion(
    hashSetOf(
      "passed",
    ),
    """
    cf.bot_management.js_detection./*caret*/
    """, strict = true
  )
}
