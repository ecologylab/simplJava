

/**
 * Title:        Queue
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      Hazmat Industries
 * @author Matt Bennett
 * @version 1.0
 */

package ecologylab.sensors.gps;
import java.lang.StringBuffer;

class Queue {
  private final int QUEUESIZE = 5;
  private final int BUFFERSIZE = 95;
  private int inQueue;
  private int outQueue;
  private boolean queueFull;
  private StringBuffer[] q;

  public Queue() {
    q = new StringBuffer[QUEUESIZE];
    inQueue = 0;
    outQueue = 0;
    queueFull = false;
  }

  public boolean queueFull() {
    return queueFull;
  }

  public boolean queueEmpty() {
    return (boolean) (inQueue == outQueue);
  }

  public synchronized void push(String o) {
    //append the string to the end of the list
    if (((inQueue + 1) % QUEUESIZE) == outQueue) {
      queueFull = true;
    } else {
      inQueue = ((inQueue + 1) % QUEUESIZE);
      q[inQueue] = new StringBuffer(o);
      this.notify(); // tell the waiting threads that the data is ready
    }
  }


  public synchronized String pop() {
    while(inQueue == outQueue) { // queue is empty
      try { this.wait(); }
      catch (InterruptedException e) {
        //* Ignore this exception
      }
    }
    outQueue = ((outQueue + 1) % QUEUESIZE);
    queueFull = false;
//    System.out.println("The outQueue pointer is:" + outQueue);
    return q[outQueue].toString();
  }

  /*
   * Java 1.2, which the tini doesn't support.
  LinkedList q = new LinkedList();  // Where objects are stored
  public synchronized void push(Object o) {
    q.add(o); //append the object to the end of the list
    this.notify(); // tell the waiting threads that the data is ready
  }
  public synchronized Object pop() {
    while(q.size() == 0) {
      try { this.wait(); }
      catch (InterruptedException e) {
        //* Ignore this exception
      }
    }
    return q.remove(0);
  }*/
}
