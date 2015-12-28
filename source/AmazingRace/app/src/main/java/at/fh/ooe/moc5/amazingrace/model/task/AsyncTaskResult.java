package at.fh.ooe.moc5.amazingrace.model.task;

/**
 * Created by Thomas on 12/25/2015.
 */
public class AsyncTaskResult<T> {

    public final T result;
    public final Exception exception;

    public AsyncTaskResult(T result, Exception exception) {
        this.result = result;
        this.exception = exception;
    }
}
