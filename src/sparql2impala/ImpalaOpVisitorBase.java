package sparql2impala;


import org.apache.log4j.Logger;

import sparql2impala.op.ImpalaBGP;
import sparql2impala.op.ImpalaDistinct;
import sparql2impala.op.ImpalaFilter;
import sparql2impala.op.ImpalaGroup;
import sparql2impala.op.ImpalaJoin;
import sparql2impala.op.ImpalaLeftJoin;
import sparql2impala.op.ImpalaOrder;
import sparql2impala.op.ImpalaProject;
import sparql2impala.op.ImpalaReduced;
import sparql2impala.op.ImpalaSequence;
import sparql2impala.op.ImpalaSlice;
import sparql2impala.op.ImpalaUnion;
import sparql2impala.op.ImpalaExtend;


public class ImpalaOpVisitorBase implements ImpalaOpVisitor {
    

    protected static Logger logger = Logger.getLogger(ImpalaOpVisitor.class);

    // Operadores
    @Override
    public void visit(ImpalaBGP impalaBGP) {
        logger.error("BGP not supported yet!");
        throw new UnsupportedOperationException("BGP not supported yet!");
    }

    @Override
    public void visit(ImpalaFilter impalaFilter) {
        logger.error("FILTER not supported yet!");
        throw new UnsupportedOperationException("FILTER not supported yet!");
    }

    @Override
    public void visit(ImpalaJoin impalaJoin) {
        logger.error("JOIN not supported yet!");
        throw new UnsupportedOperationException("JOIN not supported yet!");
    }

    @Override
    public void visit(ImpalaSequence impalaSequence) {
        logger.error("SEQUENCE not supported yet!");
        throw new UnsupportedOperationException("SEQUENCE not supported yet!");
    }

    @Override
    public void visit(ImpalaLeftJoin impalaLeftJoin) {
        logger.error("LEFTJOIN not supported yet!");
        throw new UnsupportedOperationException("LEFTJOIN not supported yet!");
    }

    /* @Override
   public void visit(ImpalaConditional impalaConditional) {
        logger.error("CONDITIONAL not supported yet!");
        throw new UnsupportedOperationException("CONDITIONAL not supported yet!");
    }*/

    @Override
    public void visit(ImpalaUnion impalaUnion) {
        logger.error("UNION not supported yet!");
        throw new UnsupportedOperationException("UNION not supported yet!");
    }

    // Modificadores de Solução
    @Override
    public void visit(ImpalaProject impalaProject) {
        logger.error("PROJECT not supported yet!");
        throw new UnsupportedOperationException("PROJECT not supported yet!");
    }

    @Override
    public void visit(ImpalaDistinct impalaDistinct) {
        logger.error("DISTINCT not supported yet!");
        throw new UnsupportedOperationException("DISTINCT not supported yet!");
    }

    @Override
    public void visit(ImpalaReduced impalaReduced) {
        logger.error("REDUCED not supported yet!");
        throw new UnsupportedOperationException("REDUCED not supported yet!");
    }

    @Override
    public void visit(ImpalaOrder impalaOrder) {
        logger.error("ORDER not supported yet!");
        throw new UnsupportedOperationException("ORDER not supported yet!");
    }

    @Override
    public void visit(ImpalaSlice impalaSlice) {
        logger.error("SLICE not supported yet!");
        throw new UnsupportedOperationException("SLICE not supported yet!");
    }
    
    public void visit(ImpalaGroup impalaGroup){
    	logger.error("GROUP not supported yet!");
        throw new UnsupportedOperationException("GROUP not supported yet!");
    }
    
    public void visit(ImpalaExtend impalaExtend){
    	logger.error("EXTEND not supported yet!");
        throw new UnsupportedOperationException("EXTEND not supported yet!");
    }
    

}
