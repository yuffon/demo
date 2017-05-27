

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import jfuzz.JFuzz;
import monitor.MonitorWithDFA;
import automata.State;
import automata.Transition;
import automata.fsa.FSATransition;
import automata.fsa.FiniteStateAutomaton;
import automata.fsa.NFAToDFA;
import property.analysis.Property;
import property.analysis.PropertyAnalysis;
import property.guided.tests.PropertyGuidedSEAbstractTest;
import property.guidedSE.JDartPropertyGuidedSE;
import util.Global;
import util.Global.HeuristicOfDSE.Strategy;

public class TestRhino extends PropertyGuidedSEAbstractTest{

	public static void main(String[] args) {
		/**args for jdart*/
		String symMethod = "test.rhino.TestParser.start()";
		String symMethodSig = "test.rhino.TestParser.start()";
		String classname = "test.rhino.TestParser";
		
		/**args for static analysis*/
		String includeFile = "src/example-rhino/Include.txt";
		String exclusionFile = "src/example-rhino/Exclusions.txt";

		String mainClassName = "Ltest/rhino/TestParser";

		Global.USE_LAST_VALUE = true;
		Global.DEBUG = false;
		Global.HeuristicOfDSE.GUIDEDSEARCHSTRATEGY = Strategy.GUIDED;
		
		if (args.length < 5) {
			args = new String[]{"1", "1", "1", "-1", "1,0,0,0"};
		}
		
		initArgs(args);
		
		String[] testArgs = createArgsRhino(null, symMethod, symMethodSig, classname, iterationNum, iterationPeriod,
				includeFile, exclusionFile, mainClassName, callStringBound, propertyGuidedSE, refineAnalysisResult);

		TestRhino tester = new TestRhino();
		tester.startTest(testArgs);
	}

	/**
	 * a call back function, used to set the property in the PropertyAnalysis
	 * */
	@Override
	public void setProperty() {
		// now only construct a monitor by hand
		FiniteStateAutomaton dfa = getForwardFSAAntlr();
		
		MonitorWithDFA forwardMonitor = new MonitorWithDFA(dfa);//com.alibaba.fastjson.parser
		forwardMonitor.addSensitiveEvent("Ljava/util/Enumeration;", "hasMoreElements()Z");
		forwardMonitor.addSensitiveEvent("Ljava/util/Enumeration;", "nextElement()Ljava/lang/Object;");
		forwardMonitor.addSensitiveEvent("Lmozilla/javascript/ShallowNodeIterator;", "hasMoreElements()Z");
		forwardMonitor.addSensitiveEvent("Lmozilla/javascript/ShallowNodeIterator;", "nextElement()Ljava/lang/Object;");
		forwardMonitor.addMethodNameChar("hasMoreElements()Z", 'H');
		forwardMonitor.addMethodNameChar("nextElement()Ljava/lang/Object;", 'N');
		//important		
		PropertyAnalysis.property = new Property(forwardMonitor);
	}
	
	public static FiniteStateAutomaton getForwardFSAAntlr() {
		//create a FSA
		FiniteStateAutomaton nfa = new FiniteStateAutomaton();
		State s0 = nfa.createState(new Point(0,0)); // start
		s0.setLabel("0");
		nfa.setInitialState(s0);
		State s1 = nfa.createState(new Point(0,0));
		s1.setLabel("1");
		State s2 = nfa.createState(new Point(0,0));
		s2.setLabel("2");
		nfa.addFinalState(s2);

		List<Transition> trans = new ArrayList<Transition>();
		
		trans.add(new FSATransition(s0, s2, "N")); // next
		trans.add(new FSATransition(s0, s1, "H")); // hasnext
		trans.add(new FSATransition(s1, s0, "N")); // next
		trans.add(new FSATransition(s1, s1, "H")); // hasNext
		trans.add(new FSATransition(s2, s2, "N")); // next
		trans.add(new FSATransition(s2, s1, "H")); // hasNext

		for(Transition t : trans) {
			nfa.addTransition(t);
		}

		//deterministic
		NFAToDFA to = new NFAToDFA();
				
		FiniteStateAutomaton dfa = to.convertToDFA(nfa);
				
		Global.shortestPathLength.put(s0.getID(), 2);
		Global.shortestPathLength.put(s1.getID(), 1);
		Global.shortestPathLength.put(s2.getID(), 0);
				
		return dfa;
	}
	
	public static String[] createArgsRhino(String inputFile, 
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
                "+classpath=build/example-rhino",
				"+vm.insn_factory.class=gov.nasa.jpf.jdart.ConcolicInstructionFactory",
				"+listener=jfuzz.ConcolicListener,monitor.MonitorListener,gov.nasa.jpf.vm.JVMForwarder",
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
                "+native_classpath=build/example-rhino",
                "+nhandler.delegateUnhandledNative=true",
                "+nhandler.resetVMState=false"
                //"+nhandler.spec.delegate=java.util.ResourceBundle.*"
		};
	}

}
