/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Interfaces.BodyAtom;
import Interfaces.Term;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author User
 */
public class Rule {
    private Atom head;
    private ArrayList<Atom> bodyPlus;
    private ArrayList<Atom> bodyMinus;
    private ArrayList<Operator> operators;
    
    public Rule(Atom head, ArrayList<Atom> bodyPlus, ArrayList<Atom> bodyMinus, ArrayList<Operator> operators){
        this.head = head;
        this.bodyPlus = bodyPlus;
        this.bodyMinus = bodyMinus;
        this.operators = operators;
    }
    
    public Rule(){
        this.head = null;
        this.bodyPlus = new ArrayList<Atom>();
        this.bodyMinus = new ArrayList<Atom>();
        this.operators = new ArrayList<Operator>();
    }
    
    public void setHead(Atom head){
        this.head = head;
    }
    public void addAtomPlus(Atom p){
        this.bodyPlus.add(p);
    }
    public void addAtomMinus(Atom p){
        this.bodyMinus.add(p);
    }
    
    public boolean isSafe(){
        //TODO: Is this the definition of safe? or is it also safe when the Variable occurs in the head?
        HashSet<Variable> hs = new HashSet<Variable>();
        for(BodyAtom ba: bodyPlus){
            Atom pir = (Atom)ba;
            if(ba.getClass().equals(Atom.class)){
                for(Term t: pir.getTerms()){
                    for(Variable v: t.getUsedVariables()){
                        hs.add(v);
                    }
                }
            }
        }
        for(BodyAtom ba: bodyMinus){
            if(ba.getClass().equals(Atom.class)){
                Atom pir = (Atom)ba;
                for(Term t: pir.getTerms()){
                    for(Variable v: t.getUsedVariables()){
                        if(!hs.contains(v)) return false;
                    }
                }
            }
        }
        if(head != null){
            for(Term t: this.head.getTerms()){
                for(Variable v: t.getUsedVariables()){
                    if(!hs.contains(v)) return false;
                }
            }
        }
        
        return true;
    }
    
    public boolean isConstraint(){
        if(head == null) return true;
        return false;
    }
    
    @Override
    public String toString(){
        String s = "";
        if (head != null) s = s + head.toString();
        s = s + " :- ";
        for(BodyAtom ba: bodyPlus){
            s = s + ba + ",";
        }
        for(BodyAtom ba: bodyMinus){
            s = s + ba + ",";
        }
        return s.substring(0, s.length()-1) + ".";
    }

    public ArrayList<Atom> getBodyMinus() {
        return bodyMinus;
    }

    public ArrayList<Atom> getBodyPlus() {
        return bodyPlus;
    }
    
    public ArrayList<Operator> getOperators(){
        return operators;
    }

    public Atom getHead() {
        return head;
    }
    
    
    
    
    
}