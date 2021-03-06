/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Datastructure.storage.Storage;
import Entity.Atom;
import Entity.Instance;
import Entity.Variable;
import Interfaces.Term;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Gerald Weidinger 0526105
 * 
 * Abstract Class Node, is the father class of all other nodes of which the rete network is built.
 * 
 * @param memory the memory where this node saves it's instances
 * @param varOrdering defines which position of the stored variable assignments belongs to which variable
 * @param children the child-nodes of this node
 * @param rete the retenetwork the node is in
 * @param tempVarPosition used to build the rete. It's a mapping which position of an instance belongs to which variable
 * 
 */
public abstract class Node {
    
    protected Variable[] varOrdering;
    public Storage memory;
    protected ArrayList<Node> children;
    protected ArrayList<Node> childrenR;
    protected Rete rete;
    protected HashMap<Variable,Integer> tempVarPosition;
    
    public static HashSet<Node> nodes = new HashSet<Node>();
    
    /**
     * 
     * @return the tempVarPosition Mapping of this node.
     */
    public HashMap<Variable,Integer> getVarPositions(){
        //TODO remove this method --> Extra treatment for choiceNOdes needed then, but tempVars should not be used anymore!
        return this.tempVarPosition;
    }
    
    /**
     * 
     * resets the tempVarPosition mapping by removing everything and adding those variables of the current atom.
     * 
     * @param atom 
     */
    public void resetVarPosition(Atom atom){
        //System.err.println("resetVarPosition: " + this);
        tempVarPosition.clear();
        Term[] terms = atom.getTerms();
        for(int i = 0; i < terms.length;i++){
            Term t = terms[i];
            for(int j = 0;j < t.getUsedVariables().size();j++){
                Variable v = t.getUsedVariables().get(j);
                if(!tempVarPosition.containsKey(v)){
                    tempVarPosition.put(v, tempVarPosition.size());
                }
            }
        }
        //System.err.println(this + ": varPositions: " + tempVarPosition);
    }
    
    //TODO: Remove not used? Now in Atom?!
    /*public HashMap<Variable,Integer> getVarPosition(Atom atom){
        HashMap<Variable,Integer> ret = new HashMap<Variable,Integer>();
        Term[] terms = atom.getTerms();
        for(int i = 0; i < terms.length;i++){
            Term t = terms[i];
            for(int j = 0;j < t.getUsedVariables().size();j++){
                Variable v = t.getUsedVariables().get(j);
                if(!ret.containsKey(v)){
                    ret.put(v, ret.size());
                }
            }
        }
        return ret;
    }*/
    
    /**
     * 
     * public constructor. Creates a new Node with initialized data structures.
     * Also adds this node into the ChoiceUnits DecisionMemory, such that,
     * when backtracking, added instances of higher decision levels are removed again.
     * 
     * @param rete the rete network this node is in
     */
    public Node(Rete rete){
        this.rete = rete;
        this.children = new ArrayList<Node>();
        this.childrenR = new ArrayList<Node>();
        tempVarPosition = new HashMap<Variable,Integer>();
        this.rete.getChoiceUnit().addNode(this);
        nodes.add(this);
    }
    
    /**
     * 
     * @return the variable ordering of this node
     */
    public Variable[] getVarOrdering(){
        return varOrdering;
    }
    
    /**
     * 
     * selects from the memory the desired instances. (See Storage Class for details on how the selectionCriteria works)
     * 
     * @param selectionCriteria the selectionCriteria for this selection
     * @return all instances that satisfy the selectionCriteria and are contained within this memory
     */
    public Collection<Instance> select(Term[] selectionCriteria){
        //System.out.println("Selecting from node: " + this + " - " + Instance.getInstanceAsString(selectionCriteria));
        return memory.select(selectionCriteria);
    }
    
    /**
     * 
     * removes an instance from the memory, without registering this removement in the DecisionMemory
     * 
     * @param instance The instance you want to remove
     */
    public void simpleRemoveInstance(Instance instance){
        //TODO: Remove if somehow
        //System.err.println("this: " + this + " memory: " + this.memory + " removeSimpelInstnce: " + instance);
        if(this.memory.containsInstance(instance)){ // TODO: das if muss weg. Im Moment sreiekn leider noch die HeadNodeConstraints!
            this.memory.removeInstance(instance);
            //System.err.println("REMOVING INSTANCE: " + instance + " from: " + this);
        }
    }
    
    /**
     * 
     * Adds an instance into the memory, without registering this instance in the Decision Memory
     * 
     * @param instance the instance you want to add to this nodes memory
     */
    public void simpleAddInstance(Instance instance){
        this.memory.addInstance(instance);
    }
    
    /**
     * 
     * registeres the adding of an instance in the DecisionMemory.
     * (Actually does not add this instance into the memory, since this differs from node to node)
     * Therefore each node has to override this method. (But most of them call super.addInstance,
     * to register within the Decision Memory)
     * 
     * @param instance The instance you want to add
     * @param from true means right JoinPartner false means leftt one. For nonJoinNodes this parameter doese not matter
     */
    public void addInstance(Instance instance, boolean from){
        this.rete.getChoiceUnit().addInstance(this, instance);
    }
    
    /**
     * 
     * removes an instance from this nodes memory.
     * 
     * @param instance The instance you want to remove
     */
    public void removeInstance(Instance instance){
        //TODO: Is this method eer called? We only use simpleRemove no?
        //has to be implemented by each NodeType
        if(this.memory.containsInstance(instance)){ // TODO: das if muss weg. Im Moment sreiekn leider noch die HeadNodeConstraints!
            this.memory.removeInstance(instance);
            //System.err.println("REMOVING INSTANCE: " + instance + " from: " + this);
        }else{
            //System.out.println("TRIED TO REMOVE " + instance + " from: " + this + " BUT WAS NOT THERE!");
        }
        
    }
    
    /**
     * 
     * @param instance the instance you want to check for
     * @return wether the instance is saved within the memory or not
     */
    public boolean containsInstance(Instance instance){
        return memory.containsInstance(instance);
    }
    
    /**
     * 
     * @return the children of this node
     */
    public ArrayList<Node> getChildren(){
        return this.children;
    }
    /**
     * 
     * @return the childrenR of this node
     */
    public ArrayList<Node> getChildrenR(){
        return this.childrenR;
    }
    
    /**
     * 
     * Adds a child to this node
     * 
     * @param n the node you want to add as a child for this node
     */
    public void addChild(Node n){
        if (!this.children.contains(n)) this.children.add(n);
    }
    
    public void addChildR(Node n){
        if (!this.childrenR.contains(n)) this.childrenR.add(n);
    }
    
    public Storage getMemory(){
        return this.memory;
    }
    

    
    
}
