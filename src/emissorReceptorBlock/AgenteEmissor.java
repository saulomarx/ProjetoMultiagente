package emissorReceptorBlock;

import jade.core.Agent;
 
public class AgenteEmissor extends Agent
{    
    @Override
    protected void setup()
    {
       addBehaviour(new ComportamentoEmissor(this));
    }
}