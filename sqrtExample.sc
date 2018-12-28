def average(a: Float, b: Float): Float = (a + b) / 2

def square(n: Float) = n * n 

def improve(guess: Float, n: Float): Float = average(guess, n / guess) 

def abs(a: Float): Float = if(a < 0) a * -1 else a 

abs(-5) 

def goodEnough(guess: Float, n: Float): Boolean = abs(square(guess) - n) < 0.0000001 

def tryIt(guess: Float, n: Float): Float = {
    if(goodEnough(guess, n)) guess
    else tryIt(improve(guess, n), n)
}
def sqrt(n: Float): Float = tryIt(1, n)