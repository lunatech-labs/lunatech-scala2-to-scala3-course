package org.lunatechlabs.dotty.sudoku

import scala.language.implicitConversions

class ReductionRuleSuite extends munit.FunSuite with SudokuTestHelpers {
  test("Applying reduction rules should eliminate values in isolated complete sets from occurrences in other cells (First reduction rule)") {
    /**
     * Note: test data in this test is a "ReductionSet": is consists of 9 cells (e.g. in a
     *       row or column or 3x3 block). Each cell is represented by 9 characters: a space
     *       encodes a number not present in the cell, characters '1' through '9' encode the
     *       presence of that digit in the cell.
     */
    val input = stringToReductionSet(Vector(
      "12345678 ",
      "1        ", // 1: Isolated & complete
      "   4     ", // 4: Isolated & complete
      "12 45678 ",
      "      78 ", // (7,8): Isolated & complete
      "       89",
      "      78 ", // (7,8): Isolated & complete
      "     6789",
      " 23   78 "
    ))

    val reducedInput1 = stringToReductionSet(Vector(
      " 23 56   ",
      "1        ",
      "   4     ",
      " 2  56   ",
      "      78 ",
      "        9",
      "      78 ",
      "     6  9",
      " 23      "
    ))

    assertEquals(applyReductionRules(input), reducedInput1)


    // After first reduction round, 9 is isolated & complete
    val reducedInput2 = stringToReductionSet(Vector(
      " 23 56   ",
      "1        ",
      "   4     ",
      " 2  56   ",
      "      78 ",
      "        9",
      "      78 ",
      "     6   ",
      " 23      "
    ))

    assertEquals(applyReductionRules(reducedInput1), reducedInput2)

    // After second reduction round, 6 is isolated & complete
    val reducedInput3 = stringToReductionSet(Vector(
      " 23 5    ",
      "1        ",
      "   4     ",
      " 2  5    ",
      "      78 ",
      "        9",
      "      78 ",
      "     6   ",
      " 23      "
    ))

    assertEquals(applyReductionRules(reducedInput2), reducedInput3)

    // reducing again should give same result
    assertEquals(applyReductionRules(reducedInput3), reducedInput3)
  }


  test("Applying reduction rules should eliminate values in isolated complete sets of 5 values from occurrences in other cells (First reduction rule)") {
    val input = stringToReductionSet(Vector(
      "12345    ", // (1,2,3,4,5): Isolated & complete
      "1    67  ",
      "12345    ", // (1,2,3,4,5): Isolated & complete
      "12345    ", // (1,2,3,4,5): Isolated & complete
      " 2    78 ",
      "12345    ", // (1,2,3,4,5): Isolated & complete
      "  3    89",
      "12345    ", // (1,2,3,4,5): Isolated & complete
      "1 3  6  9"
    ))

    val reducedInput = stringToReductionSet(Vector(
      "12345    ",
      "     67  ",
      "12345    ",
      "12345    ",
      "      78 ",
      "12345    ",
      "       89",
      "12345    ",
      "     6  9"
    ))

    assertEquals(applyReductionRules(input), reducedInput)

  }

  test("Applying reduction rules should eliminate values in 2 isolated complete sets of 3 values from occurrences in other cells (First reduction rule)") {
    val input = stringToReductionSet(Vector(
      "123      ",
      "123      ",
      "123      ",
      "   456   ",
      "   456   ",
      "   456   ",
      "12345678 ",
      "123456 89",
      "1234567 9"
    ))

    val reducedInput = stringToReductionSet(Vector(
      "123      ",
      "123      ",
      "123      ",
      "   456   ",
      "   456   ",
      "   456   ",
      "      78 ",
      "       89",
      "      7 9"
    ))

    assertEquals(applyReductionRules(input), reducedInput)
  }

  test("Applying reduction rules should eliminate values in shadowed complete sets from occurrences in same cells (Second reduction rule)") {
    val input = stringToReductionSet(Vector(
      "12  5 789", // (1,2,7,8) shadowed & complete
      "  345    ", // (3,4)     shadowed & complete
      "12  5678 ", // (1,2,7,8) shadowed & complete
      "    56  9",
      "    56  9",
      "12    789", // (1,2,7,8) shadowed & complete
      "  3456  9", // (3,4)     shadowed & complete
      "12   6789", // (1,2,7,8) shadowed & complete
      "        9"
    ))

    val reducedInput1 = stringToReductionSet(Vector(
      "12    78 ",
      "  34     ",
      "12    78 ",
      "    56   ",
      "    56   ",
      "12    78 ",
      "  34     ",
      "12    78 ",
      "        9"
    ))

    assertEquals(applyReductionRules(input), reducedInput1)

    // reducing again gives same result
    assertEquals(applyReductionRules(reducedInput1), reducedInput1)
  }

  test("Applying reduction rules should eliminate values in shadowed complete (6 value) sets from occurrences in same cells (Second reduction rule)") {
    val input = stringToReductionSet(Vector(
      "123456 89", // (1,2,3,4,5,6) shadowed & complete
      "      78 ",
      "12345678 ", // (1,2,3,4,5,6) shadowed & complete
      "123456789", // (1,2,3,4,5,6) shadowed & complete
      "123456 8 ", // (1,2,3,4,5,6) shadowed & complete
      "       89",
      "123456 8 ", // (1,2,3,4,5,6) shadowed & complete
      "      7 9",
      "12345678 "  // (1,2,3,4,5,6) shadowed & complete
    ))

    val reducedInput = stringToReductionSet(Vector(
      "123456   ",
      "      78 ",
      "123456   ",
      "123456   ",
      "123456   ",
      "       89",
      "123456   ",
      "      7 9",
      "123456   "
    ))

    assertEquals(applyReductionRules(input), reducedInput)
  }
}
