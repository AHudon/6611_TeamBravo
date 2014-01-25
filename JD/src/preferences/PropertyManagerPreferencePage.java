package preferences;

import jd.Activator;

import org.eclipse.jface.preference.*;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class PropertyManagerPreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	private IntegerFieldEditor projectCompilationUnitCacheSizeFieldEditor;
	private IntegerFieldEditor libraryCompilationUnitCacheSizeFieldEditor;
	
	public PropertyManagerPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		Composite composite = new Composite(getFieldEditorParent(), SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		
		Group sliceExtractionPreferenceGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		sliceExtractionPreferenceGroup.setLayout(new GridLayout(1, false));
		sliceExtractionPreferenceGroup.setText("Preferences");
	
		projectCompilationUnitCacheSizeFieldEditor = new IntegerFieldEditor(
				PreferenceConstants.P_PROJECT_COMPILATION_UNIT_CACHE_SIZE,
				"&Project CompilationUnit cache size:", sliceExtractionPreferenceGroup);
		projectCompilationUnitCacheSizeFieldEditor.setEmptyStringAllowed(false);
		addField(projectCompilationUnitCacheSizeFieldEditor);

		libraryCompilationUnitCacheSizeFieldEditor = new IntegerFieldEditor(
				PreferenceConstants.P_LIBRARY_COMPILATION_UNIT_CACHE_SIZE,
				"&Library CompilationUnit cache size:", sliceExtractionPreferenceGroup);
		libraryCompilationUnitCacheSizeFieldEditor.setEmptyStringAllowed(false);
		addField(libraryCompilationUnitCacheSizeFieldEditor);
	}

	protected void checkState() {
		super.checkState();
		try {
			int projectCompilationUnitCacheSize = projectCompilationUnitCacheSizeFieldEditor.getIntValue();
			if(projectCompilationUnitCacheSize >= 10) {
				setErrorMessage(null);
				setValid(true);
			}
			else {
				setErrorMessage("Cache size is recommended to be >= 10");
				setValid(false);
				return;
			}
		}
		catch(NumberFormatException e) {
			setErrorMessage("Cache size must be an Integer");
			setValid(false);
			return;
		}
		try {
			int libraryCompilationUnitCacheSize = libraryCompilationUnitCacheSizeFieldEditor.getIntValue();
			if(libraryCompilationUnitCacheSize >= 20) {
				setErrorMessage(null);
				setValid(true);
			}
			else {
				setErrorMessage("Cache size is recommended to be >= 20");
				setValid(false);
				return;
			}
		}
		catch(NumberFormatException e) {
			setErrorMessage("Cache size must be an Integer");
			setValid(false);
			return;
		}
	}

	public void propertyChange(PropertyChangeEvent event) {
		super.propertyChange(event);
		if(event.getProperty().equals(FieldEditor.VALUE)) {
			checkState();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
}