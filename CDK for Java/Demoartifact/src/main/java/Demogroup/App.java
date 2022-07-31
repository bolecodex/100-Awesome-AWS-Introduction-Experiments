package Demogroup;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main (String[] args) {
        Alternator alt3 = new Alternator(3, 6);
        System.out.println(alt3.alternate()); // prints 3
        System.out.println(alt3.alternate()); // prints 6
        // Method both changes state and accesses state!
        System.out.println(alt3.alternate()); // prints 3
        System.out.println(alt3.alternate()); // prints 6
    } 
}
