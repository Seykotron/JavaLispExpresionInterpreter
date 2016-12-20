/*
 * Copyright (C) 2016 Seykotron
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package jalei;

import java.util.HashMap;

/**
 *
 * @author Seykotron
 */
public class Main {

    /*
        EJEMPLO
        -------
    
        Expresion: (OR (NOT VAL1) (AND VAL1 VAL2) )
        Operandos:
        {VAL1=true, VAL2=false}
        Solucion: 
        ----------------------------------------------------------
        [OR, NOT]
        VAL1
        NOT
        Operando el NOT sobre VAL1
        VAL1 vale true
        [OR, false, AND, VAL1]
        VAL2
        VAL1
        AND
        Operando el AND sobre VAL1 y VAL2
        VAL1 vale true
        VAL2 vale false
        [OR, false, false]
        false
        false
        OR
        Operando el OR sobre false y false
        ----------------------------------------------------------
        Resultado: false
    */
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       String lisp = "(OR (NOT VAL1) (AND VAL1 VAL2) )";
       HashMap<String,Object> valores = new HashMap<>();
       valores.put( "VAL1", true );
       valores.put( "VAL2", false );
       
       Jalei jalei = new Jalei( lisp, valores,true );
       
       /*
            Ejemplo sin verbose!
       
            Jalei jalei = new Jalei( lisp, valores );
            System.out.println( "Solucion "+jalei.getResultado() );
       */
       
    }
}
