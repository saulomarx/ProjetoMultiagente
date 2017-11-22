package hospital;

import jade.core.*;

public class AgenteMedicoChefe extends Agent
{
    
   @Override
   protected void setup()
   {
       
       try
       {
          Thread.sleep(1000);
       }
       catch(Exception e)
       {
          System.out.println("Erro: " + e);
       }
      addBehaviour(new ComportamentoMedicoChefe(this));
   }
}