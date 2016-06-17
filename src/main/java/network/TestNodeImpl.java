/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Minh Dao-Tran
 */
public class TestNodeImpl implements NodeInterface {

    private int count;
    
    public TestNodeImpl()
    {
        super();
        count = 0;
    }
    
    @Override
    public ReplyMessage handleAddingFacts() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public boolean hasMoreChoice() throws RemoteException {
        count++;
        
        switch (count)
        {
            case 1:
            case 3:
            case 14:
                System.out.println("TestNodeImpl::hasMoreChoice()? count = " + count + ". return TRUE.");
                return true;
            case 5:
            case 19:
                System.out.println("TestNodeImpl::hasMoreChoice()? count = " + count + ". return FALSE.");
                return false;
            default:
                throw new UnsupportedOperationException("TestNodeImpl::hasMoreChoice()? ERROR with count = " + count);
        }
    }


    @Override
    public ReplyMessage makeChoice(int global_level) throws RemoteException {
        count++;
        System.out.println("TestNodeImpl::makechoice(" + global_level + ").");
        
        switch (count)
        {
            case 2:
            case 4:
                System.out.println("TestNodeImpl::makechoice(" + global_level + "). count = " + count + ". return SUCCEEDED.");
                return ReplyMessage.SUCCEEDED;
            case 15:
                System.out.println("TestNodeImpl::makechoice(" + global_level + "). count = " + count + ". return INCONSISTENT.");              
                return ReplyMessage.INCONSISTENT;
            default:
                throw new UnsupportedOperationException("TestNodeImpl::makeChoice(" + global_level + "). ERROR with count = " + count);                
        }
    }
    
    @Override
    public ReplyMessage hasMoreBranch() throws RemoteException {
        count++;
        
        switch (count)
        {
            case 7:
            case 12:
            case 17:
                System.out.println("TestNodeImpl::hasMoreBranch()? count = " + count + ". return HAS_BRANCH.");
                return ReplyMessage.HAS_BRANCH;
            case 10:
            case 21:
                System.out.println("TestNodeImpl::hasMoreBranch()? count = " + count + ". return NO_MORE_BRANCH.");
                return ReplyMessage.NO_MORE_BRANCH;
            case 23:
                System.out.println("TestNodeImpl::hasMoreBranch()? count = " + count + ". return NO_MORE_ALTERNATIVE.");
                return ReplyMessage.NO_MORE_ALTERNATIVE;
            default:
                throw new UnsupportedOperationException("TestNodeImpl::hasMoreBranch()? ERROR with count = " + count);
        }
    }  

    @Override
    public ReplyMessage makeBranch() throws RemoteException {
        count++;
        switch (count)
        {
            case 8:
                System.out.println("TestNodeImpl::makeBranch. count = " + count + ". return INCONSISTENT.");
                return ReplyMessage.INCONSISTENT;
            case 13:
            case 18:
                System.out.println("TestNodeImpl::makeBranch. count = " + count + ". return SUCCEEDED.");               
                return ReplyMessage.SUCCEEDED;
            default:
                throw new UnsupportedOperationException("TestNodeImpl::makeBranch. ERROR with count = " + count);                                
        }
    }
    
    @Override
    public ReplyMessage localBacktrack(int global_level) throws RemoteException {
        count++;
        System.out.println("TestNodeImpl::localBacktrack(" + global_level + "). count = " + count + ". return SUCCEEDED.");
        return ReplyMessage.SUCCEEDED;
    }
    
    public static void main(String[] args) {
        System.out.println("Starting TestNodeImpl.main(). args[0] = " + args[0]);
        
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        
        try {
            System.out.println("Starting context ");
            // args[0] is the context id
            String name = "Context_" + args[0];
            System.out.println("name = " + name);
            NodeInterface local_node = new TestNodeImpl();
            NodeInterface stub =
                (NodeInterface) UnicastRemoteObject.exportObject(local_node, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("TestNodeImpl bound");
        } catch (Exception e) {
            System.err.println("TestNodeImpl exception:");
            e.printStackTrace();
        }
    }
}