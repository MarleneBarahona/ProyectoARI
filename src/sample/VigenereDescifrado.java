package sample;

public class VigenereDescifrado {

    char[] mensaje;
    char[] clave;
    char[] resultado; //resultado cifrado
    char matriz[][];
    //char[] cifrado;
    char descifrado[];

    public VigenereDescifrado(String msg, String clave) {

        this.mensaje = msg.toCharArray();
        char[] claveTemp = clave.toCharArray();
        this.clave = new char[mensaje.length];
        int cont = 0;

        //For mete la clave multiples veces en 1 arreglo
        for (int i = 0; i < mensaje.length; i++) {
            this.clave[i] = claveTemp[cont];
            cont++;
            if (cont == claveTemp.length)
                cont = 0;
        }
        //la clave ya se guardo en un arreglo de igual tamaño que del mensaje

        this.matriz = generarMatrizABC();//Generamos matriz del abecedarioç

        descifrar();
    }

    //Genera descifrado
    public void descifrar(){
        descifrado = new char[mensaje.length];
        int aux= 0;
        for(int cont=0;cont<mensaje.length;cont++){
            aux = (this.mensaje[cont] - this.clave[cont]);
            if(aux<0){
                aux = aux+75*1;
            }
            descifrado[cont]= (char) (aux+48);

        }
        this.resultado = descifrado;

    }

    //Genera la Matriz de Vigenere
    private char[][] generarMatrizABC(){
        int contador;
        char abcTemp[] = this.generarAbecedario();
        char abc[] = new char[abcTemp.length*2];
        for(int c=0;c<75;c++){
            abc[c]=abcTemp[c];
            abc[c+75]=abcTemp[c];
        }
        char[][] matriz = new char[75][75];
        for(int i=0;i<75;i++){
            contador=0;
            for(int j=0;j<75;j++) {
                matriz[i][j]=abc[contador+i];
                contador++;
            }
        }
        return matriz;
    }

    //Genera los caracteres a utilizar
    private char[] generarAbecedario(){
        char[] abc = new char[75];

        for(int i= 48; i<= 122;i++){
            abc[i-48]=(char)i;
        }
        return abc;
    }


}
