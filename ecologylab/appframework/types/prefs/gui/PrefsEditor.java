package ecologylab.appframework.types.prefs.gui;

import java.util.HashMap;

import javax.swing.JComponent;

import ecologylab.appframework.types.prefs.MetaPref;
import ecologylab.appframework.types.prefs.MetaPrefSet;

public class PrefWidgetManager
{
    HashMap<String, HashMap<JComponent, MetaPref>> categoryToWidgets = new HashMap<String, HashMap<JComponent, MetaPref>>();

    public PrefWidgetManager(MetaPrefSet set)
    {
        for (MetaPref metaPref : set)
        {
            HashMap widgets = categoryToWidgets.get(metaPref.getCategory());

            if (widgets == null)
            {
                widgets = new HashMap<JComponent, MetaPref>();
                categoryToWidgets.put(metaPref.getCategory(), widgets);
            }

            widgets.put(metaPref.getWidget(), metaPref);
        }
    }

    // TODO rendering code -- iterate through each category
    // (categoryToWidgets.get(<categoryname>)) & render each widget
    // (categoryToWidgets.get(<categoryname>).keySet())
}
