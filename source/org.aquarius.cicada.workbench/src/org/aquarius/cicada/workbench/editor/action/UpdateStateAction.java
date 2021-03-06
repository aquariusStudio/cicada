package org.aquarius.cicada.workbench.editor.action;

import java.util.List;

import org.aquarius.cicada.core.RuntimeManager;
import org.aquarius.cicada.core.model.Movie;
import org.aquarius.cicada.workbench.editor.action.base.AbstractSelectionAction;
import org.aquarius.util.enu.RefreshType;

/**
 * Update the selected movies to the specified state.<BR>
 *
 * @author aquarius.github@gmail.com
 *
 */
public class UpdateStateAction extends AbstractSelectionAction {

	private int state;

	/**
	 *
	 * @param label
	 */
	public UpdateStateAction(String label, int state) {
		super(label);

		this.state = state;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected RefreshType internalRun(List<Movie> movieList) {
		for (Movie movie : movieList) {
			movie.setState(this.state);
		}

		RuntimeManager.getInstance().getStoreService().insertOrUpdateMovies(movieList);

		return RefreshType.Update;
	}

}
