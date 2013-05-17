package be.cegeka.android.flibture;


public interface  FutureCallable<T> {

	public void onSucces(T result);

	public void onError(Exception e);
}
