package util;

import java.util.Stack;

public class TwoStack<E> extends Stack<E> {
    /* Atomically pushes two elements */
    public void pushTwo(E first, E second) {
        push(first);
        push(second);
    }
}