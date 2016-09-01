package sparql2impala;

import java.util.Iterator;

import sparql2impala.op.ImpalaOp;
import sparql2impala.op.ImpalaOp1;
import sparql2impala.op.ImpalaOp2;
import sparql2impala.op.ImpalaOpN;


public class ImpalaOpWalker extends ImpalaOpVisitorByType {


	private final ImpalaOpVisitor visitor;

	private final boolean topDown;


	private ImpalaOpWalker(ImpalaOpVisitor visitor, boolean topDown) {
		this.visitor = visitor;
		this.topDown = topDown;
	}


	public static void walkTopDown(ImpalaOpVisitor visitor, ImpalaOp op) {
		op.visit(new ImpalaOpWalker(visitor, true));
	}

	public static void walkBottomUp(ImpalaOpVisitor visitor, ImpalaOp op) {
		op.visit(new ImpalaOpWalker(visitor, false));
	}

	/**
	 * Visita operador folha sem operadores internos.
	 * 
	 */
	@Override
	protected void visit0(ImpalaOp op) {
		op.visit(visitor);
	}

	/**
	 * Visita operador com 1 operador interno.
	 * 
	 * @param op
	 */
	@Override
	protected void visit1(ImpalaOp1 op) {
		if (topDown)
			op.visit(visitor);
		if (op.getSubOp() != null)
			op.getSubOp().visit(this);
		else
			logger.warn("Sub operator is missing in " + op.getResultName());
		if (!topDown)
			op.visit(visitor);
	}

	/**
	 * Visita operador com 2 operadores internos.
	 * 
	 * @param op
	 */
	@Override
	protected void visit2(ImpalaOp2 op) {
		if (topDown)
			op.visit(visitor);
		if (op.getLeft() != null)
			op.getLeft().visit(this);
		else
			logger.warn("Left sub operator is missing in " + op.getResultName());
		if (op.getRight() != null)
			op.getRight().visit(this);
		else
			logger.warn("Right sub operator is missing in " + op.getResultName());
		if (!topDown)
			op.visit(visitor);
	}

	/**
	 * Visita operador com N operadores internos.
	 * 
	 */
	@Override
	protected void visitN(ImpalaOpN op) {
		if (topDown)
			op.visit(visitor);
		for (Iterator<ImpalaOp> iter = op.iterator(); iter.hasNext();) {
			ImpalaOp sub = iter.next();
			sub.visit(this);
		}
		if (!topDown)
			op.visit(visitor);
	}

}
