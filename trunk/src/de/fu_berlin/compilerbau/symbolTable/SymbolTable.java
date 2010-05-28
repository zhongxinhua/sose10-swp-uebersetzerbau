package de.fu_berlin.compilerbau.symbolTable;

import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;

public interface SymbolTable extends Map<Symbol,Symbol> {
	
	/**
	 * Parent of this element.
	 * @throws NoSuchElementException {@code this} is the {@code environment} or a {@link #viewFor() view}.
	 */
	SymbolTable getParent() throws NoSuchElementException;
	
	/**
	 * The children in the order they where created.
	 * @throws NoSuchElementException {@code this} is the {@code environment} or a {@link #viewFor() view}.
	 */
	Collection<SymbolTable> getListOfChildren() throws NoSuchElementException;
	
	/**
	 * {@code this} represents the {@link #getChildren() children}, {@link #getAncestors() ancestors} or
	 * 	{@link #getOffspring() offspring} of a {@code SymboleTable}.
	 * @return the element that created this view or null
	 */
	SymbolTable viewFor();
	
	/**
	 * Returns a list of all {@code SymboleTable}s iff this {@code SymboleTable} is an environment.
	 * @return List of all elements or null.
	 */
	Collection<Symbol> environmentOf();

	/**
	 * A view the parent and the parent's parent.
	 * @throws NoSuchElementException {@code this} is the {@code environment} or a {@link #viewFor() view}.
	 */
	
	SymbolTable getAncestors() throws NoSuchElementException;
	/**
	 * A view of all children <u>excluding</u> the children's children.
	 * @throws NoSuchElementException {@code this} is the {@code environment} or a {@link #viewFor() view}.
	 */
	
	SymbolTable getChildren() throws NoSuchElementException;
	/**
	 * A view of all children in <u>including</u> the children's children.
	 * @throws NoSuchElementException {@code this} is the{@link #viewFor() view}.
	 */
	SymbolTable getOffspring() throws NoSuchElementException;
	
	/**
	 * Spawn a new child {@code SymbolTable}.
	 */
	SymbolTable hatch();
	
	/**
	 * Adds a new child {@link Symbol} as a {@code SymbolTable}.
	 * @throws UnsupportedOperationException {@code this} is a view
	 */
	SymbolTable hatch(Symbol child) throws UnsupportedOperationException;
	
	/**
	 * Some SymbolTables may belong to a {@link Symbol} (methods, interfaces ...).
	 * @return null or this very {@code SymbolTable}.
	 * @throws UnsupportedOperationException {@code this} is a view
	 */
	Symbol isSymbol() throws UnsupportedOperationException;
	
}
