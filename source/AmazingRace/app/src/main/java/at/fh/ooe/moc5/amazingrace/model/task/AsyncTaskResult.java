package at.fh.ooe.moc5.amazingrace.model.task;

/**
 * Created by Thomas on 12/25/2015.
 * This class represents an async task result which allows handling of any occurred exception
 * in the postExecute method instead of the doInBackground where we are not in the UI Thread.
 */
public class AsyncTaskResult<T> {

    public final T result;
    public final Exception exception;

    public AsyncTaskResult(T result, Exception exception) {
        this.result = result;
        this.exception = exception;
    }
}
