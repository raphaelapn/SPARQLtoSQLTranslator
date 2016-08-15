package sparql2impala.sparql;

import com.hp.hpl.jena.sparql.algebra.OpVisitorBase;
import com.hp.hpl.jena.sparql.algebra.op.OpProject;
import com.hp.hpl.jena.sparql.core.Var;
import java.util.List;


public class FilterVarEqualityVisitor extends OpVisitorBase {
    
    public List<Var> projectVars;

    public FilterVarEqualityVisitor() {}


    @Override
    public void visit(OpProject opProject) {
        projectVars = opProject.getVars();
    }

}
