package course.labs.modernartui;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;

import java.util.HashMap;
import java.util.Random;

/* --------------------------------------------------------------------------------------------- */

/**
 * "Coursera" | "Programming Mobile Applications for Android Handheld Systems" | Lab: "Modern Art UI"</br>
 *</br>
 * Lab description:</br>
 * A simple application that creates and displays a user interface, presents a "menu" and a "dialog", and
 * that opens a link to an external website.
 * Specifically, this application's user interface is composed of geometric shapes arranged in a
 * particular order. It has one area containing multiple colored rectangles and
 * another one containing a "SeekBar" (sometimes called a "Slider"). When the user drags the "SeekBar",
 * all non-white / non-gray rectangles gradually change their color. This application also contains
 * an (overflow) "options" menu. When the user clicks on the "options" menu an option labeled,
 * "More Information" should appear. When the user clicks on the "More Information" option, a "Dialog"
 * should appear displaying some text and two buttons. If the user clicks on the "Not Now" button in
 * the "Dialog", the "Dialog" should be dismissed. If the user clicks on the "Visit MOMA" button,
 * then the "Web Browser" should be opened to view a web page on the "MoMA.org" web site.</br>
 *</br>
 * Lab requirements:</br>
 * The application must implement all the functions described above, but can vary in the visual layout
 * as long as:</br>
 * - 1) user interface displays at least 5 separate colored rectangles, at least one of these
 *      rectangles is "white" / "gray" in color, and at least one of these rectangles is "non-white" /
 *      "non-gray" in color [*].</br>
 * - 2) it includes a "SeekBar".</br>
 * - 3) dragging this app's "SeekBar", the colored rectangles change color.</br>
 * - 4) it allows the user to view an "options" menu displaying the text, "More Information".</br>
 * - 5) when the user clicks on the "More Information" option, the app presents a "Dialog" with two
 *      buttons labeled "Visit MOMA" and "Not Now".</br>
 * - 6) when the user clicks on the "Dialog" button labeled, "Not Now", the "Dialog" disappears.</br>
 * - 7) when the user clicks on the "Dialog" button labeled, "Visit MOMA", a browser opens displaying
 *      a page at the "www.moma.org" web site.</br>
 *</br>
 * [*]: application's user interface is composed of five (5) colored rectangles. In practice, the
 * rectangle with a "white" background color is randomly selected each time the application starts.
 * The background color of each of the remaining (four) rectangles is randomly selected each time
 * the application starts.</br>
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
 * @see <a href='http://aristotelis-metsinis.github.io/'>http://aristotelis-metsinis.github.io/</a>
 *
 */
public class ModernArtUIActivity extends Activity implements MoreInformationDialog.DialogListener
{
    /** Declare string for "LogCat" documentation. */
    private final static String TAG = "ModernArtUI";

    /** Declare "HashMap" that will store for each "rectangle" (acting as "HashMap" key) its background
    color (acting as "HashMap" value). */
    private HashMap rectangleBackgroundColors = null;

    /* ----------------------------------------------------------------------------------------- */

    /**
     * This class extends "Activity". The "onCreate()" callback method is called when the "activity"
     * is starting. This method is overridden to inflate the activity's UI, to programmatically
     * interact with widgets in the UI, etc.
     *
     * @param		savedInstanceState		If the "activity" is being re-initialized after previously
     *                                      being shut down then this "Bundle" contains the data it
     *                                      most recently supplied in "onSaveInstanceState(Bundle)".
     *                                      Otherwise it is null.
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        /* Call the super class's implementation of this method. */
        super.onCreate(savedInstanceState);
        /* Set the "activity" content from the given "layout" resource. The resource will be inflated,
        adding all top-level views to the "activity". */
        setContentView(R.layout.activity_modern_art_ui);

        /* In case where previous state has been saved, restore colors from saved state. */
        if (savedInstanceState != null)
        {
            /* Get the "rectangles versus colors" HashMap object (acting as "value")
            associated with the given "key" (tag). */
            rectangleBackgroundColors = (HashMap) savedInstanceState.getSerializable("rectangleBackgroundColors");
        }
        /* In case where previous state has not been saved, initialise colors. */
        else
        {
            /* Call initialisation method and store the "rectangles versus colors" HashMap object. */
            rectangleBackgroundColors = initialiseBackgroundColors();
        }

        /* Call "setBackgroundColor()" method that "sets" the given (original) background color for a given
        rectangle (View). The (original) color has been stored in the proper "rectangles versus colors"
        HashMap object. */
        setBackgroundColor(findViewById(R.id.rect_up_left), (int) rectangleBackgroundColors.get("rect_up_left"));
        setBackgroundColor(findViewById(R.id.rect_down_left), (int) rectangleBackgroundColors.get("rect_down_left"));
        setBackgroundColor(findViewById(R.id.rect_up_right), (int) rectangleBackgroundColors.get("rect_up_right"));
        setBackgroundColor(findViewById(R.id.rect_center_right), (int) rectangleBackgroundColors.get("rect_center_right"));
        setBackgroundColor(findViewById(R.id.rect_down_right), (int) rectangleBackgroundColors.get("rect_down_right"));

        /* Find the "SeekBar" (View) identified by the given "id" attribute from the "XML" that
        was processed in this method. */
        final SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        /* Set a listener to receive notifications of changes to the SeekBar's progress level. */
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * Notify when the progress level has changed. This method is overridden to update
             * properly the background color of the rectangles (Views).
             *
             * @param		seekBar		The "SeekBar" whose progress has changed.
             * @param		progress	The current "SeekBar" progress level. This will be in the
             *                          range 0...max ("max" as defined in the "layout" XML).
             * @param		fromUser	"true" if the progress change was initiated by the user.
             *
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                /* Call "updateBackgroundColor()" method that "updates" the background color of a given
                rectangle (View). The new color is "computed" based upon the "original" background color
                of this rectangle and the current "SeekBar" progress level. Recall that the "original"
                background color has been stored in the proper "rectangles versus colors"
                HashMap object that has been initialised (created) at application's start-up. */
                updateBackgroundColor(findViewById(R.id.rect_up_left), (int) rectangleBackgroundColors.get("rect_up_left"), progress);
                updateBackgroundColor(findViewById(R.id.rect_down_left), (int) rectangleBackgroundColors.get("rect_down_left"), progress);
                updateBackgroundColor(findViewById(R.id.rect_up_right), (int) rectangleBackgroundColors.get("rect_up_right"), progress);
                updateBackgroundColor(findViewById(R.id.rect_center_right), (int) rectangleBackgroundColors.get("rect_center_right"), progress);
                updateBackgroundColor(findViewById(R.id.rect_down_right), (int) rectangleBackgroundColors.get("rect_down_right"), progress);
            }

            /**
             * Notify when the user has started a touch gesture.
             * Auto-generated method stub; nothing to implement.
             *
             * @param		seekBar		The "SeekBar" in which the touch gesture began.
             *
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                /* Auto-generated method stub; nothing to implement. */
            }

            /**
             * Notify when the user has finished a touch gesture.
             * Auto-generated method stub; nothing to implement.
             *
             * @param		seekBar		The "SeekBar" in which the touch gesture began.
             *
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                /* Auto-generated method stub; nothing to implement. */
            }
        });
    }

    /* ----------------------------------------------------------------------------------------- */

    /**
     * Initialize the contents of the Activity's standard "options" menu.
     *
     * @param		menu		The "options" menu in which we place our items.
     * @return 	     	        boolean - "true" for the menu to be displayed.
     *
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Inflate the "menu"; the items and sub-menus will be added to this "Menu" (the action bar
        if it is present). Return a "MenuInflater" with this context and inflate a menu hierarchy
        from the given "XML" resource. */
        getMenuInflater().inflate(R.menu.menu_modern_art_ui, menu);

        /* "true" for the menu to be displayed. */
        return true;
    }

    /* ----------------------------------------------------------------------------------------- */

    /**
     * Called whenever an item in "options" menu is selected. This method is overridden to
     * handle action bar item clicks.
     *
     * @param		item		The "menu" item that was selected.
     * @return 	        	    boolean - "true" to handle "menu" processing in this method or
     *                          "false" to allow normal "menu" processing to proceed.
     *
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Get the identifier for the "selected" menu item. */
        int id = item.getItemId();

        /* (overflow) "options" menu has been selected. */
        if (id == R.id.overflow_menu)
        {
            /* Create an instance of the "dialog" fragment. */
            DialogFragment dialog = new MoreInformationDialog();
            /* Get the "FragmentManager" for interacting with "fragments" associated with this
            fragment's activity and then display the "dialog", adding the "fragment" to the given
            "FragmentManager" with the given (fragment). */
            dialog.show(getFragmentManager(), "moreInformationDialog");

            /* "true"; menu processing handled by this method. */
            return true;
        }

        /* (in any other case) call through to the base class for it to perform the default
        "menu" handling; the default implementation simply returns "false". */
        return super.onOptionsItemSelected(item);
    }

    /* ----------------------------------------------------------------------------------------- */

    /**
     * This "activity" hosts a "dialog" by creating an instance of the "dialog" with the "dialog" fragment's
     * constructor and receives the dialog's events through an implementation of the "DialogListener"
     * interface. So, the "dialog" fragment receives a reference to this "activity" through the
     * "Fragment.onAttach()" callback, which it uses to call the following method
     * defined by the "MoreInformationDialog.DialogListener" interface.
     *
     * @param		dialog		"DialogFragment" in case this host "activity" needs to query it.
     *
     */
    @Override
    public void onDialogPositiveClick(DialogFragment dialog)
    {
        /* User touched the dialog's "positive" button. Start a browser "activity" to view a web page;
        create a base "intent" for viewing a "url". */
        Intent baseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url)));

        /* Start the "activity", using this "intent". */
        startActivity(baseIntent);
    }

    /* ----------------------------------------------------------------------------------------- */

    /**
     * This "activity" hosts a "dialog" by creating an instance of the "dialog" with the "dialog" fragment's
     * constructor and receives the dialog's events through an implementation of the "DialogListener"
     * interface. So, the "dialog" fragment receives a reference to this "activity" through the
     * "Fragment.onAttach()" callback, which it uses to call the following method
     * defined by the "MoreInformationDialog.DialogListener" interface.
     *
     * @param		dialog		"DialogFragment" in case this host "activity" needs to query it.
     *
     */
    @Override
    public void onDialogNegativeClick(DialogFragment dialog)
    {
        /* User touched the dialog's "negative" button; nothing to implement. */
    }

    /* ----------------------------------------------------------------------------------------- */

    /**
     * When an "Activity" (fragment) is being killed, but may be restarted later, Android calls
     * "onSaveInstanceState()". This gives the "Activity" a chance to save any per-instance data
     * (to a "Bundle"; a collection of key-value pairs) it may need if the "Activity" is later
     * restored. In this case, it saves the background colors of the rectangles; actually the
     * "HashMap" object that stores those colors (acting as "values") for each of those rectangles
     * (acting as "keys").
     *
     * @param		savedInstanceState		"Bundle" in which to place the saved state.
     *
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        /* Define a "key" (tag) and insert a "Serializable" value ("HashMap" object) into the mapping
        of this "Bundle". */
        savedInstanceState.putSerializable("rectangleBackgroundColors", rectangleBackgroundColors);
        /* Call the default implementation taking care of most of the UI per-instance state. */
        super.onSaveInstanceState(savedInstanceState);
    }

    /* ----------------------------------------------------------------------------------------- */

    /**
     * Helper method used for the initialisation of the background color of each of the rectangles of
     * application's user interface. Application's user interface is composed of five (5) colored
     * rectangles. The rectangle with a "white" background color is randomly selected each time the
     * application starts. The background color of each of the remaining (four) rectangles is randomly
     * selected each time the application starts.
     *
     * @return		    		"HashMap" object that stores the background colors (acting as "values")
     *                          of application's UI rectangles (acting as "keys").
     *
     */
    private HashMap initialiseBackgroundColors()
    {
        /* Construct a new empty "HashMap" instance. */
        HashMap rectangleBackgroundColors = new HashMap();

        /* Define a "helper" HashMap that maps the rectangles (acting as "values" this time) to (numerical)
        "ids" (acting as "keys"). Keep in mind that this particular "HashMap" object is being
        initialised by sub-classing the "HashMap" class to an anonymous class,
        and then using the non-static initialization block to call the "put()" method as
        many times as the number of application's UI rectangles. */
        HashMap rectangleIDs = new HashMap(){{
            put(0, "rect_up_left");
            put(1, "rect_down_left");
            put(2, "rect_up_right");
            put(3, "rect_center_right");
            put(4, "rect_down_right");
        }};

        /* Construct a random generator. */
        Random random = new Random();

        /* Get a pseudo-random uniformly distributed integer in the half-open range [0, 5).
        In practice, (randomly) "select" the "id" of the rectangle with a "white" background color.
        Recall that the number of application's UI rectangles is five (5) with "ids" in the range [0, 4]. */
        int whiteRectangleID = random.nextInt(5);
        /* Get the rectangle (value) of the mapping with the specified (above computed) "id" (key). */
        String rectangleName = (String) rectangleIDs.get(whiteRectangleID);
        /* Map (store) the specified (randomly selected) rectangle (acting as key) to the specified background
        (white) color (acting as value). */
        rectangleBackgroundColors.put(rectangleName, getResources().getColor(R.color.white));

        /* Set a "random" background color for each of the remaining application's UI rectangles;
        loop for all above defined [0-4] rectangle "ids". */
        for (int rectangleID = 0; rectangleID <= 4; rectangleID++)
        {
            /* Skip the rectangle colored "already" with a "white" background. */
            if (rectangleID != whiteRectangleID)
            {
                /* Get the rectangle (value) of the mapping with the specified "id" (key). */
                rectangleName = (String) rectangleIDs.get(rectangleID);

                /* Get pseudo-random uniformly distributed integers in the half-open range [0, 256).
                Each of those integers correspond to a "randomly" selected "red", "green" and "blue"
                color component. Use those (component) values to get a color-int by calling "rgb()"
                method. Recall that these component values must be in [0..255] range. */
                int color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
                /* While this "rectangles versus colors" map contains the above computed color (value),
                select (compute) a new background color. */
                while (rectangleBackgroundColors.containsValue(color)) {
                    color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
                }

                /* Map (store) the specified rectangle (acting as key) to the specified (randomly selected)
                background color (acting as value). */
                rectangleBackgroundColors.put(rectangleName, color);
            }
        }

        /* Return the "rectangles versus colors" HashMap object. */
        return rectangleBackgroundColors;
    }

    /* ----------------------------------------------------------------------------------------- */

    /**
     * Helper method; set the background color of a "View" UI component.
     *
     * @param		view		                Applications' UI "View".
     * @param		backgroundColor		        The background color to be set.
     *
     */
    private void setBackgroundColor(View view, int backgroundColor)
    {
        /* Get the background drawable. */
        GradientDrawable backgroundDrawable = (GradientDrawable) view.getBackground();
        /* Set the drawable's color value; change this drawable to use a single (argb) color instead of a
        gradient. */
        backgroundDrawable.setColor(backgroundColor);
    }

    /* ----------------------------------------------------------------------------------------- */

    /**
     * Helper method; update the background color of a "View" component based upon the progress level of a
     * "SeekBar".
     *
     * @param		view		                Applications' UI "View".
     * @param		backgroundColor		        The (argb) background color to be updated.
     * @param		progress		            The current "SeekBar" progress level.
     *
     */
    private void updateBackgroundColor(View view, int backgroundColor, int progress) {
        /* Three element array, which holds the resulting "HSV" components;
        hsv[0] is Hue [0...360), hsv[1] is Saturation [0...1] and hsv[2] is Value [0...1]. */
        float[] hsv = new float[3];

        /* Convert the "argb" color to its "HSV" components. */
        Color.colorToHSV(backgroundColor, hsv);
        /* Update "Hue" based upon the (current) "SeekBar" progress level; result shall always be
        in the [0..360) range due to the "modulo" operation. */
        hsv[0] = (hsv[0] + progress) % 360;

        /* Convert the updated "HSV" components to an "ARGB" color (the alpha component is passed
        through unchanged) and then set the background color for the given "View". */
        setBackgroundColor(view, Color.HSVToColor(Color.alpha(backgroundColor), hsv));
    }

    /* ----------------------------------------------------------------------------------------- */
}