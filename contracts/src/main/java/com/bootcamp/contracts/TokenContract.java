package com.bootcamp.contracts;

import com.bootcamp.states.TokenState;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.CommandWithParties;
import net.corda.core.contracts.Contract;
import net.corda.core.contracts.ContractState;
import net.corda.core.transactions.LedgerTransaction;

import java.util.List;

import static net.corda.core.contracts.ContractsDSL.requireSingleCommand;
import static net.corda.core.contracts.ContractsDSL.requireThat;

public class TokenContract implements Contract{
    public static String ID = "com.bootcamp.contracts.TokenContract";


    public void verify(LedgerTransaction tx) throws IllegalArgumentException {
        if(tx.getInputStates().size() != 0) {
            throw new IllegalArgumentException("Inputs não são permitidos!");
        }
        if(tx.getOutputStates().size() !=1) {
            throw new IllegalArgumentException("Deve haver apenas um output!");
        }
        if(tx.getCommands().size() !=1) {
            throw new IllegalArgumentException("Deve haver um comando!");
        }

        if(!(tx.getOutput(0) instanceof TokenState)) {
            throw new IllegalArgumentException("Output deve ser um tokenState!");
        }

        if(((TokenState) tx.getOutput(0)).getAmount() <=0) {
            throw new IllegalArgumentException("Valor deve ser positivo!");
        }

        if(!(tx.getCommands().get(0).getValue() instanceof TokenContract.Commands.Issue)) {
            throw new IllegalArgumentException("O comando deve ser um issue!");
        }

        if(!(tx.getCommands().get(0).getSigners().contains(((TokenState) tx.getOutput(0)).getIssuer().getOwningKey()))) {
            throw new IllegalArgumentException("O Issuer deve assinar a transação!");
        }

        if(!(tx.getCommands().get(0).getSigners().contains(((TokenState) tx.getOutput(0)).getOwner().getOwningKey()))) {
            throw new IllegalArgumentException("O Owner deve assinar a transação!");
        }
    }


    public interface Commands extends CommandData {
        class Issue implements Commands { }
    }
}
