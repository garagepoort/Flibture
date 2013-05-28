Flibture
========

Asynchronous Future implementation for Android

Flibture is a library for using futures in Android. You can start an asynchronous task which will return a Future object. To this Future you can attach a callback. This callback will be fired in case of success or error. Depending on success or error the appropriate method of the callback will be executed. The library has it's own class for using Asynchronous tasks. The FutureTask class.
Why use Flibture?
=================
Handling asynchronous task in android can be quite messy leaving scattered pieces everywhere in your code making it unreadable. And handling the response can be a real pain. Using Flibture improves the structuring of your code and leaves less tracks handling the response of an asynchronous task. The main advantage of using Flibture over the Android built-in AsyncTask, is that the handling of the result can be written where the call to execute something asynchronously is made. When using AsyncTask, the result will be handled is the separate AsyncTask class, which makes the code less readable. 

Using AsyncTask to process a String:
====================================

	public void doSomethingUsingAsyncTask() {
 
 		MyAsyncTask myAsyncTask = new MyAsyncTask();
		myAsyncTask.execute("Leuven");
	}
 
	@Override
	public void update(Observable arg0, Object result) {
    	// Handle the result
    	myTextView.setText((CharSequence) result);
	}
 
	public class MyAsyncTask extends Observable {
 
    	public void execute(String... params) {
        	new Task().execute(params);
    	}
 
    	class Task extends AsyncTask<String, String, String> {
 
        	@Override
        	protected String doInBackground(String... params) {
            	// Implementation of asynchronous task
            	String location = LocationService.getLocation(params[0]);
            	return location;
        	}
 
     	   @Override
        	protected void onPostExecute(String result) {
        	     // Notify activity
        	     setChanged();
        	     notifyObservers(result);
        	}
   	 }
	}
	
Because AsyncTask has to handle the result and does not have has access to UI components, the observer pattern has to be used to update the activity. 

Performing the same task using Flibture:
========================================

	
	public void doSomethingUsingFlibture() {
 
    		MyFutureTask myFutureTask = new MyFutureTask();
    		Future<String> myFuture = myFutureTask.executeFuture("Leuven");
    		Future.whenResolved(myFuture, new FutureCallable<String>() {
 
        		@Override
        		public void onError(Exception exception) {
        		    // Handle the exception
        		    exception.printStackTrace();
        		}
 	
        		@Override
        		public void onSucces(String result) {
        		    // Handle the result
        		    myTextView.setText(result);
        		}
    		});
    	}
 
	public class MyFutureTask extends FutureTask<String, String> {
 
    		@Override
    		protected String doInBackgroundFuture(String... arg0) throws Exception {
        		// Implementation of asynchronous task
        		String location = LocationService.getLocation(params[0]);
        		return location;
    		}
	}
	
By using Flibture the result can be handled anywhere. This way there is no need for an observer pattern.

Using Flibture
================
To use Flibture there are 3 basic steps you should follow.

    Making the FutureTask
    Implementing a FutureCallable
    Registering the FutureCallable.

FutureTask
================
First making an asynchronous task. You should now use the FutureTask class from the Flibture Library. You should now only override the doInBackgroundFuture(T ... arguments) method. The FutureTask uses generics

    The first generic is the type of value of the future that is returned by executeFuture().
    The second generic is the type of the parameters given to executeFuture().
	
	public class MyFutureTask extends FutureTask<String, Boolean> {
 
    		@Override
    		protected String doInBackgroundFuture(Boolean... arg0) throws Exception {
        		// Implementation of asynchronous task
        		String location = LocationService.getLocation(params[0]);
        		return location;
    		}
	}
	
FutureCallable
================
Now we can implement the FutureCallable. You have to implements the onSuccess and onError method. The onSucces method is called when the doInBackgroundFuture method of the FutureTask executes without throwing an exception. When this method does throw an exception the onError method of the FutureCallable will be called.

	
	public MyFutureCallable implements FutureCallable<String>() {
 
		    @Override
		    public void onError(Exception exception) {
        		// Handle the exception
        		exception.printStackTrace();
    		    }
 
 		    @Override
    	            public void onSucces(String result) {
        		// Handle the result
        		myTextView.setText(result);
    	            }
	}
	
whenResolved
================
Use the whenResolved function to register the FutureCallable with the future. When the future is resolved the appropriate method from the FutureCallable will be called.
	
	public void doSomethingUsingFlibture() {
 
    		MyFutureTask myFutureTask = new MyFutureTask();
    		Future<String> myFuture = myFutureTask.executeFuture("Leuven");
    		Future.whenResolved(myFuture, new MyFutureCallable<String>());
 
	}
	
Note: A future object may only be used once, when a future is resolved a new future needs to be used to perform a new task.
