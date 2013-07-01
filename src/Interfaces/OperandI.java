package Interfaces;

import Entity.Variable;
import java.util.ArrayList;

/**
 *
 * @author xir
 * 
 * An Operand is either a Variable or an Operator
 */
public interface OperandI {
    
    public int getIntValue();
    public ArrayList<Variable> getUsedVariables();
    
}
