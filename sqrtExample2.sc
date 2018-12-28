def average(a: Float, b: Float): Float = (a + b) / 2

def square(n: Float) = n * n 

def improve(n: Float)(guess: Float): Float = average(guess, n / guess) 

def abs(a: Float): Float = if(a < 0) a * -1 else a 

abs(-5) 

def goodEnough(n: Float)(guess: Float): Boolean = abs(square(guess) - n) < 0.0000001 

@tailrec
def tryIt(n: Float)(guess: Float): Float = {
    val goodEnoughN: Float => Boolean = goodEnough(n)
    val tryItNExecution: Float => Float = tryIt(n).compose(improve(n))
    if(goodEnoughN(guess)) guess
    else tryItNExecution(guess)
}

def sqrt(n: Float): Float = {
    val tryItN = tryIt(n)
    tryItN(1)
}