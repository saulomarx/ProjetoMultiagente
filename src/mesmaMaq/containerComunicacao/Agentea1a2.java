package mesmaMaq.containerComunicacao;

import jade.core.Agent;

public class Agentea1a2 extends Agent 
{
    @Override
    protected void setup() 
    {
        addBehaviour(new CompMensagemA1A2(this));
    }
}