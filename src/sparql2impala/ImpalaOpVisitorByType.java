package sparql2impala;


import org.apache.log4j.Logger;

import sparql2impala.op.ImpalaBGP;
import sparql2impala.op.ImpalaDistinct;
import sparql2impala.op.ImpalaFilter;
import sparql2impala.op.ImpalaJoin;
import sparql2impala.op.ImpalaLeftJoin;
import sparql2impala.op.ImpalaOp;
import sparql2impala.op.ImpalaOp1;
import sparql2impala.op.ImpalaOp2;
import sparql2impala.op.ImpalaOpN;
import sparql2impala.op.ImpalaOrder;
import sparql2impala.op.ImpalaProject;
import sparql2impala.op.ImpalaReduced;
import sparql2impala.op.ImpalaSequence;
import sparql2impala.op.ImpalaSlice;
import sparql2impala.op.ImpalaUnion;
import sparql2impala.op.ImpalaGroup;
import sparql2impala.op.ImpalaExtend;


public abstract class ImpalaOpVisitorByType implements ImpalaOpVisitor {
    

    protected static Logger logger = Logger.getLogger(ImpalaOpVisitor.class);
    

    /**
     * Operadores que não se relacionam a operadores internos - Op0

     */
    protected abstract void visit0(ImpalaOp op);

    /**
     * Operadores que se relacionam com 1 operador interno - Op1

     */ 
    protected abstract void visit1(ImpalaOp1 op);

    /**
     * Operadores que se relacionam com 2 operadores internos - Op2
     * @param op 
     */
    protected abstract void visit2(ImpalaOp2 op);

    /**
     * Operadores que se relacionam com n operadores internos - OpN
     */
    protected abstract void visitN(ImpalaOpN op);
    


    // Operadores
    @Override
    public final void visit(ImpalaBGP ImpalaBGP) {
        visit0(ImpalaBGP);
    }
    
    @Override
    public final void visit(ImpalaFilter ImpalaFilter) {
        visit1(ImpalaFilter);
    }

    @Override
    public final void visit(ImpalaJoin ImpalaJoin) {
        visit2(ImpalaJoin);
    }

    @Override
    public final void visit(ImpalaSequence ImpalaSequence) {
        visitN(ImpalaSequence);
    }

    @Override
    public final void visit(ImpalaLeftJoin ImpalaLeftJoin) {
        visit2(ImpalaLeftJoin);
    }

    
    /*
    @Override
    public final void visit(ImpalaConditional ImpalaConditional) {
        visit2(ImpalaConditional);
    } */

    @Override
    public final void visit(ImpalaUnion ImpalaUnion) {
        visit2(ImpalaUnion);
    }

    // Modificadores de Solução
    @Override
    public final void visit(ImpalaProject ImpalaProject) {
        visit1(ImpalaProject);
    }

    @Override
    public final void visit(ImpalaDistinct ImpalaDistinct) {
        visit1(ImpalaDistinct);
    }

    @Override
    public final void visit(ImpalaReduced ImpalaReduced) {
        visit1(ImpalaReduced);
    }

    @Override
    public final void visit(ImpalaOrder ImpalaOrder) {
        visit1(ImpalaOrder);
    }

    @Override
    public final void visit(ImpalaSlice ImpalaSlice) {
        visit1(ImpalaSlice);
    }
    
    @Override
    public final void visit(ImpalaGroup ImpalaGroup) {
        visit1(ImpalaGroup);
    }
    
    @Override
    public final void visit(ImpalaExtend ImpalaExtend) {
        visit1(ImpalaExtend);
    }

}
