package co.istad.view;

public class HelperView {
    public static void welcome( String title ){
        System.out.println( title );
    }

    public static void error(String message){
        System.err.println(message);
    }
    public static void message( String message ){
        System.out.println(message);
    }

}
