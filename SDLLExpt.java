import java.io.PrintWriter;

/**
 * Some simple experiments with SimpleDLLs
 */
public class SDLLExpt {
  public static void main(String[] args) throws Exception {
    PrintWriter pen = new PrintWriter(System.out, true);
    SimpleListExpt.expt1(pen, new SimpleCDLL<String>());
    SimpleListExpt.expt2(pen, new SimpleCDLL<String>());
    SimpleListExpt.expt3(pen, new SimpleCDLL<String>());
    SimpleListExpt.expt4(pen, new SimpleCDLL<String>(), 3);
    SimpleListExpt.expt5(pen, new SimpleCDLL<String>());
    SimpleListExpt.expt6(pen, new SimpleCDLL<String>());
  } // main(String[]
} // SDLLExpt
