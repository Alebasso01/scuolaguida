package com.example.scuolaguida.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

// NOTE: With firebase we have to do a network request --> We need to add the permission in the AndroidManifest.xml
//      -> ref: https://developer.android.com/training/basics/network-ops/connecting

// Firebase auth - https://firebase.google.com/docs/auth/android/start?hl=en#java
// Firebase db - https://firebase.google.com/docs/database/android/start?hl=en

// 1) Create a new project from - https://firebase.google.com/ (console: https://console.firebase.google.com/u/0/)
// 2) Enable authentication: Build > Authentication > Get started , then enable Email/password (or other auth types)
// 3a) In Android Studio: Tools > Firebase > Authentication (or Realtime Database or the thing that you need!)
//      ( Then follow the instructions )
// 3b) Alternative you can connect firebase to your Android app - https://firebase.google.com/docs/android/setup?hl=en#register-app

public class FirebaseWrapper {
    public static class Callback {
        private final static String TAG = Callback.class.getCanonicalName();
        private final Method method;
        private final Object thiz;

        public Callback(Method method, Object thiz) {
            this.method = method;
            this.thiz = thiz;
        }

        public static Callback newInstance(Object thiz, String name, Class<?>... prms) {
            Class<?> clazz = thiz.getClass();
            try {
                return new Callback(clazz.getMethod(name, prms), thiz);
            } catch (NoSuchMethodException e) {
                Log.w(TAG, "Cannot find method " + name + " in class " + clazz.getCanonicalName());

                // TODO: Better handling of the error
                throw new RuntimeException(e);
            }
        }

        public void invoke(Object... objs) {
            try {
                this.method.invoke(thiz, objs);
            } catch (IllegalAccessException | InvocationTargetException e) {
                Log.w(TAG, "Something went wrong during the callback. Message: " + e.getMessage());

                // TODO: Better handling of such an error
                throw new RuntimeException(e);
            }
        }
    }

    // Auth with email and password: https://firebase.google.com/docs/auth/android/password-auth?hl=en
    public static class Auth {
        private final static String TAG = Auth.class.getCanonicalName();
        private final FirebaseAuth auth;

        public Auth() {
            this.auth = FirebaseAuth.getInstance();
        }

        public boolean isAuthenticated() {
            return this.auth.getCurrentUser() != null;
        }

        public FirebaseUser getUser() {
            return this.auth.getCurrentUser();
        }

        public void signOut() {
            this.auth.signOut();
        }

        public void signIn(String email, String password, FirebaseWrapper.Callback callback) {
            this.auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            callback.invoke(task.isSuccessful());
                        }
                    });
        }

        public void signUp(String email, String password, FirebaseWrapper.Callback callback) {
            this.auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            callback.invoke(task.isSuccessful());
                        }
                    });
        }

        public String getUid() {
            // TODO: remove this assert and better handling of non logged-in users
            assert this.isAuthenticated();
            return this.getUser().getUid();
        }
    }

    // Database
    // Choose the db: https://firebase.google.com/docs/database/rtdb-vs-firestore?hl=en&authuser=1
    /*
    // NB: For security reason update the access rules from firebase console --> e.g., an user can access on its data!
    //  https://firebase.google.com/docs/database/security/get-started?hl=en&authuser=1
    // rules: https://firebase.google.com/docs/rules/rules-and-auth?authuser=0
    // doc: https://firebase.google.com/docs/rules/basics?utm_source=studio#realtime-database_5
    // Example
    {
      "rules": {
        "events": {
          "$uid": {
            ".read": "auth.uid === $uid",
            ".write": "auth.uid === $uid",
          }
        }
      },
    }
     */
    public static class RTDatabase {
        private final static String TAG = RTDatabase.class.getCanonicalName();

        // This is the name of the root of the DB (in the JSON format)
        private static final String CHILD = "events";

        private DatabaseReference getDb() {
            /*
            NOTE: I suppose that the DB is structured as:
            "events" : {
                "<UID_user1> : {..},
                "<UID_user2> : {..},
                ...
            }
            You have to change the child name(s) based on the structure of your JSON object
             */
            // https://firebase.google.com/docs/projects/locations?hl=it#rtdb-locations
            DatabaseReference ref =
                    FirebaseDatabase
                            .getInstance("https://console.firebase.google.com/project/scuolaguida-5fc9e/database/scuolaguida-5fc9e-default-rtdb/data/~2F")
                            .getReference(CHILD);

            // Return only the events of the current user
            String uid = new FirebaseWrapper.Auth().getUid();
            if (uid == null) {
                return null;
            }

            return ref.child(uid);
        }

        public void writeDbData(MyEvent myEvent) {
            DatabaseReference ref = getDb();
            if (ref == null) {
                return;
            }

            // NOTE: You can attach listeners to handle the results (e.g., onSuccessListener, ..)
            ref.child(String.valueOf(myEvent.getEventID())).setValue(myEvent);
        }

        public void readDbData(FirebaseWrapper.Callback callback) {
            DatabaseReference ref = getDb();
            if (ref == null) {
                return;
            }

            // Read from the database
            ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    callback.invoke(task);
                }
            });
        }
    }
}
