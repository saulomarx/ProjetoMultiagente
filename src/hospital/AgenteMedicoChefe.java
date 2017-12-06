package hospital;

import jade.core.*;

public class AgenteMedicoChefe extends Agent
{
    
   @Override
   protected void setup()
   {
      addBehaviour(new ComportamentoMedicoChefe(this));
   }
}