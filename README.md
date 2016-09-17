## DevCrewCodingChallange


1) What is the major difference between an abstract class and an interface ? 
   Interface is just a pattern in which there is no concept of construction, means no constructor and no lookup. Whereas in abstract class there is a concept of constructor.
   
2) Why is Java 7’s class inheritance flawed?
    I don't think so.
    
3) What are the major differences between Activities and Fragments ?
    Fragment can not exist independently. Activities can exist independently without fragments. We can make app with no Fragment.
    
4)  When using ​Fragments ​ , how do you communicate back to their hosting ​Activity ​ ?
    By using callback interface. 
    public interface TaskCallbacks {
        void onPreExecute();

        void onCancelled();

        void onPostExecute(ArrayList<String> dataset);
    }
    Activity implements Interface
    and In Fragment
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (TaskCallbacks) context;

    }
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;

    }
    
5)  Can you make an entire app without ever using ​Fragments ​ ? Why or why not? Are there any special cases when you absolutely have to use or should use Fragments? 
    
  Yes we can make entire app without using fragments. However we should use fragments in about every app. There are some cases/requirements where we should use Fragments, like...
    
    - Make an app for tablets
    
    - Configuration specific UI
    
    - Complex and Idependent UI in Single Activity

6)  What makes an ​AsyncTask ​ such an annoyance to Android developers? Detail some of the issues with ​AsyncTask and how to potentially solve them?
    There are some issues with AsyncTask which makes it difficult for android developers to effectively implement it. 
    
    Here are some of them
    
    - (Main Issue) Async task is not tied to lifecycle of Activity, means when an Activity is recreated due to some reason i,e.. Config changes, AsyncTask keeps alive
    
    - Reference to activity(if any) is invalidated, results in resource leakage
    
    - Refence to progress dialog or Any UI element (if any) is Invalidated
    
    Solution to all these kind of issues is that AsyncTask should not keep reference to activity, or any UI Element directly
    we should use Headless Fragment to resolve this also I've used in this CodeChallange. The main concept is to make an Empty
    Retained Fragment and use call backs to communicate with the activity, and Activity in turn performs actions like showing
    dismissing dialog, updating UI etc.
    

    
