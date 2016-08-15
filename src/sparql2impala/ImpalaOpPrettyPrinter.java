package sparql2impala;

import java.io.PrintWriter;
import java.util.Iterator;

import sparql2impala.op.ImpalaOp;
import sparql2impala.op.ImpalaOp1;
import sparql2impala.op.ImpalaOp2;
import sparql2impala.op.ImpalaOpN;


public class ImpalaOpPrettyPrinter extends ImpalaOpVisitorByType {

	private final PrintWriter writer;
	private String offset;


	private ImpalaOpPrettyPrinter(PrintWriter _writer) {
		offset = "";
		writer = _writer;
	}


	public static void print(PrintWriter _writer, ImpalaOp op) {
		op.visit(new ImpalaOpPrettyPrinter(_writer));
	}

	@Override
	protected void visit0(ImpalaOp op) {
		writer.println(offset + op.getResultName());
	}

	@Override
	protected void visit1(ImpalaOp1 op) {
		writer.println(offset + op.getResultName() + "(");
		offset += "  ";
		if (op.getSubOp() != null)
			op.getSubOp().visit(this);
		offset = offset.substring(2);
		writer.println(")");
	}

	@Override
	protected void visit2(ImpalaOp2 op) {
		writer.println(offset + op.getResultName() + "(");
		offset += "  ";
		if (op.getLeft() != null)
			op.getLeft().visit(this);
		if (op.getRight() != null)
			op.getRight().visit(this);
		offset = offset.substring(2);
		writer.println(")");
	}

	@Override
	protected void visitN(ImpalaOpN op) {
		writer.println(offset + op.getResultName() + "(");
		offset += "  ";
		for (Iterator<ImpalaOp> iter = op.iterator(); iter.hasNext();) {
			ImpalaOp sub = iter.next();
			sub.visit(this);
		}
		offset = offset.substring(2);
		writer.println(")");
	}

}
