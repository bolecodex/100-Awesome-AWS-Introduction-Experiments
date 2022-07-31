package Demogroup;
public class Alternator {
    private Integer firstValue;
    private Integer secondValue;
    private boolean switcher;

    public Alternator(Integer firstValue, Integer secondValue) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
        this.switcher = true;
        System.out.println("Hello World!");
    }

    public Integer alternate(){
        if (switcher){
            switcher = !switcher;
            return firstValue;
        }else {
            switcher = !switcher;
            return secondValue;
        }
    }
}