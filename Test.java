class Test{
	public static void main(String args[]){
		Mydouble dbl=new Mydouble(0);
		double zero = 0;
		Poly p1, p2;
		p1=new Poly("1 2 3");
		p2=new Poly("3 4 5");
		System.out.println(p1.toString());
		System.out.println(p2.toString());
		System.out.println(p1.sum(p2).toString());
		System.out.println(p1.sub(p2).toString());
		System.out.println("Value in x=1: "+p1.polyval(1));
		System.out.println("Slope in x=1: "+p1.slope(1));
		p2=new Poly("3 4 5 6");
		System.out.println(p1.div(p2).toString());
		p1=new Poly("-1 1 2 0 1");
		p2=new Poly("1 1 1");
		System.out.println(p1.div(p2).toString());
		p1=new Poly("-6 1 1");
		System.out.println("Con secanti: "+p1.zeriSecanti(1,3,dbl));
		System.out.println(dbl.getDbl());
		
		Dualzeri dz = p1.zeriNewton(1,3);
		System.out.println("Con Newton, retval="+dz.getSuccess());
		if(dz.getSuccess() == 0){
			System.out.println("Zero="+dz.getZero()+" with iteration="+dz.getIteration());
		}else{
			System.out.println("error!!");
		}
	}
}