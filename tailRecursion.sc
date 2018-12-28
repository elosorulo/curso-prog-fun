def even(i: Int): Boolean = i match {
  case 0 => true
  case 1 => false
  case _ => even(i - 2)
}

def odd(i: Int): Boolean = !even(i)
