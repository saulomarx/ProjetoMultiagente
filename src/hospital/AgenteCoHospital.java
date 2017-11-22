package hospital;

import jade.core.*;

public class AgenteCoHospital extends Agent
{
    
   @Override
   protected void setup()
   {
      addBehaviour(new ComportamentoCoHospital(this));
   }
}