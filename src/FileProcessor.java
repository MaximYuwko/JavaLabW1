import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class FileProcessor implements Runnable {

    private File directory;
    private ExecutorService pool;

    public FileProcessor(File directory, ExecutorService pool){
        this.directory = directory;
        this.pool = pool;
    }

    @Override
    public void run() {
        File[] files = directory.listFiles();
        ArrayList<Future<?>> tasks = new ArrayList<>();
        for (File fileInList:files
             ) {
            if(fileInList.isDirectory()){
                FileProcessor fileProcessor = new FileProcessor(fileInList, pool);
                tasks.add(pool.submit(fileProcessor));
            }
            else if(fileInList.getName().contains(".java")){
                search(fileInList);
            }
        }
        try {
            for (Future<?> task : tasks
            ) {
                task.get();
            }
        }
        catch(Exception exception){
            LabW1.showMessage(exception.getMessage());
        }
    }

    private void search(File file){
        try (Scanner scanner = new Scanner(new FileInputStream(file))) {
            ArrayList<String> fileLines = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String str = scanner.nextLine();
                str = str.replaceAll("public", "protected");
                fileLines.add(str);
            }
            scanner.close();
            FileWriter fileWriter = new FileWriter(file, false);
            for (String line:fileLines
                 ) {
                fileWriter.write(line + "\n");
            }
            fileWriter.close();
            LabW1.showMessage("File " + file + " was processed");
        }
        catch(Exception exception){
            LabW1.showMessage(exception.getMessage());
        }
    }
}
