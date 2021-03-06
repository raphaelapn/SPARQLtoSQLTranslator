package sparql2impala;


import sparql2impala.op.ImpalaBGP;
import sparql2impala.op.ImpalaDistinct;
import sparql2impala.op.ImpalaFilter;
import sparql2impala.op.ImpalaJoin;
import sparql2impala.op.ImpalaLeftJoin;
import sparql2impala.op.ImpalaOrder;
import sparql2impala.op.ImpalaProject;
import sparql2impala.op.ImpalaReduced;
import sparql2impala.op.ImpalaSequence;
import sparql2impala.op.ImpalaSlice;
import sparql2impala.op.ImpalaUnion;
import sparql2impala.op.ImpalaGroup;
import sparql2impala.op.ImpalaExtend;

public interface ImpalaOpVisitor {
    
    // Operadores
    public void visit(ImpalaBGP impalaBGP);
    public void visit(ImpalaFilter impalaFilter);
    public void visit(ImpalaJoin impalaJoin);
    public void visit(ImpalaSequence impalaSequence);
    public void visit(ImpalaLeftJoin impalaLeftJoin);
    //public void visit(ImpalaConditional impalaConditional);
    public void visit(ImpalaUnion impalaUnion);

    // Modificadores de Solução
    public void visit(ImpalaProject impalaProject);
    public void visit(ImpalaDistinct impalaDistinct);
    public void visit(ImpalaReduced impalaReduced);
    public void visit(ImpalaOrder impalaOrder);
    public void visit(ImpalaSlice impalaSlice);
    public void visit(ImpalaGroup impalaGroup);
    public void visit(ImpalaExtend impalaExtend);

}
