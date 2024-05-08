package bet.astral.aaaa.example;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public class Example {
	public static void main(String[] args){
		ExampleInterface exampleInterface = () -> "Hello!";

		Example example = new Example(exampleInterface);
		Object object = example.something();
	}

	private final ExampleInterface exampleInterface;
	private ExampleEnum exampleEnum;
	public ExampleRecord record;
	public Example(ExampleInterface exampleInterface) {
		this.exampleInterface = exampleInterface;
		this.exampleEnum = ExampleEnum.FIRST;
		this.record = new ExampleRecord("first value", "second value");

		voidFunction();
		returnString();
		doSomething("Something");
		doSomething(returnString());
	}

	public void voidFunction(){
		System.out.println("Hello!");
	}
	public String returnString(){
		return "returned a string";
	}
	public void doSomething(String something) {
		System.out.println(something);
	}

	@Nullable
	@ApiStatus.NonExtendable
	public Object something(){
		System.out.println("Hello World!");
		System.out.println(exampleInterface.something());
		return null;
	}
}
