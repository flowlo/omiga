package Datastructure.Rete;

import Entity.Constant;
import Entity.Instance;
import Entity.Operator;
import Entity.Variable;
import Interfaces.Term;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author User
 */
public class OperatorNode extends Node{
    
    private Operator op; // note that this can only be equals, notequals, bigger or smaller if allSet is true, and only PLUS or MINUS if allSet is false

    
    public OperatorNode(Rete rete, Operator op, Node from){
        super(rete);
        //System.out.println("CREATIGN OP NODE!");
        this.op = op;
        
        if(op.getOP().equals(Enumeration.OP.ASSIGN)){
            // There is one Variable that has to be calculated by the Operator therefore we add one further Variable into this VarPositions
            this.tempVarPosition = (HashMap<Variable, Integer>) from.getVarPositions().clone();
            this.tempVarPosition.put((Variable)op.getLeft(), tempVarPosition.size());
            memory.initStorage(tempVarPosition.size()+1);
            //this.memory = new Storage(tempVarPosition.size()+1);
        }else{
            // All Variables of the Operator are set
            this.tempVarPosition = (HashMap<Variable, Integer>) from.getVarPositions().clone();
            memory.initStorage(tempVarPosition.size());
            //this.memory = new Storage(tempVarPosition.size());
            //System.err.println("OMGraraga: " + op +" - "+ tempVarPosition + " - from: " + from);
        }
        
    }
    
    @Override
    public void addInstance(Instance instance){
        
        
        if(op.getOP().equals(Enumeration.OP.ASSIGN)){
            ArrayList<Variable> temp = op.getUsedVariables();
            temp.remove((Variable)op.getLeft());
            for(Variable v: temp){
                //we initialize all the avriable values by the insatnce values such that the operator can calculate its value
                v.setValue(instance.get(this.getVarPositions().get(v)));
                //System.err.println("Setting Value of: " + v + " = " + v.getValue());
            }
            Variable x = ((Variable)op.getLeft());
            x.setValue(Constant.getConstant(String.valueOf(op.getIntValue())));
            Term instanceArray[] = new Term[this.tempVarPosition.size()];
            for(Variable v: this.getVarPositions().keySet()){
                if(!v.equals(x)) {
                    //System.err.println("Setting: " + v + " = " + instance.get(this.getVarPositions().get(v)));
                    v.setValue(instance.get(this.getVarPositions().get(v)));
                }
                    instanceArray[this.getVarPositions().get(v)] = v.getValue();
            }
            //System.err.println(Instance.getInstanceAsString(instanceArray));
            Instance instance2Add = Instance.getInstance(instanceArray,instance.propagationLevel,instance.decisionLevel);
            memory.addInstance(instance2Add);
            //super.addInstance(instance2Add); // Register this instance in our backtracking structure.
            for (Node child : children) {
                sendInstanceToChild(instance2Add, child);
            }
        }else{
            for(Variable v: op.getUsedVariables()){
                //we initialize all the avriable values by the insatnce values such that the operator can calculate its value
                v.setValue(instance.get(this.getVarPositions().get(v)));
            }
            if(op.getIntValue() == 1){
                // The instance fullfills the operator
                memory.addInstance(instance);
                //super.addInstance(instance); // Register this instance in our backtracking structure.
                for (Node child : children) {
                    sendInstanceToChild(instance, child);
                }
            }else{
                //else we do not add the instance
                //System.out.println("Operatorcheck not passed!");
            }
        }
            
    }
    
}
