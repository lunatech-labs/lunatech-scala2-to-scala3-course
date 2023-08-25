var x = -6
def f(x: Int): Int = x + 1
val xs = Seq(1,2,3)
val ys = Seq(3,4,5)

def body: Int = 6

if x < 0 then
  "negative"
else if x == 0 then
  "zero"
else
  "positive"

if x < 0 then -x else x

while x <= 0 do x = f(x)

println(x)

for x <- xs if x > 0
yield x * x

for
    x <- xs
    y <- ys
do
    println(x + y)
