class Dualpoly{
	private Poly quot;	//quotient
	private Poly rem;	//reminder

	Dualpoly(int grade){
		quot = new Poly(grade);
		rem = new Poly(grade);
	}
	
	Dualpoly(int gradeQQ, int gradeRR){
		quot = new Poly(gradeQQ);
		rem = new Poly(gradeRR);
	}
	public double getQuotCff(int i){
		return quot.getPolyCff(i);
	}
	public void setQuotCff(int i, Double d){
		quot.setPolyCff(i, d);
	}
	public double getRemCff(int i){
		return rem.getPolyCff(i);
	}
	public void setRemCff(int i, Double d){
		rem.setPolyCff(i, d);
	}
	public String toString(){
		String s="\nQuot: ";
		for(int i=0;i<quot.polyCffLen();i++){
			s+=(i>0?" ":"")+quot.getPolyCff(i);
		}
		s+="\nRem: ";
		for(int i=0;i<rem.polyCffLen();i++){
			s+=(i>0?" ":"")+rem.getPolyCff(i);
		}
		return "Div result: "+s;
	}
}