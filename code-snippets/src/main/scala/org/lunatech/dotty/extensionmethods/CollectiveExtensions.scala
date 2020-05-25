package org.lunatech.dotty.extensionmethods

    extension on (i: Int) {
      def isEven: Boolean = i % 2 == 0
    
      def unary_! : Int = -i

      def square : Int = i * i
    }

    extension listOps on [T](xs: List[T]) {
      def second: T = xs.tail.head
      def third: T = xs.tail.second
    }
