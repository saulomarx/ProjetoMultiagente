package hospital;

import jade.core.*;

public class AgenteCoCentroCirurgico extends Agent
{
    
   @Override
   protected void setup()
   {
      addBehaviour(new ComportamentoCoCentroCirurgico(this));
   }
}