public interface IProcess {

    void run();

    void dumpStates(java.util.Map<String,String> out);

    void dumpTimers(java.util.Map<String,Long> out);

}
