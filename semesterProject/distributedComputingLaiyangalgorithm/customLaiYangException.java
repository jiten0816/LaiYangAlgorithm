package distributedComputingLaiyangalgorithm;

public class customLaiYangException extends RuntimeException {

	public customLaiYangException(String errorMessage, Throwable err)

	{
		super(errorMessage,err);
	}

	public customLaiYangException(String string) {
		super(string);
		// TODO Auto-generated constructor stub
	}
}
