package ast;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import jd.Activator;

import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.preference.IPreferenceStore;

import preferences.PreferenceConstants;

public class LibraryClassStorage extends Indexer {
	private static LibraryClassStorage instance;
	private LinkedList<IClassFile> iClassFileList;
	private LinkedList<CompilationUnit> compilationUnitList;
	private Set<IClassFile> unMatchedClassFiles;
	
	private LibraryClassStorage() {
		super();
		this.iClassFileList = new LinkedList<IClassFile>();
		this.compilationUnitList = new LinkedList<CompilationUnit>();
		this.unMatchedClassFiles = new LinkedHashSet<IClassFile>();
	}
	
	public static LibraryClassStorage getInstance() {
		if(instance == null) {
			instance = new LibraryClassStorage();
		}
		return instance;
	}
	
	public CompilationUnit getCompilationUnit(IClassFile classFile) {
		if(iClassFileList.contains(classFile)) {
			int position = iClassFileList.indexOf(classFile);
			return compilationUnitList.get(position);
		}
		else {
			CompilationUnit compilationUnit = null;
			try {
				if(!unMatchedClassFiles.contains(classFile)) {
					ASTParser parser = ASTParser.newParser(AST.JLS4);
					parser.setSource(classFile);
					parser.setResolveBindings(true);
					compilationUnit = (CompilationUnit)parser.createAST(null);
					
					IPreferenceStore store = Activator.getDefault().getPreferenceStore();
					int maximumCacheSize = store.getInt(PreferenceConstants.P_LIBRARY_COMPILATION_UNIT_CACHE_SIZE);
					if(iClassFileList.size() < maximumCacheSize) {
						iClassFileList.add(classFile);
						compilationUnitList.add(compilationUnit);
					}
					else {
						iClassFileList.removeFirst();
						compilationUnitList.removeFirst();
						iClassFileList.add(classFile);
						compilationUnitList.add(compilationUnit);
					}
				}
			}
			catch(IllegalStateException e) {
				unMatchedClassFiles.add(classFile);
			}
			return compilationUnit;
		}
	}
}
