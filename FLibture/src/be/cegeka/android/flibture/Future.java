package be.cegeka.android.flibture;

import java.util.ArrayList;

public class Future<T> {

	private T value;
	private Exception exception;
	
	private ArrayList<FutureCallable<T>> futureCallables = new ArrayList<FutureCallable<T>>();

	public synchronized void setValue(T value) {
		this.value = value;
	}
	
	public synchronized void setError(Exception exception){
		this.exception = exception;
	}

	public T getValue() {
		return value;
	}

	public void notifyFutures() {
		if (exception == null) {
			for (FutureCallable<T> f : futureCallables) {
				f.onSucces(getValue());
			}
		} else {
			for (FutureCallable<T> f : futureCallables) {
				f.onError(exception);
			}
		}
	}

	public synchronized void registerFutureCallable(FutureCallable<T> callable) {
		if(value != null)
		{
			callable.onSucces(value);
		}
		else if(exception != null)
		{
			callable.onError(exception);
		}
		else
		{
			futureCallables.add(callable);
		}
//		FutureTask<String, Boolean> futureTask = new FutureTask<String, Boolean>() {
//			
//			@Override
//			protected String doInBackgroundFuture(Boolean... uri) throws Exception {
//				// TODO Auto-generated method stub
//				return null;
//			}
//		}; 
//		
//		FutureTask<Boolean, Object> futureTask2 = new FutureTask<Boolean, Object>() {
//			
//			@Override
//			protected Boolean doInBackgroundFuture(Object... uri) throws Exception {
//				// TODO Auto-generated method stub
//				return null;
//			}
//		};
//		
//		futureTask.whenDone(new FutureCallable<String>() {
//			
//			@Override
//			public void onSucces(String result) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onError(Exception e) {
//				// TODO Auto-generated method stub
//				
//			}
//		}).pipe(futureTask2, "fez").whenDone(new FutureCallable<Boolean>() {
//			
//			@Override
//			public void onSucces(Boolean result) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onError(Exception e) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//		
//		futureTask.executeFuture(true,false,true);
		
	}
	
	public static <S> void whenResolved(Future<S> future, FutureCallable<S> futureCallable){
		future.registerFutureCallable(futureCallable);
	}
	
}
