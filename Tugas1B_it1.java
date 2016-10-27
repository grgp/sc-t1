public class Tugas1B_it1 {
    public static void main(String[] args) {
        try {
            Scanner in = new Scanner(System.in);
            File inputFile = new File(in.nextLine());

            Scanner input = new Scanner(inputFile);
            int dimension = Integer.parseInt(input.nextLine());

            for (i=0, i<dimension, i++) {
                
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }
}
