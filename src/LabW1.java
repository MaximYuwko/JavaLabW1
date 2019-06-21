import java.io.File;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class LabW1 {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter directory -> ");
        String directory = scanner.next();
        ExecutorService pool = Executors.newCachedThreadPool();
        FileProcessor fileProcessor = new FileProcessor(new File(directory), pool);
        Future<?> task = pool.submit(fileProcessor);

        try{
            task.get();
        }
        catch(Exception exception){}
            pool.shutdownNow();
    }

    public static void showMessage(String message){
        System.out.println(message);
    }
}
