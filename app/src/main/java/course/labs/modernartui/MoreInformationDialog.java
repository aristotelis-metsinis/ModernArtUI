package course.labs.modernartui;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/* --------------------------------------------------------------------------------------------- */

/**
 * This application contains an (overflow) "options" menu. When the user clicks on that "options"
 * menu an option labeled, "More Information" should appear. When the user clicks on the
 * "More Information" option, the "dialog" (fragment) implemented by "this" class should appear
 * displaying some text and two "buttons". If the user clicks on the "Not Now" button in the
 * "dialog", the "dialog" should be dismissed. If the user clicks on the "Visit MOMA" button, then
 * the "Web Browser" should be opened to view a web page on the "MoMA.org" web site.</br>
 *</br>
 * Notes:</br>
 * - the app has been tested on the emulator using a "Google Galaxy Nexus AVD - 4.3" with "API"
 * level 18 (screen resolution: 720 x 1280). To limit configuration problems, you should test the app
 * against a similar AVD.</br>
 * - keep also in mind that the "Genymotion" Android emulator used. Have a look at:</br>
 * <a href='https://www.genymotion.com/'>https://www.genymotion.com/</a></br>
 * - the project has been created, developed and configured on "Android Studio" IDE. Have a look at:</br>
 * <a href='http://developer.android.com/tools/studio/index.html'>http://developer.android.com/tools/studio/index.html</a></br>
 * - screencast video of the app in action can be found at:</br>
 * <a href='https://youtu.be/bWY8zdC_AvM'>https://youtu.be/bWY8zdC_AvM</a></br>
 *
 * @author Aristotelis Metsinis (aristotelis.metsinis@gmail.com)
 * @version 1.0
 * @since 2015-06-04
 * @see <a href='http://www.thinkageek.com/metsinis/'>http://www.thinkageek.com/metsinis/</a>
 *
 */
public class MoreInformationDialog extends DialogFragment
{
    /** Declare string for "LogCat" documentation. */
    private final String TAG = "ModernArtUI";

    /** Use this instance of the "interface" to deliver action events back to the host "activity". */
    DialogListener mListener;

    /* ----------------------------------------------------------------------------------------- */

    /**
     * This class extends "DialogFragment", creates and returns a "Dialog" in the "onCreateDialog()"
     * callback method. This method is overridden to build a custom "Dialog" container.
     *
     * @param		savedInstanceState		The last saved instance state of the "Fragment", or null
     *                                      if this is a freshly created "Fragment".
     * @return 	     	                    Return a new "Dialog" instance to be displayed by the
     *                                      "Fragment".
     *
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        /* Create a "Dialog" window that uses the "Activity" (context) this "fragment" is currently
        associated with and the default "dialog" frame style. */
        Dialog dialog = new Dialog(getActivity());
        /* Retrieve the current "Window" for the "activity" and enable the "no title" extended screen
        feature, turning off the title at the top of the screen. */
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        /* Set the screen content from the given custom "layout" resource. */
        dialog.setContentView(R.layout.dialog_more_information);
        /* Start the "dialog" and display it on screen. */
        dialog.show();

        /* Find a child (button) "View" with the given identifier. */
        Button mButton_not_now = (Button) dialog.findViewById(R.id.dialog_not_now_button);
        /* Register a callback to be invoked when this (button) "View" is clicked. */
        mButton_not_now.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when this (button) "View" has been clicked. This method is overridden to
             * define the action to take when the user presses this button; the "dialog"
             * should be just dismissed finally. In practice, the method shall send a "negative"
             * button event back to the host "activity" that will be "ignored" completely.
             *
             * @param		view		The (button) "View" that was clicked.
             *
             */
            @Override
            public void onClick(View view) {
                /* Send a "negative" button event back to the host "activity"
                passing the "DialogFragment" in case the host needs to query it. */
                mListener.onDialogNegativeClick(MoreInformationDialog.this);
                /* Dismiss the "fragment" and its "dialog". */
                dismiss();
            }
        });

        /* Find a child (button) "View" with the given identifier. */
        Button mButton_visit = (Button) dialog.findViewById(R.id.dialog_visit_moma_button);
        /* Register a callback to be invoked when this (button) "View" is clicked. */
        mButton_visit.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when this (button) "View" has been clicked. This method is overridden to
             * define the action to take when the user presses this button; the "Web Browser" should
             * be opened to view a web page on the "MoMA.org" web site finally. In practice, the
             * method shall send a "positive" button event back to the host "activity" that will
             * proceed with the proper "actions" displaying the web page.
             *
             * @param		view		The (button) "View" that was clicked.
             *
             */
            @Override
            public void onClick(View view) {
                /* Send a "positive" button event back to the host "activity"
                passing the "DialogFragment" in case the host needs to query it. */
                mListener.onDialogPositiveClick(MoreInformationDialog.this);
                /* Dismiss the "fragment" and its "dialog". */
                dismiss();
            }
        });

        /* Return the "Dialog" instance. */
        return dialog;
    }

    /* ----------------------------------------------------------------------------------------- */

    /**
     * Called when the "fragment" is first attached to its "activity". The "Fragment.onAttach()" method
     * is overridden to instantiate the "DialogListener" interface.
     *
     * @param		activity		    Attached "activity".
     * @exception 	ClassCastException  Exception on error.
     *
     */
    @Override
    public void onAttach(Activity activity) throws ClassCastException
    {
        super.onAttach(activity);

        /* Verify that the host "activity" implements the callback interface. */
        try
        {
            /* Instantiate the "DialogListener" interface so we can send events back to the host. */
            mListener = (DialogListener) activity;
        }
        catch (ClassCastException exception)
        {
            /* The activity doesn't implement the "DialogListener" interface, throw exception. */
            throw new ClassCastException(activity.toString() + " must implement \"DialogListener\"");
        }
    }

    /* ----------------------------------------------------------------------------------------- */

    /**
     * Called when the "fragment" is visible to the user. The "DialogFragment.onStart()" method is
     * overridden to set the proper "style" as the dialog's "window animations" to use on showing and
     * hiding the "dialog" (fragment).
     *
     */
    @Override
    public void onStart()
    {
        super.onStart();

        /* Retrieve the current "Window" for this "activity" and specify custom animations to use
        for this "window" based upon the given "style". */
        getDialog().getWindow().setWindowAnimations(R.style.dialog_animation_fade);
    }

    /* ----------------------------------------------------------------------------------------- */

    /**
     * The "activity" that creates an instance of this "dialog" fragment must
     * implement this "interface" in order to receive event callbacks.
     * Each method passes the "DialogFragment" in case the host needs to query it.
     *
     */
    public interface DialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    /* ----------------------------------------------------------------------------------------- */
}