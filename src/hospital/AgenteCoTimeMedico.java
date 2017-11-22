package hospital;

import jade.core.*;

public class AgenteCoTimeMedico extends Agent
{
    
   @Override
   protected void setup()
   {
      addBehaviour(new ComportamentoCoTimeMedico(this));
   }
}