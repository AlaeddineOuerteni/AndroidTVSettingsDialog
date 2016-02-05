package zgaw.settings.androidtv;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.android.tv.settings.old.Action;
import com.android.tv.settings.old.ActionAdapter;
import com.android.tv.settings.old.ActionFragment;
import com.android.tv.settings.old.ContentFragment;
import com.android.tv.settings.old.DialogActivity;

/**
 * The class description here.
 *
 * @author Alaeddine Ouertani
 * @since 2016.02.05
 */
public class SettingsActivity
    extends DialogActivity
    implements ActionAdapter.Listener
{

  private static final String LANGUAGE_PREFERENCE_KEY = "languagePreferenceKey";

  private static final String KEY_LOCALE = "locale";

  private Fragment fragment;

  private ActionFragment actionFragment;

  private ArrayList<Action> actions;

  private String[] languages;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    init();
    setContentAndActionFragments(fragment, actionFragment);
  }

  @Override
  public void onActionClicked(Action action)
  {
    final String key = action.getKey();
    if (key.contains(KEY_LOCALE))
    {
      final String s = key.substring(KEY_LOCALE.length());
      final int offset = Integer.parseInt(s);
      setLanguagePreference(offset);
    }
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig)
  {
    super.onConfigurationChanged(newConfig);
    makeContentFragment();
    setContentFragment(fragment);
  }

  private final void makeContentFragment()
  {
    fragment = ContentFragment.newInstance(getString(R.string.languages), null, null, R.drawable.ic_settings_language, getResources().getColor(R.color.background));
  }

  private final void init()
  {
    actions = new ArrayList<>();
    makeContentFragment();
    languages = getResources().getStringArray(R.array.array_languages);

    for (int index = 0; index < languages.length; index++)
    {

      final StringBuilder sb = new StringBuilder();
      sb.append(KEY_LOCALE).append(index);
      actions.add(new Action.Builder().key(sb.toString()).title(languages[index]).checked(languages[index].equals(PreferenceManager.getDefaultSharedPreferences(this).getString(LANGUAGE_PREFERENCE_KEY, null))).build());
    }

    actionFragment = ActionFragment.newInstance(actions);
  }

  private final void setLanguagePreference(int offset)
  {
    final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    defaultSharedPreferences.edit().putString(LANGUAGE_PREFERENCE_KEY, languages[offset]).commit();

    Toast.makeText(this, languages[offset], Toast.LENGTH_SHORT).show();
  }
}