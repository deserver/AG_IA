/**
 * JMException.java
 * 
 * @author Antonio J. Nebro
 * @version 1.0
 */
package vrptw.spea.util;

import java.io.Serializable;

import vrptw.spea.baseS.Configuration;

/**
 * jmetal exception class
 */
public class JMException extends Exception implements Serializable {
  
  /**
   * Constructor
   * @param Error message
   */
  public JMException (String message){
     super(message);      
  } // JmetalException
}
