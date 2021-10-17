# Development decisions

## Introduction
This file contains explanations of some development decisions.

### Streams
In most cases for loops are used instead of streams. It is because during testing stream implementations were slower 
than those using for loops.

e.g. function
```
void repeat(int count, Runnable action) {
    IntStream.range(0, count).forEach(i -> action.run());
}
```
can replace for loop and make code more readable but resulting code is about 15% slower.