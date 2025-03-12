import java.util.StringTokenizer;

class Poly{
	private double[] cff;
	private double EPSILON=1E-6;
	private int MAXITER=50;

	Poly(int grade){
		cff = new double[grade+1];
	}
	Poly(String s){
		int i=0;
		StringTokenizer st = new StringTokenizer(s);
		cff = new double[st.countTokens()];
		
		while (st.hasMoreTokens()){
			cff[i++] = Double.parseDouble(st.nextToken());
		}
	}
	public int polyGrade(){
		return cff.length-1;
	}
	public int polyCffLen(){
		return cff.length;
	}
	public double getPolyCff(int i){
		return cff[i];
	}
	public void setPolyCff(int i, Double d){
		cff[i]=d;
	}
	public Poly sum(Poly p){
		Poly res;
		int i;
		int max = cff.length >= p.polyCffLen()?cff.length:p.polyCffLen();
		int min = cff.length <= p.polyCffLen()?cff.length:p.polyCffLen();
		Poly tmp = cff.length >= p.polyCffLen()?this:p;
		res = new Poly(max-1);
/*
		System.arraycopy(tmp, 0, res, 0, tmp.length);
		for(i=0;i<min;i++)
			res.setPolyCff(i, cff[i]+p.getPolyCff(i));
*/
		for(i=0;i<min;i++)
			res.setPolyCff(i, cff[i]+p.getPolyCff(i));
		for(;i<max;i++)
			res.setPolyCff(i, tmp.getPolyCff(i));
		return res;
	}

	public Poly sub(Poly p){
		int i;
		Poly minusp=new Poly(p.polyCffLen()-1);
		for(i=0;i<p.polyCffLen();i++)
			minusp.setPolyCff(i, -p.getPolyCff(i));
		return sum(minusp);
	}
	public Poly mul(Poly p){
		int i, j;
		Poly res = new Poly(cff.length + p.polyCffLen()-2);
		for(i=0;i<cff.length;i++)
			for(j=0;j<p.polyCffLen();j++)
				res.setPolyCff(i+j, res.getPolyCff(i+j)+cff[i]*p.getPolyCff(j));
		return res;
	}
	public Dualpoly div(Poly p){
		int i, j;
		double dd, ds;
		if(this.polyGrade() < p.polyGrade()){	//grado dividendo < grado divisore
			Dualpoly d = new Dualpoly(1,this.polyGrade());
			for(i=0;i<cff.length;i++)
				d.setRemCff(i, cff[i]);			//resto = dividendo, quoziente = 0
			return d;
		}
		Poly partRem = new Poly(cff.length);	//resto parziale
		Dualpoly d = new Dualpoly(cff.length-p.polyCffLen()+1,p.polyCffLen()-1);
		for(i=0;i<cff.length;i++)
			partRem.setPolyCff(i, cff[i]);
		for(i=partRem.polyCffLen()-1, j=p.polyCffLen()-1; i>=j; i--){
			dd=partRem.getPolyCff(i);
			dd/=p.getPolyCff(j);
			d.setQuotCff(i-j, dd);
//			partRem.setPolyCff(i, (double)0);	//oppure...
			partRem.setPolyCff(i, 0.0);
			for(int k=0;k<=j;k++)
				partRem.setPolyCff(k+i-j, partRem.getPolyCff(k+i-j)-dd*p.getPolyCff(k));
		}
		for(int k=0;k<i;k++)
			d.setRemCff(k, partRem.getPolyCff(k));
		return d;
	}
	public double polyval(double x){
		double p=0;
		for(int i=cff.length;i>0;i--)
			p=p*x+cff[i-1];
		return p;
	}
	public Poly derive(){
		Poly res = new Poly(cff.length-1);
		for(int i=1;i<cff.length;i++)
			res.setPolyCff(i,i*cff[i]);		//f'=k*n*x^(n-1)
		return res;
	}
	public double slope(double x){
		Poly res = derive();
		return res.polyval(x);
	}
	public boolean zeriBisezione(){
		return false;
	}
	public int zeriSecanti(double x1, double x2, Mydouble dbl){
		double xc,m, f1, f2, fc;
		int i, iteration;
		xc=dbl.getDbl();
		
		f1=polyval(x1);
		f2=polyval(x2);
		if(f1*f2 >= 0)		//condizioni th zeri non soddisfatte
			return -1;
		iteration=0;
		while(iteration < MAXITER){
			m=(f2-f1)/(x2-x1);
			xc=x1-f1/m;					//xc=(m*x1-f1)/m;
			fc=polyval(xc);
			if(Math.abs(fc) < EPSILON)
				break;
			if(fc*f1 > 0){
				x1=xc;
				f1=polyval(x1);
			}else{
				x2=xc;
				f2=polyval(x2);
			}
			iteration++;
		}
		if(iteration < MAXITER){
			dbl.setDbl(xc);
			return 0;
		}
		return -2;
	}
	public boolean zeriTangenti(){
		return false;
	}
	public Dualzeri zeriNewton(double x1, double x2){
		Poly drv;
		Dualzeri dz = new Dualzeri();
		double pole, xc, m, f1, f2, fc;
		int i, iteration;

		xc=0;
		f1=polyval(x1);
		f2=polyval(x2);
		if(f1*f2 >= 0){		//condizioni th zeri non soddisfatte
			dz.setSuccess(-1);
			return dz;
		}
		drv=derive();
		pole=x1;
		m=drv.polyval(pole);		//calcolo di m con derivata
		if(m == 0){			//tenta con x2
			pole=x2;
			m=drv.polyval(pole);
			if(m == 0){		//m=0, metodo non utilizzabile
				dz.setSuccess(-2);
				return dz;
			}
		}
		iteration=0;
		f1=polyval(pole);
		while(iteration < MAXITER){
			xc=pole-f1/m;					//xc=(m*x1-f1)/m;
			fc=polyval(xc);
			if(Math.abs(fc) < EPSILON)
				break;
			pole=xc;
			f1=fc;
			m=drv.polyval(pole);
			if(m == 0){
				dz.setSuccess(-2);
				return dz;
			}
			iteration++;
		}
		if(iteration <= MAXITER){
			dz.setZero(xc);
			dz.setIteration(iteration);
			dz.setSuccess(0);
			return dz;
		}
		dz.setSuccess(-3);
		return dz;
	}
	public String toString(){
		String p="";
		for(int i=0;i<cff.length;i++){
			p+=(i>0?" ":"")+cff[i];
		}
		return "Poly: "+p;
	}
}