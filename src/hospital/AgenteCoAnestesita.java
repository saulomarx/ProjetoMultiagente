package hospital;

import jade.core.*;

public class AgenteCoAnestesita extends Agent
{
    
   @Override
   protected void setup()
   {
      addBehaviour(new ComportamentoCoAnestesista(this));
   }
}