package criarAgente;

import jade.core.Agent;

public class Pong extends Agent 
{	
    @Override
    protected void setup() 
    {
       addBehaviour(new ComporMensagemPong(this));
    }
}