import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Start!");
//        task1();
//       task2();
        task3();
        System.out.println("Done!");
    }


    private static void task1() {
        System.out.println("---------- Task 1 ----------");
        List<String> ids = IDs.getIDs();
        System.out.print("Students ids: ");
        for(String id : ids)
            System.out.print(id+" | ");
        System.out.println("");
    }

    private static void task2() {
        System.out.println("---------- Task 2 ----------");
        List<ISolver> solvers = new ArrayList<ISolver>();
        Minimax minimax = new Minimax();
        solvers.add(minimax);
        solveInstances(solvers);
        System.out.println("");
    }

    private static void task3() {
        System.out.println("---------- Task 3 ----------");
        List<ISolver> solvers = new ArrayList<ISolver>();
        AlphaBetaPruning alphaBeta = new AlphaBetaPruning();
        solvers.add(alphaBeta);
        solveInstances(solvers);
        System.out.println("");
    }

    public static void solveInstances(List<ISolver> solvers) {
        try {
            long totalTime = 0;
            List<String> instances = getInstances();
            for(String instance : instances) {
                System.out.println("---- "+instance.substring(instance.indexOf("othello_"))+" ----");
                WeightedOthelloBoard problem = new WeightedOthelloBoard(instance);
                problem.printBoard();
                for(ISolver solver : solvers) {
                    System.out.println("Solver: "+solver.getSolverName());
                    long startTime = System.nanoTime();
                    double solution = solver.solve(problem);
                    long finishTime = System.nanoTime();

                    System.out.println("Minimax Value:  "+solution);
                    System.out.println("Time:  "+(finishTime-startTime) / 1000000.0+" ms");
                    totalTime += (finishTime-startTime) / 1000000.0;
                    System.out.println("");
                }
                System.out.println("");
            }
            System.out.println("Total time:  "+totalTime / 60000.0+" min");
            System.out.println("");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getInstances() throws IOException {
        List<String> instances = new ArrayList<String>();
        String currentDir = new java.io.File(".").getCanonicalPath()+"//instances//";
        File folder = new File(currentDir);
        File[] listOfFiles = folder.listFiles();

        for(int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                instances.add(currentDir+listOfFiles[i].getName());
            }
        }
        return instances;
    }
}
