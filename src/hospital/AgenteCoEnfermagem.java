package hospital;

import jade.core.*;

public class AgenteCoEnfermagem extends Agent
{
    
   @Override
   protected void setup()
   {
      addBehaviour(new ComportamentoCoEnfermagem(this));
   }
}