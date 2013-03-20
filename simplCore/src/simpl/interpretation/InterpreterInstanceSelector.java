package simpl.interpretation;

public interface InterpreterInstanceSelector {
	boolean selectInstance(Object obj);
	SimplInterpretation obtainInterpreter();
}
