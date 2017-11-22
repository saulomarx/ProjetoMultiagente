package hospital;

import jade.core.*;

public class AgenteCoHospital extends Agent
{
    
   @Override
   protected void setup()
   {
       try
       {
          Thread.sleep(3000);
       }
       catch(Exception e)
       {
          System.out.println("Erro: " + e);
       }
      addBehaviour(new ComportamentoCoHospital(this));
   }
}