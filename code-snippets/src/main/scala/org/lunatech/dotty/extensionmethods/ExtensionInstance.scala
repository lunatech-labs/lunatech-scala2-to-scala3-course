package org.lunatech.dotty.extensionmethods

extension intOps {
    def (i: Int).isEven: Boolean = i % 2 == 0
    
    def (i: Int).unary_! : Int = -i

    def (x: Int).square : Int = x * x
}


@main def test: Unit = {
    println(4.isEven)

    println(3.square)
}

// given intOps as AnyRef {
//     def (i: Int).isEven: Boolean = i % 2 == 0
    
//     def (i: Int).unary_! : Int = -i

//     def (x: Int).square : Int = x * x
// }
