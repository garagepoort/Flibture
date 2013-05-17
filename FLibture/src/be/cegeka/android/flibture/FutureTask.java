package be.cegeka.android.flibture;

import android.os.AsyncTask;

public abstract class FutureTask<T, S> extends AsyncTask<S, Object, Future<T>> {
	private Future<T> future;

	
	public FutureTask() {
		future = new Future<T>();
	}

	public Future<T> executeFuture(S... arguments) {
		super.execute(arguments);
		return future;
	}

	@Override
	protected final Future<T> doInBackground(S... arguments) {
		try {
			future.setValue(doInBackgroundFuture(arguments));
		} catch (Exception e) {
			future.setError(e);
		}
		return future;
	}

	@Override
	protected final void onPostExecute(Future<T> result) {
		future.notifyFutures();
	}

	// public <R, E> Future<R> pipe(FutureTask<R, E> task, FutureCallable<R>
	// futureCallable, E... arguments){
	// Future<R> future = task.executeFuture(arguments);
	// Future.whenResolved(future, new FutureCallable<R>() {
	//
	// @Override
	// public void onSucces(R result) {
	// executeFuture(result);
	// }
	//
	// @Override
	// public void onError(Exception e) {
	// // TODO Auto-generated method stub
	//
	// }
	// });
	// return future;
	// }

	//geef de Futures als argumenten bij executeFuture()
	
	protected abstract T doInBackgroundFuture(S... uri) throws Exception;

}
