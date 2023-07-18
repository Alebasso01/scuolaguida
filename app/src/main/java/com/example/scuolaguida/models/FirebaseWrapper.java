package com.example.scuolaguida.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
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
public class FirebaseWrapper {
   private FirebaseHelper firebaseHelper;


    public FirebaseWrapper() {
        // Inizializza l'istanza di FirebaseHelper
        firebaseHelper = new FirebaseHelper();
    }
    public void addLessonForUser(String prova) {
        // Utilizza l'istanza di FirebaseHelper per aggiungere la lezione per l'utente specifico
        firebaseHelper.addLessonForUser(prova);
    }
    public static String getuserid(){
       return RTDatabase.getUserID();
    }
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
        FirebaseAuth auth;

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

    public static class RTDatabase {
        static String userID;
        private final static String TAG = RTDatabase.class.getCanonicalName();
        DatabaseReference ref = FirebaseDatabase.getInstance(
                "https://scuolaguida-5fc9e-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Users");

        // This is the name of the root of the DB (in the JSON format)
        private static final String CHILD = "Users";

        private DatabaseReference getDb() {
            // Return only the events of the current user
            String uid = new FirebaseWrapper.Auth().getUid();
            if (uid == null) {
                return null;
            }

            return ref.child(uid);
        }

        public void writeDbData(MyEvent myEvent) {
            DatabaseReference reference = getDb();
            if (reference == null) {
                return;
            }
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser currentuser = auth.getCurrentUser();


            if(currentuser!=null)
            {
               // userID = currentuser.getUid();
                ref = FirebaseDatabase.getInstance("https://scuolaguida-5fc9e-default-rtdb.europe-west1" +
                        ".firebasedatabase.app").getReference("Users");

            }
            ref.child(String.valueOf(myEvent.getEventID())).setValue(myEvent);
        }
        public static String getUserID(){
            return userID;
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


    public class FirebaseHelper {
        private DatabaseReference databaseReference;

        public FirebaseHelper() {
            // Ottieni l'istanza del database Firebase
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            // Ottieni il riferimento alla sezione "Users"
            databaseReference = database.getReference("Users");
        }
        public void addLessonForUser(String prova){
            DatabaseReference userRef = databaseReference.child("2");
            userRef.setValue(prova);

        }
    }
}
