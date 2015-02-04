package hr.fer.zemirs.optjava.numeric.function;

import Jama.Matrix;

public class SystemFunction extends AbstractSystem implements IHFunction {
	public SystemFunction(Matrix a, Matrix b) {
		super(a, b);
	}

	@Override
	public int getVariableNum() {
		return 6;
	}

	@Override
	public double f(Matrix vector) {
		double sum = 0;
		for(int i = 0; i < A.getRowDimension(); ++i) {
			double x1 = A.get(i, 0);
			double x2 = A.get(i, 1);
			double x3 = A.get(i, 2);
			double x4 = A.get(i, 3);
			double x5 = A.get(i, 4);
			double y = this.b.get(i, 0);
			
			double a = vector.get(0, 0);
			double b = vector.get(1, 0);
			double c = vector.get(2, 0);
			double d = vector.get(3, 0);
			double e = vector.get(4, 0);
			double f = vector.get(5, 0);
			
			double part1 = a * x1;
			double part2 = b * Math.pow(x1, 3) * x2;
			double part3 = c * Math.pow(Math.E, d * x3) * (1 + Math.cos(e * x4));
			double part4 = f * x4 * Math.pow(x5, 2);
	
			double eq = part1 + part2 + part3 + part4 - y;
			sum += eq * eq;
		}
		return (sum / 2);
	}

	@Override
	public Matrix grad(Matrix vector) {
		Matrix grad = new Matrix(6, 1);
		for(int i = 0; i < A.getRowDimension(); ++i) {
			double x1 = A.get(i, 0);
			double x2 = A.get(i, 1);
			double x3 = A.get(i, 2);
			double x4 = A.get(i, 3);
			double x5 = A.get(i, 4);
			double y = this.b.get(i, 0);
			
			double a = vector.get(0, 0);
			double b = vector.get(1, 0);
			double c = vector.get(2, 0);
			double d = vector.get(3, 0);
			double e = vector.get(4, 0);
			double f = vector.get(5, 0);
			
			double x5sqr = x5 * x5;
			double x1cub = x1 * x1 * x1;
			double edx3 = Math.pow(Math.E, d * x3);
			double cosex4 = Math.cos(e * x4);
			double longExpr1 = (a * x1 + b * x1cub * x2 + f * x4 * x5sqr - y + c * edx3 * (1 + cosex4));
			
			double[] arr = new double[] { 
				2 * x1 * longExpr1, 
				2 * x1cub * x2 * longExpr1,
				2 * edx3 * (1 + cosex4) * longExpr1, 
				2 * c * edx3 * x3 * (1 + cosex4) * longExpr1,
				-2 * c * edx3 * x4 * longExpr1 * Math.sin(e * x4), 
				2 * x4 * x5sqr * longExpr1 
			};
			
			Matrix g = new Matrix(arr, arr.length);
			grad.plusEquals(g);
		}
		return grad;
	}

	@Override
	public Matrix hessian(Matrix vector) {
		Matrix H = new Matrix(6, 6);
		for(int i = 0; i < A.getRowDimension(); ++i) {
			double x1 = A.get(i, 0);
			double x2 = A.get(i, 1);
			double x3 = A.get(i, 2);
			double x4 = A.get(i, 3);
			double x5 = A.get(i, 4);
			double y = this.b.get(i, 0);
			
			double a = vector.get(0, 0);
			double b = vector.get(1, 0);
			double c = vector.get(2, 0);
			double d = vector.get(3, 0);
			double e = vector.get(4, 0);
			double f = vector.get(5, 0);
			
			double edx3 = Math.pow(Math.E, d * x3);
			double x1sqr = x1 * x1;
			double x1cub = x1sqr * x1;
			double x1qtr = x1cub * x1;
			double x2sqr = x2 * x2;
			double x3sqr = x3 * x3;
			double x4sqr = x4 * x4;
			double x5sqr = x5 * x5;
			double x5qtr = x5sqr * x5sqr;
			double cosex4 = Math.cos(e * x4);
			double sinex4 = Math.sin(e * x4);
			double longExpr1 = (2 * c * edx3 + a * x1 + b * x1cub * x2 + f * x4 * x5sqr - y + 2 * c * edx3 * cosex4);
			
			double[][] arr = new double[][] {
				{
					2*x1sqr,
					2*x1qtr*x2,
					2*edx3*x1*(1 + cosex4),
					2*c*edx3*x1*x3*(1 + cosex4),
					-2*c*edx3*x1*x4*sinex4,
					2*x1*x4*x5sqr}
					,
				{
					2*x1qtr*x2,
					2*x1cub*x1cub*x2sqr,
					2*edx3*x1cub*x2*(1 + cosex4),
					2*c*edx3*x1cub*x2*x3*(1 + cosex4),
					-2*c*edx3*x1cub*x2*x4*sinex4,
					2*x1cub*x2*x4*x5sqr
				}
					,
				{
					2*edx3*x1*(1 + cosex4),
					2*edx3*x1cub*x2*(1 + cosex4),
					2*edx3*edx3*(1 + cosex4)*(1 + cosex4),
					2*edx3*x3*(1 + cosex4)*longExpr1,
					-2*edx3*x4*longExpr1*sinex4,
					2*edx3*x4*x5sqr*(1 + cosex4)
				}
					,
				{
					2*c*edx3*x1*x3*(1 + cosex4),
					2*c*edx3*x1cub*x2*x3*(1 + cosex4),
					2*edx3*x3*(1 + cosex4)*longExpr1,
					2*c*edx3*x3sqr*(1 + cosex4)*longExpr1,
					-2*c*edx3*x3*x4*longExpr1*sinex4,
					2*c*edx3*x3*x4*x5sqr*(1 + cosex4)
				}
					,
				{
					-2*c*edx3*x1*x4*sinex4,
					-2*c*edx3*x1cub*x2*x4*sinex4,
					-2*edx3*x4*longExpr1*sinex4,
					-2*c*edx3*x3*x4*longExpr1*sinex4,
					2*c*edx3*x4sqr*(-(cosex4*(a*x1 + b*x1cub*x2 + f*x4*x5sqr - y + c*edx3*(1 + cosex4))) + c*edx3*sinex4*sinex4),
					-2*c*edx3*x4sqr*x5sqr*sinex4
				}
					,
				{
					2*x1*x4*x5sqr,
					2*x1cub*x2*x4*x5sqr,
					2*edx3*x4*x5sqr*(1 + cosex4),
					2*c*edx3*x3*x4*x5sqr*(1 + cosex4),
					-2*c*edx3*x4sqr*x5sqr*sinex4,
					2*x4sqr*x5qtr
				}
			};
			
			Matrix h = new Matrix(arr);
			H.plusEquals(h);
		}
		return H;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{x1 \t x2 \t x3 \t x4 \t x5} \t \t y(x1,x2,x3,x4,x5)\n");
		for(int i = 0; i < A.getRowDimension(); ++i) {
			sb.append('[');
			for(int j = 0; j < A.getColumnDimension(); ++j) {
				double elem = A.get(i, j);
				sb.append(Double.toString(elem));
				sb.append("\t");
			}
			sb.append(']');
			sb.append(" = [ " + Double.toString(b.get(i,0)) + " ]" );
			sb.append("\n");
		}
		return sb.toString();
	}
}
