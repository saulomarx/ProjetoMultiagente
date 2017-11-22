package emissorReceptorBlock;

import jade.core.*;

public class AgenteReceptor  extends Agent
{
   @Override
   protected void setup()
   {        
      addBehaviour(new ComportamentoReceptorComBlock(this));
   }
}
