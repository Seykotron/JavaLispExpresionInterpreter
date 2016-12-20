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

import static jalei.Reversed.reversed;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Seykotron
 */
public class Jalei {
    
    /*
        Estos son los operadores definidos por defecto, se pueden agregar más agregando aquí una entrada y la
        correspondiente acción en el metodo operar()
    */
    private ArrayList<String> operadores = new ArrayList<>( 
            Arrays.asList(
                    "AND",
                    "OR",
                    "NOT",
                    ">",
                    "<",
                    ">=",
                    "<=",
                    "=="
            )
    );

    
    /*
        La pila se usa para ir almacenando los operadores y operandos en el orden en el que van llegando,
        esta pila se gestiona de manera LIFO para resolver la expresion LISP
    
        Es de tipo CopyOnWriteArrayList porque operamos sobre el ArrayList mientras leemos, de otra forma no
        iteraría bien por el
    */
    private CopyOnWriteArrayList<Object> pila = new CopyOnWriteArrayList<>();

    private Object resultado;
    private boolean verbose;
    private String lisp;
    private HashMap<String,Object> valores;
    
    /*
        Constructor de la clase
        por defecto el verbose está desactivado asi que no mostrará por pantalla informacion
    */
    public Jalei( String lisp, HashMap<String,Object> valores ) {
        
        if( lisp==null || lisp.length() == 0 ){
            System.out.println( "La expresion LISP no puede estar vacía." );
            return;
        }
        if( valores==null || valores.isEmpty() ){
            System.out.println( "Los valores de las variables no pueden estar vacíos " );
            return;
        }
        
        //Reinicio la pila
        pila.clear();
        
        this.valores = valores;
        this.lisp = lisp;
        this.verbose = false;
        
        this.resultado = resolver();
    }
    
    /*
        Constructor de la clase
        el verbose se setea para poder activar la salida por pantalla de información
    */
    public Jalei( String lisp, HashMap<String,Object> valores, boolean verbose ) {
        
        if( lisp==null || lisp.length() == 0 ){
            System.out.println( "La expresion LISP no puede estar vacía." );
            return;
        }
        if( valores==null || valores.isEmpty() ){
            System.out.println( "Los valores de las variables no pueden estar vacíos " );
            return;
        }
        
        //Reinicio la pila
        pila.clear();
        
        this.valores = valores;
        this.lisp = lisp;
        this.verbose = verbose;
        
        this.resultado = resolver();
    
    }
    
    /*
        Devuelve el resultado de la operacion LISP si existe
    */
    public Object getResultado(){
        
        return resultado;
    }
    
    public void log( String texto ){
        /*
            Este metodo escribe por pantalla si el verbose esta activado.
            Por defecto el verbose está activado
        */
        if( this.verbose ){
            System.out.println( texto );
        }
    }
    
    /*
        Este metodo opera y agrega a la pila el resultado de la operacion,
        en operNombres esta el nombre de la variable que
        es la key del dict "valores" que se le da al constructor de la clase,
        op es la operación a realizar, y deberá existir en la lista de operadores
        Y la pila es la pila del programa en la que se van guardando las operaciones que se van realizando
    
        OJO las comparaciones >, <, <=, >=, == se harán sobre float, cambiar el casteo si se espera otro tipo de dato.
    */
    public void operar( String op, CopyOnWriteArrayList<Object> operNombres ){
        //saco de la pila el operador
        pila.remove( pila.size()-1 );
        
        //Variables auxiliares
        Boolean val1;
        Boolean val2;
        Object ob1;
        Object ob2;
        
        switch( op ){
            case "AND":
                log( "Operando el AND sobre "+operNombres.get(1)+" y "+operNombres.get(0) );
                //Obtengo los dos valores
                val1 = (Boolean) getValor( operNombres.get(1) );
                val2 = (Boolean) getValor( operNombres.get(0) );
                //Agrego el resultado a la pila
                pila.add( val1 && val2 );
                break;
            case "OR":
                log( "Operando el OR sobre "+operNombres.get(1)+" y "+operNombres.get(0) );
                //Obtengo los dos valores
                val1 = (Boolean) getValor( operNombres.get(1) );
                val2 = (Boolean) getValor( operNombres.get(0) );
                //Agrego el resultado a la pila
                pila.add( val1 || val2 );
                break;
            case "NOT":
                log( "Operando el NOT sobre "+operNombres.get(0) );
                //Obtengo el valor
                val1 = (Boolean) getValor( operNombres.get(0) );
                //Agrego el resultado a la pila
                pila.add( !val1  );
                break;
            case ">":
                log( "Operando el > sobre "+operNombres.get(1)+" y "+operNombres.get(0) );
                //Obtengo los dos valores
                ob1 = getValor( operNombres.get(1) );
                ob2 = getValor( operNombres.get(0) );
                //Agrego el resultado a la pila
                pila.add( (float) ob1 > (float) ob2 );
                break;
            case "<":
                log( "Operando el < sobre "+operNombres.get(1)+" y "+operNombres.get(0) );
                ob1 = getValor( operNombres.get(1) );
                ob2 = getValor( operNombres.get(0) );
                //Agrego el resultado a la pila
                pila.add( (float) ob1 < (float) ob2 );
                break;
            case ">=":
                log( "Operando el >= sobre "+operNombres.get(1)+" y "+operNombres.get(0) );
                ob1 = getValor( operNombres.get(1) );
                ob2 = getValor( operNombres.get(0) );
                //Agrego el resultado a la pila
                pila.add( (float) ob1 >= (float) ob2 );
                break;
            case "<=":
                log( "Operando el <= sobre "+operNombres.get(1)+" y "+operNombres.get(0) );
                ob1 = getValor( operNombres.get(1) );
                ob2 = getValor( operNombres.get(0) );
                //Agrego el resultado a la pila
                pila.add( (float) ob1 <= (float) ob2 );
                break;
            case "==":
                log( "Operando el == sobre "+operNombres.get(1)+" y "+operNombres.get(0) );
                ob1 = getValor( operNombres.get(1) );
                ob2 = getValor( operNombres.get(0) );
                //Agrego el resultado a la pila
                pila.add( (float) ob1 == (float) ob2 );
                break;
            /*
                Agregar aquí el código si se agregan más operadores con nuevas entradas del case
                */
        }
    }
    
    /*
        Si el valor es un boolean true o false, devuelve el valor pasado por parámetro,
        si el valor es un string lo busca en las claves del dict valores y devuelve su
        valor booleano (true o false)
    
        OJO con los tipos de datos
    */
    public Object getValor( Object o  ){
        
        if( o.getClass() == boolean.class || o.getClass() == Boolean.class ){
            return (Boolean) o;
        }
        else if( o.getClass() == String.class ) {
            log( o+" vale "+valores.get((String) o) );
            return valores.get((String) o);
        }
        else{
            return false;
        }
    }
    
    /*
        Este metodo resuelve la expresion LISP dada al constructor de esta clase y retorna el valor solución
        si no obtiene el resultado devuelve None
    */
    public final Object resolver(){
        if( lisp.length() > 0 ){
            HashMap<String,Object> operandos = valores;
            //Seteo esta variable vacía
            String palabra = "";
            
            log("Expresion: "+lisp);
            log("Operandos:");
            log( operandos.toString() );
            log("Solucion: ");
            log("----------------------------------------------------------");
            
            //Itero por cada caracter de la expresion LISP
            for( char caracter : lisp.toCharArray() ){
                //Si es un caracter de apertura de parentesis o un espacio
                if( caracter == '(' || caracter == ' ' ){
                    //Si la palabra contiene algo
                    if( palabra.length() > 0 ){
                        //Se agrega a la pila la palabra
                        pila.add(palabra);
                        //Se vacía la palabra
                        palabra = "";
                    }
                }
                /*
                    Si no es un caracter de abrir parentesis, espacio o cerrar parentesis entonces es un caracter perteneciente
                    a una palabra
                */
                else if( caracter != '(' && caracter != ' ' && caracter != ')' ){
                    //concateno el caracter a la palabra
                    palabra += caracter;
                }
                //Si es un caracter de cierre de parentesis entonces hay que resolver la expresion contenida dentro de ese paréntesis
                else if( caracter == ')' ){
                    //Imprimo la pila
                    log( pila.toString() );
                    //Si había una palabra almacenada, la agrego a la pila
                    if( palabra.length() > 0 ){
                        //Se agrega a la pila la palabra
                        pila.add(palabra);
                        //Se vacía la palabra
                        palabra = "";
                    }

                    //Inicializo el array de los nombres de los operandos (los valores)
                    CopyOnWriteArrayList<Object> operNombres = new CopyOnWriteArrayList<>();
                    //Por cada operacion en la pila (de manera reversa) es decir last in first out LIFO
                    for( Object op : reversed(pila) ){
                        log( ""+op );
                        //Si el operador de la pila no está en los operadores, entonces es un operando (un valor o variable)
                        if( op.getClass() != String.class || !operadores.contains((String) op)  ){
                            //Agrego el operando a la lista de nombres de operandos
                            operNombres.add(op);
                            //Extraigo dicho operando de la pila
                            pila.remove( pila.size()-1 );
                        }
                        /*
                            Si el operador de la pila está en los operadores, entonces es una operación
                            OJO las operaciones en LISP siempre van después de los operandos (viendo la pila en el orden en el que
                            la estamos tratando, es decir de arriba abajo) por lo tanto los operandos ya deberían estar en su posicion
                            en la lista de operandos.
                        */
                        else{
                            //Le pasamos al metodo operar los valores para que haga lo suyo
                            operar( (String) op, operNombres );
                            //Interrumpimos el bucle para no contiunar resolviendo la expresion LISP
                            //ya que podríamos estar en el medio de esta y faltarnos valores para resolverla
                            break;
                        }
                    }

                }
            }
            
            log( "----------------------------------------------------------" );

            if( pila.size() == 1 ){
                log( "Resultado: "+pila.get(0) );
                return pila.get(0);
            }
            else{
                return null;
            }
        }
        else{
            System.out.println("La Expresión no puede estar vacía");
        }
        
        return true;
    }
    
}
