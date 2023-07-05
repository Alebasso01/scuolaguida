package com.example.scuolaguida.models;

/* Services:
-) foreground: https://developer.android.com/guide/components/foreground-services
-) background: https://developer.android.com/guide/background
*/

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

public class MyWorker extends ListenableWorker {
    /**
     * @param appContext   The application {@link Context}
     * @param workerParams Parameters to setup the internal state of this worker
     */
    public MyWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        // Check events stored on firebase vs content provider
/*
        CalendarProvider cp = new CalendarProvider(this.getApplicationContext().getContentResolver());
        MyEvent.Collection<MyEvent> events = new MyEvent.Collection<>(cp.getAllEvents());

        // Check for new events
        // *) Retrieve the events in firebase
        // *) Check if there are new events and store them!
        try {
            /*
            NOTE: The current callback do not update any UI element!
            If you want to update the UI after you have retrieved the data in firebase,
            you have to put a "loading" image and then (in the callback) replace that
            image with the real data!
            (for instance, you can put several fragments and then replace them in the callbacks)

            new FirebaseWrapper.RTDatabase()
                    .readDbData(
                            new FirebaseWrapper.Callback(
                                    MyEvent.Collection.class.getMethod("firebaseDbCallback", Task.class),
                                    events));
        } catch (NoSuchMethodException e) {
            // TODO: Better handling of such an exception
            throw new RuntimeException(e);
        }

        // To return a value (if needed): Futures.immediateFuture(Result.success())*/
        return null;
    }
}

