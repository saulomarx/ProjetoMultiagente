package hospital;

import jade.core.*;
 
public class AgenteCoTransplante extends Agent
{    
    @Override
    protected void setup()
    {
       try
       {
          Thread.sleep(5000);
       }
       catch(Exception e)
       {
          System.out.println("Erro: " + e);
       }
       
       addBehaviour(new ComportamentoCoTransplante(this));
    }
}