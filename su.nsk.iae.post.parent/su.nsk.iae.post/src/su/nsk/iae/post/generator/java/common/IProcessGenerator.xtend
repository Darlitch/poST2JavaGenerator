package su.nsk.iae.post.generator.java.common

class IProcessGenerator {

    def static String generate() {

'''
public interface IProcess {

    void run();

    void dumpStates(java.util.Map<String,String> out);

    void dumpTimers(java.util.Map<String,Long> out);
    
    void dumpLocalVars(Map<String,Object> out);
    
    String getStateName();
    
    void start();
    
    void stop();
    
    void error();
    
    void setNext();

}
'''
    }

}