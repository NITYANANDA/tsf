/**
 * Copyright (C) 2011 Talend Inc. - www.talend.com
 */
package client;

import org.example.contract.doubleit.DoubleItPortType;
import org.example.contract.doubleit.DoubleItService;

public class WSClient {
   public static void main(String[] args) {
      DoubleItService service = new DoubleItService();

      // UsernameToken port
      DoubleItPortType utPort = service.getDoubleItPortUT();
      doubleIt(utPort, 10);
      
      // X.509 port
      DoubleItPortType x509Port = service.getDoubleItPortX509();
      doubleIt(x509Port, 15);
   }

   public static void doubleIt(DoubleItPortType port, int numToDouble) {
      int resp = port.doubleIt(numToDouble);
      System.out.println("The number " + numToDouble + " doubled is " + resp);
   }
}
