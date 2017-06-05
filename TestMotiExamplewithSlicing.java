import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import jfuzz.JFuzz;
import monitor.MonitorWithDFA;
import property.analysis.Property;
import property.analysis.PropertyAnalysis;
import property.guided.tests.PropertyGuidedSEAbstractTest;
import util.Global;
import automata.State;
import automata.Transition;
import automata.fsa.FSATransition;
import automata.fsa.FiniteStateAutomaton;
import automata.fsa.NFAToDFA;
import slice.PathSlicer;
import util.Global;

public class TestMotiExamplewithSlicing extends PropertyGuidedSEAbstractTest {
 
	public static void main(String[] args) {
		/**args for jdart*/ 
		String symMethod = "moti.Example.start()";
		String symMethodSig = "moti.Example.start()";
		String classname = "moti.Example";
		  
		/**args for static analysis*/
		String includeFile = "src/example-motiexample/Include.txt";
		String exclusionFile = "src/example-motiexample/Exclusions.txt";

		String mainClassName = "Lmoti/Example";
		String inputFile = "";
		
		Global.DEBUG  =  false;
		Global.experimentMode=false; // if  true, then the pruned branches can still run!
		
		if (args.length < 5) {
			args = new String[]{"1", "1", "0", "-1", "0,0,10,0", "", "1"};
		}
		
		initArgs(args);
		
		String[] testArgs = createArgsMotiExample(inputFile, symMethod, symMethodSig, classname, iterationNum, iterationPeriod,
				includeFile, exclusionFile, mainClassName, callStringBound, propertyGuidedSE, refineAnalysisResult);

		TestMotiExample tester = new TestMotiExample();

                // set the configuration of path slicing
				String Events[] = new String[]{
				"<init>(Ljava/io/InputStream;)V",
				"<init>(Ljava/io/InputStream;Ljava/lang/String;)V",
				"<init>(Ljava/io/InputStream;Ljava/nio/charset/Charset;)V",
				"<init>(Ljava/io/InputStream;Ljava/nio/charset/CharsetDecoder;)V",
				"read()I",
				"read([CII)I",
				"ready()Z",
				"close()V"
				};
				String mainclass="Lmoti/Example";
				String Methods[] = new String[]{
						"main",
						"moti.Example.start()"
				};
				String include= "src/example-motiexample/Include.txt";
				String exclusion = "src/example-motiexample/Exclusions.txt";
				PathSlicer.config.Set(Events,mainclass,Methods,include,exclusion);
		
		tester.startTest(testArgs);		
	}
	
	public void setProperty() {

		FiniteStateAutomaton dfa = getForwardFSA();
		
		MonitorWithDFA forwardMonitor = new MonitorWithDFA(dfa);
			
		String classSig = "Ljava/io/InputStreamReader;";
		
		//init operations
		String inits[] = new String[]{
				"<init>(Ljava/io/InputStream;)V",
				"<init>(Ljava/io/InputStream;Ljava/lang/String;)V",
				"<init>(Ljava/io/InputStream;Ljava/nio/charset/Charset;)V",
				"<init>(Ljava/io/InputStream;Ljava/nio/charset/CharsetDecoder;)V"
		};
		for(String i : inits){
			forwardMonitor.addSensitiveEvent(classSig, i.trim());  
		}
		
		//close operation
		forwardMonitor.addSensitiveEvent(classSig, "close()V");
		
		//read operations
		String operations[] = new String[]{
				"read()I",
				"read([CII)I",
				"ready()Z"
		};
		
		for(String w : operations){
			forwardMonitor.addSensitiveEvent(classSig, w.trim());
		}
		   
		//method name to char map
		for(String i : inits) {
			forwardMonitor.addMethodNameChar(i.trim(), 'I');	
		}
		
		forwardMonitor.addMethodNameChar("close()V", 'C');
		
		for(String w : operations) {
			forwardMonitor.addMethodNameChar(w.trim(), 'R');
		}
		
		PropertyAnalysis.property = new Property(forwardMonitor);
	}
	
	public static FiniteStateAutomaton getForwardFSA() {
		//create a FSA
		FiniteStateAutomaton nfa = new FiniteStateAutomaton();
		State s0 = nfa.createState(new Point(0,0)); // inited
		nfa.setInitialState(s0);
		State s1 = nfa.createState(new Point(0,0)); // close
		State s2 = nfa.createState(new Point(0,0)); // error
		nfa.addFinalState(s2);	
		List<Transition> trans = new ArrayList<Transition>();
		trans.add(new FSATransition(s0, s0, "I"));
		trans.add(new FSATransition(s0, s0, "R"));
		trans.add(new FSATransition(s0, s1, "C"));
		trans.add(new FSATransition(s1, s1, "C"));
		trans.add(new FSATransition(s1, s2, "R"));
		trans.add(new FSATransition(s2, s2, "R"));
		trans.add(new FSATransition(s2, s1, "C"));
		for(Transition t : trans) {
			nfa.addTransition(t);
		}
		NFAToDFA to = new NFAToDFA();	
		FiniteStateAutomaton dfa = to.convertToDFA(nfa);
		Global.shortestPathLength.put(s0.getID(), 2);
		Global.shortestPathLength.put(s1.getID(), 1);
		Global.shortestPathLength.put(s2.getID(), 0);
		return dfa;
	}

	public static String[] createArgsMotiExample(String inputFile, 
			String symMethod,
			String symMethodSig, 
			String className, 
			int numExec,
			String period,
			String analysisInputFile, 
			String analysisExclusionFile,
			String mainClassname, 
			int callStringBound, 
			boolean propertyGuidedSE,
			boolean refineAnalysisResult) {
		return new String[] {"+" + JFuzz.JFUZZ_INPUT_PROP + "=" + inputFile, 
				"+jpf.basedir=../jpf-core",
				"+symbolic.dp=z3bitvec",
                "+classpath=build/example-motiexample",
				"+vm.insn_factory.class=gov.nasa.jpf.jdart.ConcolicInstructionFactory",
				"+listener=jfuzz.ConcolicListener,monitor.MonitorListener,gov.nasa.jpf.vm.JVMForwarder", //,slice.SliceListener",
				"+symbolic.method="  + symMethod,
				"+search.class=gov.nasa.jpf.search.Simulation",
				"+jpf.report.console.finished=",
				numExec != -1? "+" + JFuzz.JFUZZ_NUM_EXEC + "=" + numExec:
			    "+" + JFuzz.JFUZZ_TIME_EXEC + "=" + period,
				"+" + JFuzz.JFUZZ_NO_DEL + "=",
				"+" + "perturb.params=bar",
				"+" + "perturb.bar.method=" + symMethodSig,
				"+" + "perturb.class=" + "jfuzz.Producer", 
				"+target=" + className, 
				"+target_args=" + inputFile,
				"+include.file=" + analysisInputFile,
				"+exclusion.file=" + analysisExclusionFile,
				"+main.class.name=" + mainClassname,
				"+call.string.bound=" + callStringBound,
				"+property.guided.symbolic.execution=" + propertyGuidedSE,
				"+refine.analysis.result=" + refineAnalysisResult,
                "+nhandler.clean=true",
                "+native_classpath=build/example-motiexample",
                "+nhandler.delegateUnhandledNative=true",
                "+nhandler.resetVMState=true",
                "+nhandler.spec.delegate=java.lang.Class.getProtectionDomain",
                "+property.slicing.symbolic.execution=" + PropertyGuidedSEAbstractTest.doSlicing
		};
	}	
}
